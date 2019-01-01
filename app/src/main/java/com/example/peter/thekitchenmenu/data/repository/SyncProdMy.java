package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.databaseRemote.DataListenerPending;
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteDbRefs;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;
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
 * Synchronises an incoming remote with a local item. Giving preference to the remote item.
 * For more information on Looper, Handler and message queue see:
 * https://www.youtube.com/watch?v=998tPb10DFM&list=PL6nth5sRD25hVezlyqlBO9dafKMc5fAU2&index=6
 */
class SyncProdMy {

    private static final String TAG = "SyncProdMy";

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
    private Queue<DmProdMy> mRemoteData = new LinkedList<>();

    // A batch of database updates.
    private List<DmProdMy> mRemoteUpdates = new ArrayList<>();
    // A batch of database inserts.
    private List<DmProdMy> mRemoteInserts = new ArrayList<>();

    // Ensures all threads use the same instance of this field.
    private volatile DmProdMy[] mPmArray = new DmProdMy[2];

    /**
     * @param context the application context required for invoking the Repository.
     */
    SyncProdMy(Context context, RepositoryRemote repositoryRemote) {
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
        syncPrep();
    }

    /**
     * Prepares the remote and local elements for comparison and synchronisation
     */
    private void syncPrep() {

        // Get the first DmProdMy item in the remote data queue
        mPmArray[0] = mRemoteData.peek();

        mWorker.execute(() -> {
            // If exists, get its local counterpart.
            mPmArray[1] = mRepository.getProdMyByRemoteId(
                    mPmArray[0].getFbProductReferenceKey());

            if (mPmArray[1] != null) {
                // If exists, add the local elements ID to the remote elements ID
                mPmArray[0].setId(mPmArray[1].getId());
            }

        }).execute(() -> {
            // Get the local related DmProdComm using the remote reference ID
            DmProdComm pc = mRepository.getProdCommByRemoteId(
                    mPmArray[0].getFbProductReferenceKey());

            // Add the DmProdComm local ID to the remote DmProdMy
            mPmArray[0].setCommunityProductId(pc.getId());

            compareAndSync();
        });
    }

    /**
     * Compares the elements in the array, if they are different, stores or updates the local with
     * remote.
     */
    private void compareAndSync() {

        // If there is no locally matching DmProdMy insert the remote.
        if (mPmArray[1] == null) {

            mRemoteInserts.add(mPmArray[0]);
            nextPlease();

        } else {
            // Compare the two. If they are different, update the local with the remote.
            if (!mPmArray[0].toString().equals(mPmArray[1].toString())) {

                mRemoteUpdates.add(mPmArray[0]);
                nextPlease();

            } else {
                // There is no change to make, so move on to next item
                nextPlease();
            }
        }
    }

    private void nextPlease() {
        // Element has been processed, so remove from queue.
        mRemoteData.remove();

        if (mRemoteData.size() > 0) {
            // Get the next element in the queue.
            syncPrep();

        } else {

            // Queue has been processed, save the inserts and updates to the database.

            Log.d(TAG, "nextPlease: Saving data");
            mWorker.execute(() -> {

                Log.d(TAG, "nextPlease: Saving: " + mRemoteInserts.size() + " inserts to database.");

                if (mRemoteInserts.size() > 0) {
                    mRepository.insertAllProdMy(mRemoteInserts);
                    mRemoteInserts.clear();

                    // Work is complete, inform handler.
                    mResultCode += 1; // inserts complete.

                } else {

                    mResultCode += 2; // no inserts.
                }
            }

            ).execute(() -> {
                Log.d(TAG, "nextPlease: Saving:" + mRemoteUpdates.size() + " updates to database.");
                if(mRemoteUpdates.size() > 0) {

                    mRepository.updateAllProdMy(mRemoteUpdates);
                    mRemoteUpdates.clear();

                    mResultCode += 3; // updates complete.

                } else {

                    mResultCode += 4;
                }

            }).execute(() -> {
                // Send a message to let RepositoryRemote know sync is complete for this model.
                mMessage.arg1 = mResultCode;
                mHandler.sendMessage(mMessage);
            });
        }
    }

    /**
     * Sets up the remote listener for this class
     */
    private void initialiseVel() {
        Log.d(TAG, "initialiseVel: called");

        DatabaseReference prodMyRef = RemoteDbRefs.getRefProdMy(Constants.getUserId().getValue());

        Queue<DmProdMy> remoteData = new LinkedList<>();

        ValueEventListener prodMyVel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    DmProdMy pm = shot.getValue(DmProdMy.class);

                    if (pm != null) {
                        remoteData.add(pm);
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
                Log.e(TAG, "Unable to update MyProducts, with error: " + databaseError);
            }
        };
        mListenerPending = new DataListenerPending(prodMyRef, prodMyVel);
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
