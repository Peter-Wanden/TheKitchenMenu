package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.source.remote.DataListenerPending;
import com.example.peter.thekitchenmenu.data.source.remote.RemoteDbRefs;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
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
 * https://www.youtube.com/watch?v=998tPb10DFM&list=PL6nth5sRD25hVezlyqlBO9dafKMc5fAU2&index=6
 */
class SyncUserProductData {

    private static final String TAG = "SyncUserProductData";

    private Repository repository;
    private RepositoryRemote repositoryRemote;

    private DataListenerPending listenerPending;

    private HandlerWorker worker;
    private Handler handler;
    private Message resultMessage;
    private int resultCode = 0;

    private Queue<ProductUserDataEntity> remoteData = new LinkedList<>();
    private List<ProductUserDataEntity> batchUpdates = new ArrayList<>();
    private List<ProductUserDataEntity> batchInserts = new ArrayList<>();

    private volatile ProductUserDataEntity[] mPmArray = new ProductUserDataEntity[2];

    SyncUserProductData(Context context, RepositoryRemote repositoryRemote) {
        repository = ((Singletons) context).getRepository();
        this.repositoryRemote = repositoryRemote;
        resultMessage = Message.obtain();
        resultMessage.obj = TAG;
    }

    void syncRemoteData(Handler handler, HandlerWorker worker) {
        this.handler = handler;
        this.worker = worker;
//        findLocalCopy();
    }

//    private void findLocalCopy() {
//
//        // Load remote product_uneditable.
//        mPmArray[0] = remoteData.peek();
//
//        worker.execute(() -> {
//            // If exists, get its local counterpart.
//            mPmArray[1] = repository.getUserProductDataByRemoteId(
//                    mPmArray[0].getRemoteProductId());
//
//            if (mPmArray[1] != null) {
//                // If exists, add the local elements ID to the remote elements ID
//                mPmArray[0].setId(mPmArray[1].getId());
//            }
//
//        }).execute(() -> {
//            // Get the local related ProductEntity using the remote reference ID
//            ProductEntity pc = repository.getProductByRemoteId(
//                    mPmArray[0].getRemoteProductId());
//
//            // Add the ProductEntity local ID to the remote ProductUserDataEntity
//            mPmArray[0].setProductId(pc.getId());
//
//            compareLocalWithRemote();
//        });
//    }

    private void compareLocalWithRemote() {

        // If there is no locally matching ProductUserDataEntity insert the remote.
        if (mPmArray[1] == null) {

            batchInserts.add(mPmArray[0]);
            nextPlease();

        } else {
            // Compare the two. If they are different, add to updates.
            if (!mPmArray[0].toString().equals(mPmArray[1].toString())) {

                batchUpdates.add(mPmArray[0]);
                nextPlease();

            } else {
                // There is no change to make, so move on to next item
                nextPlease();
            }
        }
    }

    private void nextPlease() {
        // Element has been processed, so remove from queue.
        remoteData.remove();

        if (remoteData.size() > 0) {
            // Get the next element in the queue.
//            findLocalCopy();

        } else {

            // Queue has been processed, save the inserts and updates to the database.

            Log.d(TAG, "tkm - nextPlease: Saving data");
            worker.execute(() -> {

                Log.d(TAG, "tkm - nextPlease: Saving: " + batchInserts.size() + " inserts to database.");

                if (batchInserts.size() > 0) {
                    repository.insertAllUserProductData(batchInserts);
                    batchInserts.clear();

                    // Work is complete, inform handler.
                    resultCode += 1; // inserts complete.

                } else {

                    resultCode += 2; // no inserts.
                }
            }

            ).execute(() -> {
                Log.d(TAG, "tkm - nextPlease: Saving:" + batchUpdates.size() + " updates to database.");
                if(batchUpdates.size() > 0) {

                    repository.updateUsersProductData(batchUpdates);
                    batchUpdates.clear();

                    resultCode += 3; // updates complete.

                } else {

                    resultCode += 4;
                }

            }).execute(() -> {
                // Send a message to let RepositoryRemote know sync is complete for this model.
                resultMessage.arg1 = resultCode;
                handler.sendMessage(resultMessage);
            });
        }
    }

    // Sets up the remote listener for this class.
    private void initialiseVel() {
        Log.d(TAG, "tkm - initialiseVel: called");

        DatabaseReference prodMyRef = RemoteDbRefs.getUserProductData(Constants.getUserId().getValue());

        Queue<ProductUserDataEntity> remoteSnapShot = new LinkedList<>();

        ValueEventListener prodMyVel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    ProductUserDataEntity pm = shot.getValue(ProductUserDataEntity.class);

                    if (pm != null) {
                        remoteSnapShot.add(pm);
                    }
                }
                // Copies the remote data for processing.
                remoteData.addAll(remoteSnapShot);
                Log.d(TAG, "tkm - onDataChange: returned: " + SyncUserProductData.this.remoteData.size() + " objects");
                // Clears down remote data queue.
                remoteSnapShot.clear();
                // Updates the data models status in the RemoteRepository.
                repositoryRemote.dataSetReturned(new ModelStatus(
                        ProductUserDataEntity.TAG, true, remoteData.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "tkm - Unable to update MyProducts, with error: " + databaseError);
            }
        };
        listenerPending = new DataListenerPending(prodMyRef, prodMyVel);
    }

    // Returns true if listener is activeState, false if null or inactive.
    boolean getListenerState() {
        if (listenerPending == null) {
            initialiseVel();
        }
        Log.d(TAG, "tkm - getListenerIsAttached: " + listenerPending.getListenerIsAttached());
        return listenerPending.getListenerIsAttached();
    }

    // Sets the listeners state
    void setListenerState(boolean requestedState) {
        if (listenerPending == null) {
            initialiseVel();
        }
        Log.d(TAG, "tkm - setListenerIsAttached: " + requestedState);
        listenerPending.setListenerIsAttached(requestedState);
    }
}
