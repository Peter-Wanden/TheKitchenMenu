package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.databaseRemote.DataListenerPending;
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteDbRefs;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;

/**
 * Synchronises an incoming remote with a local data model. Giving preference to the remote item.
 * SEE: https://www.youtube.com/watch?v=998tPb10DFM&list=PL6nth5sRD25hVezlyqlBO9dafKMc5fAU2&index=6
 */
class SyncProdComm {

    private static final String TAG = "SyncProdComm";

    private Repository mRepository;
    private RepositoryRemote mRepositoryRemote;

    // For tracking the progress of this sync session.
    private int mResultCode = 0;

    // Manages the listeners connection to the DmProdComm remote data.
    private DataListenerPending mListenerPending;

    // For running database actions on a separate thread.
    private HandlerWorker mWorker;
    // Sends messages when sync is complete to Handler in RepositoryRemote.
    private Handler mHandler;
    private Message mMessage;

    // The incoming data set from the remote server.
    private Queue<DmProdComm> mRemoteData = new LinkedList<>();
    // A batch of database updates.
    private List<DmProdComm> mRemoteUpdates = new ArrayList<>();
    // A batch of database inserts.
    private List<DmProdComm> mRemoteInserts = new ArrayList<>();

    // Ensures all threads use the same instance of this field.
    private volatile DmProdComm[] mPcArray = new DmProdComm[2];

    /**
     * @param context the application context required for invoking the Repositories.
     */
    SyncProdComm(Context context, RepositoryRemote repositoryRemote) {
        mRepository = ((Singletons) context).getRepository();
        mRepositoryRemote = repositoryRemote;
        mMessage = Message.obtain();
        mMessage.obj = TAG;
    }

    /**
     * Starts sync for this classes data model
     * @param handler the handler used to send work and message updates to the worker.
     * @param worker thread used to process result sets into local database.
     */
    void syncRemoteData(Handler handler, HandlerWorker worker) {
        // RepositoryRemote's handler to post updates too.
        mHandler = handler;
        // RepositoryRemote's worker thread to perform database operations.
        mWorker = worker;
        // Start the sync to local database.
        syncPrep();
    }

    /**
     * Prepares the remote and local elements for comparison and synchronisation
     */
    private void syncPrep() {

        // Get the first element in the remote data queue
        mPcArray[0] = mRemoteData.peek();

        mWorker.execute(() -> {

            // If exists, get its local counterpart.
            mPcArray[1] = mRepository.getProdCommByRemoteId(
                    mPcArray[0].getFbProductReferenceKey());

            if (mPcArray[1] != null) {
                // If exists, add the local elements ID to the remote elements ID
                mPcArray[0].setId(mPcArray[1].getId());
                }
                compareAndSync();
            }
        );
    }

    /**
     * Compares the elements in the array, if they are different, stores or updates the local with
     * remote.
     */
    private void compareAndSync() {

        // If there is no locally matching element insert the remote.
        if (mPcArray[1] == null) {

            mRemoteInserts.add(mPcArray[0]);
            nextPlease();

        } else {

            // Compare the two, if they are not equal, update the local with the remote.
            if (!mPcArray[0].toString().equals(mPcArray[1].toString())) {

                mRemoteUpdates.add(mPcArray[0]);
                nextPlease();

            } else {
                // There is no change to make so move on to next element item
                nextPlease();
            }
        }
    }

    private void nextPlease() {
        // Element has been processed, so remove from remote queue.
        mRemoteData.remove();

        if (mRemoteData.size() > 0) {

            // Get the next element in the queue.
            syncPrep();

        } else {

            // Queue has been processed, save the inserts and updates to the database.
            mWorker.execute(() -> {

                if (mRemoteInserts.size() > 0) {

                    mRepository.insertAllProdComm(mRemoteInserts);
                    mRemoteInserts.clear();

                    // Work is complete, inform handler.
                    mResultCode += 1; // inserts complete.

                    } else {

                    mResultCode += 2; // no inserts.
                }
            }

            ).execute(() -> {
                if (mRemoteUpdates.size() > 0) {

                    mRepository.updateAllProdComm(mRemoteUpdates);
                    mRemoteUpdates.clear();

                    mResultCode += 3; // updates complete.

                } else {

                    mResultCode += 4; // no updates.
                }

            }).execute(() -> {
                // Send a message to let RepositoryRemote know sync is complete for this model.
                mMessage.arg1 = mResultCode;
                mHandler.sendMessage(mMessage);
            });
        }
    }

    // Sets up the listener for this class
    private void initialiseVel() {
        Log.d(TAG, "initialiseProdCommVel: for " + TAG + " called");

        // Database reference to the community products location in Firebase.
        DatabaseReference prodCommRef = RemoteDbRefs.getRefProdComm();

        // A Queue to store the returned data.
        Queue<DmProdComm> remoteData = new LinkedList<>();

        // Reports changes in the remote database.
        ValueEventListener prodCommVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    DmProdComm pc = shot.getValue(DmProdComm.class);

                    if (pc != null) {
                        // Add the remote reference key.
                        pc.setRemoteProdRefKey(shot.getKey());
                        remoteData.add(pc);
                    }
                }
                // Copies the remote data for processing.
                mRemoteData.addAll(remoteData);
                Log.d(TAG, "onDataChange: returned: " + mRemoteData.size() + " objects");
                // Clears down remote data queue.
                remoteData.clear();
                // Updates the data models status in the RemoteRepository.
                mRepositoryRemote.dataSetReturned(
                        new ModelStatus(DmProdComm.TAG, true));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Unable to update community products, with error: "
                        + databaseError);
            }
        };
        // Creates a listener to attach to this VEL.
        mListenerPending = new DataListenerPending(prodCommRef, prodCommVEL);
    }

    // Returns true if listener is active, false if null or inactive.
    boolean getListenerState() {
        if (mListenerPending == null) {
            initialiseVel();
        }
        Log.d(TAG, "getListenerState: " + mListenerPending.getListenerState());
        return mListenerPending.getListenerState();
    }

    // Sets the listeners state
    void setListenerState(boolean requestedState) {
        if (mListenerPending == null) {
            initialiseVel();
        }
        Log.d(TAG, "setListenerState: " + requestedState);
        mListenerPending.setListenerState(requestedState);
    }
}