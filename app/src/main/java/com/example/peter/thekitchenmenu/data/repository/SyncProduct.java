package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.source.remote.DataListenerPending;
import com.example.peter.thekitchenmenu.data.source.remote.RemoteDbRefs;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
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
 * https://www.youtube.com/watch?v=998tPb10DFM&list=PL6nth5sRD25hVezlyqlBO9dafKMc5fAU2&index=6
 */
class SyncProduct {

    private static final String TAG = "SyncProduct";

    private Repository repository;
    private RepositoryRemote repositoryRemote;

    private DataListenerPending listenerPending;

    private HandlerWorker worker;
    private Handler handler;
    private Message resultMessage;

    private Queue<ProductEntity> remoteData = new LinkedList<>();
    private List<ProductEntity> batchUpdates = new ArrayList<>();
    private List<ProductEntity> batchInserts = new ArrayList<>();

    private ProductEntity remoteProductEntity;
    private ProductEntity localProductEntity;

    SyncProduct(Context context, RepositoryRemote repositoryRemote) {
        repository = ((Singletons) context).getRepository();
        this.repositoryRemote = repositoryRemote;
        resultMessage = Message.obtain();
        resultMessage.obj = TAG;
    }

    void syncRemoteData(Handler handler, HandlerWorker worker) {
        this.handler = handler;
        this.worker = worker;
//        matchWithLocalProduct();
    }

//    private void matchWithLocalProduct() {
//        remoteProductEntity = null;
//        localProductEntity = null;
//
//        remoteProductEntity = remoteData.peek();
//        Log.d(TAG, "tkm - matchWithLocalProduct: " + remoteProductEntity.toString());
//
//        worker.execute(() -> {
//            // If exists, load local counterpart.
//            localProductEntity = repository.getProductByRemoteId(remoteProductEntity.getRemoteProductId());
//
//            if (localProductEntity != null) {
//                // If exists, add the local elements ID to the remote elements ID
//                remoteProductEntity.setId(localProductEntity.getId());
//                }
//                compareLocalWithRemote();
//            }
//        );
//    }

    private void compareLocalWithRemote() {

        // If there is no locally matching element insert the remote.
        if (localProductEntity == null) {
            batchInserts.add(remoteProductEntity);
            moveOnToNextElement();

        } else {

            // Compare the two, if they are not equal, add to updates.
            if (!remoteProductEntity.toString().equals(localProductEntity.toString())) {
                batchUpdates.add(remoteProductEntity);
                moveOnToNextElement();

            } else {
                moveOnToNextElement();
            }
        }
    }

    private void moveOnToNextElement() {
        // Element has been processed, so remove from remote queue.
        remoteData.remove();

        if (remoteData.size() > 0) {

            // Get the next element in the queue.
//            matchWithLocalProduct();

        } else {

            worker.execute(() -> {

                if (batchInserts.size() > 0) {
//                    repository.insertAllProducts(batchInserts);
                    batchInserts.clear();
                }
            }

            ).execute(() -> {
                if (batchUpdates.size() > 0) {
//                    repository.updateProducts(batchUpdates);
                    batchUpdates.clear();
                }

            }).execute(() -> {
                // Send a resultMessage to let RepositoryRemote know sync is complete for this model.
                handler.sendMessage(resultMessage);
            });
        }
    }

    // Sets up the listener for this class.
    private void initialiseVel() {
        Log.d(TAG, "tkm - initialiseVel: called");

        // Database reference to the community products location in Firebase.
        DatabaseReference prodCommRef = RemoteDbRefs.getRemoteProductData();

        // A Queue to store the returned data.
        Queue<ProductEntity> remoteSnapShot = new LinkedList<>();

        // Reports changes in the remote database.
        ValueEventListener prodCommVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    ProductEntity productEntity = shot.getValue(ProductEntity.class);

                    if (productEntity != null) {
                        // Add the remote reference key.
//                        productEntity.setRemoteProductId(shot.getKey());
                        remoteSnapShot.add(productEntity);
                    }
                }
                // Copies the remote data for processing.
                remoteData.addAll(remoteSnapShot);
                Log.d(TAG, "tkm - onDataChange: returned: " + SyncProduct.this.remoteData.size() + " objects");
                // Clears down remote data queue.
                remoteSnapShot.clear();
                // Updates the data models status in the RemoteRepository.
                repositoryRemote.dataSetReturned(new ModelStatus(
                        ProductEntity.TAG, true, remoteData.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "tkm - Unable to update community products, with error: "
                        + databaseError);
            }
        };
        // Creates a listener to attach to this VEL.
        listenerPending = new DataListenerPending(prodCommRef, prodCommVEL);
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