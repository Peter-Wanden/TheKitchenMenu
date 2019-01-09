package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.content.Intent;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.entity.UsersProductData;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import static com.example.peter.thekitchenmenu.app.Constants.ANONYMOUS;

/**
 * SyncManagers responsibility is to synchronise the minimum amount of data possible. This is
 * achieved by only synchronising models that are being observed.
 *
 * Data models in SyncManager have three states:
 * - observed and synchronised
 * - not observed and not synchronised
 * - not observed, but synchronised as other data models are dependent.
 *
 * SyncManager uses instances of {@link DataModelStatus} to maintain a list of data models in
 * strict dependency order.
 *
 * Pseudo code:
 * The {@link Repository} informs SyncManager of a data models (the target) observed state.
 *  - If the targets state is unchanged, ignore the request.
 *      - End.
 *  - If state has changed
 *      - Update the models status in the dependency list:
 *      - If the target model is now being observed.
 *      |   - Loop through its upstream dependencies.
 *      |       -> If the upstream model is observed, continue looping.
 *      |       -> If the upstream model is not observed:
 *      |           - Update its 'has dependencies' flag to true.
 *      |       - Keep looping until the target model has been processed.
 *      |           - Inform the signInChecker that that sync queue needs processing.
 *      |
 *      -> If the target model is not being observed.
 *          - Retrieve the next downstream dependency.
 *             |- If it is being observed flag the target as having dependencies.
 *             |     - End.
 *             -> If the downstream dependency is not being observed, check its dependency flag:
 *                 |- If set to true
 *                 |    - Flag the target as having dependencies.
 *                 |    - End.
 *                 -> If set to false:
 *                      - Set the target as having no dependencies.
 *                      - Loop through upstream dependencies.
 *                         |- If the upstream dependency is not observed
 *                         |    - Set its dependency flag to false.
 *                         -> If the upstream dependency is observed:
 *                              - Set sync pending flag to true.
 *                              - Inform the signInChecker sync queue needs processing.
 *
 * signInChecker:
 * - Check to see if one or more data models are being observed:
 *     - If not detach sign in observer
 *     - If so attach sign in observer
 *
 * - Is the user signed in?
 *     - If not, observe until user signs in.
 *     - If yes, is the update sync flag set to true?
 *          - If not, do nothing.
 *          - If yes:
 *              - Set the update sync flag to false
 *              - Loop through sync map:
 *                  - Is the current model being observed or is it a dependency for another model?
 *                  - If yes:
 *                      - Create an intent and send it to the SyncRemoteDataService
 *                  - If no:
 *                      - End.
 *
 * SignInObserver triggered:
 *      - Run the signInChecker.
 */
class SyncManager {

    private static final String TAG = "SyncManager";

    // TODO - Does SyncManager need to be network state aware? Probably...
    // What happens if the remote connection is lost for a while?
    // Do the Firebase listeners account for this situation?

    private MutableLiveData<String> userId;

    // Keeps track of the value of the user ID, which in turn tells us if the user is logged in.
    private Context context;

    // Keeps an ordered list of data models and their respective remote listener status.
    private List<DataModelStatus> modelSyncList = new LinkedList<>();

    // Keeps track of when a sync is required.
    private boolean syncPending = false;

    // Keeps an eye out for changes to the users login status.
    private Observer<String> userIdObserver = userId -> {

        if (userId != null) {
            // User is logged in, sync any data models in the sync map.
            if (!userId.equals(ANONYMOUS)) {
                performSync();
            }
        }
    };

    SyncManager(Context context) {

        this.context = context;

        userId = new MutableLiveData<>();
        userId = Constants.getUserId();

        // Add new data models to the synchronisation map in dependency order.
        modelSyncList.add(new DataModelStatus(Product.TAG, false, false));
        modelSyncList.add(new DataModelStatus(UsersProductData.TAG, false, false));
    }

    void setModelToSync(String model, boolean isObserved) {

        // Updates the targets state. Returns true if changed.
        boolean hasChanged = updateModelsSyncStatus(model, isObserved);

        // If the targets state has changed// :
        if (hasChanged) {
            // If the target is now being observed:
            if (isObserved) {
                // Turn on sync for all of the targets upstream inactive data models:
                syncAllUpstream(model);
            } else {
                // If the target is no longer being observed, check for downstream dependencies.
                if (!resolveDownStream(model)) {
                    // If it does not have downstream dependencies, are there any upstream dependencies that are not
                    // being observed?
                    resolveUpstream();
                }
            }
        }
    }

    private boolean updateModelsSyncStatus(String modelToUpdate, boolean newObservedState) {

        ListIterator<DataModelStatus> iterator = modelSyncList.listIterator();

        while (iterator.hasNext()) {

            DataModelStatus model = iterator.next();

            // Only update the target models status if it has changed.
            if (model.getName().equals(modelToUpdate) && model.observedState() != newObservedState) {
                model.setObservedState(newObservedState);
                iterator.set(model);
                return true;
            }
        }
        return false;
    }

    /**
     * Flags all upstream data models to sync before the given data model.
     * @param target the given data model.
     */
    private void syncAllUpstream(String target) {

        ListIterator<DataModelStatus> iterator = modelSyncList.listIterator();
        DataModelStatus dms;

        do {
            // Loop through upstream data models.
            dms = iterator.next();

            // If the upstream model is not observed and its 'has dependents' flag is set to false:
            if (!dms.observedState() && !dms.hasDependents()) {

                // Update its 'has dependents' flag to true.
                dms.setHasDependents(true);
                iterator.set(dms);
            }
        }
        // Keep looping until the target model has been processed.
        while (iterator.hasNext() && !dms.getName().equals(target));

        // Update sync pending to true and perform sync.
        syncPending = true;
        performSync();
    }

    /**
     * Clears out any upstream dependents that are no longer required.
     */
    private void resolveUpstream() {

        ListIterator<DataModelStatus> li = modelSyncList.listIterator(modelSyncList.size());
        DataModelStatus dms;

        // Loop backwards through dependencies from the downstream to the the target.
        while (li.hasPrevious()) {
            dms = li.previous();

            // If the upstream dependency is not being observed:
            if (!dms.observedState()) {
                // Set its dependency flag to false.
                dms.setHasDependents(false);

                // If the upstream dependency is being observed:
            } else {
                // Set sync pending flag to true.
                syncPending = true;
                // Inform the signInChecker sync queue needs processing.
                performSync();
                break;
            }
        }
    }

    /**
     * Works out if data model, which is no longer being observed, has downstream dependents.
     *
     * @return true if has dependents, false if not.
     */
    private boolean resolveDownStream(String target) {

        ListIterator<DataModelStatus> iterator = modelSyncList.listIterator();
        DataModelStatus dms;

        do {
            // Loop through the dependency tree:
            dms = iterator.next();
            // When the target is found:
            if (dms.getName().equals(target)) {
                // See if there is another downstream dependency in the list:
                if (iterator.hasNext()) {
                    // If there is get the downstream dependency.
                    dms = iterator.next();
                    // If it is being observed:
                    if (dms.observedState()) {

                        // Go back to the target:
                        dms = iterator.previous();
                        // And flag as it having dependencies.
                        dms.setHasDependents(true);
                        iterator.set(dms);

                        return true;

                    } else {
                        // If however the downstream dependency is not being observed see if the
                        // downstream dependency is flagged as having dependents:
                        if (dms.hasDependents()) {

                            // If it does, go back to the target.
                            dms = iterator.previous();
                            // And flag the target as having dependents.
                            dms.setHasDependents(true);
                            iterator.set(dms);
                            return true;

                        } else {
                            // If however the downstream dependency has no dependents go back to
                            // the target.
                            dms = iterator.previous();
                            // And set it to having no dependents
                            dms.setHasDependents(false);
                            iterator.set(dms);
                            return false;
                        }
                    }
                }
            }
        }

        while (iterator.hasNext() && !dms.getName().equals(target));
        return false;
    }

    private void performSync() {

        int totalObservedModels = 0;

        // Check to see if one or more data models are being observed.
        for (DataModelStatus dataModel : modelSyncList) {
            if(dataModel.activeState()) {
                totalObservedModels ++;
            }
        }
        // If observed data models, turn on the user ID observer.
        if (totalObservedModels > 0) {
            if (!userId.hasActiveObservers()) {
                userId.observeForever(userIdObserver);
            }

        // If no observed data models turn user ID observer off.
        } else {
            userId.removeObserver(userIdObserver);
        }

        // If the user is logged in.
        if (!userId.getValue().equals(ANONYMOUS)) {

            // And there is a sync pending.
            if (syncPending) {
                // Turn sync pending off.
                syncPending = false;

                // Start processing pending data models.
                for (DataModelStatus model : modelSyncList) {
                    syncModel(model.getName(), model.activeState());
                }
            }
        }
    }

    /**
     * Sends intents to the {@link SyncRemoteDataService} for synchronisation.
     * @param dataModel the data model to synchronise.
     * @param activeState  true turns synchronisation on, false off.
     */
    private void syncModel(String dataModel, boolean activeState) {

        Intent i = new Intent(context, SyncRemoteDataService.class);
        i.setAction(dataModel);
        i.putExtra("activeState", activeState);

        // Send the intent to be processed.
        context.startService(i);
    }

    /**
     * Used for storing and managing data model states.
     */
    class DataModelStatus {
        private boolean mIsObserved;
        private boolean mHasDependents;

        private String mModelName;

        private DataModelStatus(String mModelName, boolean mIsObserved, boolean mHasDependents) {
            this.mIsObserved = mIsObserved;
            this.mHasDependents = mHasDependents;
            this.mModelName = mModelName;
        }

        boolean observedState() {
            return mIsObserved;
        }

        void setObservedState(boolean mIsObserved) {
            this.mIsObserved = mIsObserved;
        }

        boolean hasDependents() {
            return mHasDependents;
        }

        void setHasDependents(boolean mHasDependents) {
            this.mHasDependents = mHasDependents;
        }

        String getName() {
            return mModelName;
        }

        boolean activeState() {
            return mIsObserved || mHasDependents;
        }
    }
}