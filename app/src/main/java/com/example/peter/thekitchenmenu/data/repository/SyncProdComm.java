package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.databaseRemote.DataListenerPending;
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteDbRefs;
import com.example.peter.thekitchenmenu.data.entity.Product;
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
class SyncProdComm {

    private static final String TAG = "SyncProdComm";

    private Repository repository;
    private RepositoryRemote repositoryRemote;

    private DataListenerPending listenerPending;

    private HandlerWorker worker;
    private Handler handler;
    private Message resultMessage;

    private Queue<Product> remoteData = new LinkedList<>();
    private List<Product> batchUpdates = new ArrayList<>();
    private List<Product> batchInserts = new ArrayList<>();

    private Product remoteProduct;
    private Product localProduct;

    SyncProdComm(Context context, RepositoryRemote repositoryRemote) {
        repository = ((Singletons) context).getRepository();
        this.repositoryRemote = repositoryRemote;
        resultMessage = Message.obtain();
        resultMessage.obj = TAG;
    }

    void syncRemoteData(Handler handler, HandlerWorker worker) {
        this.handler = handler;
        this.worker = worker;
        matchWithLocalProduct();
    }

    private void matchWithLocalProduct() {
        remoteProduct = null;
        localProduct = null;

        remoteProduct = remoteData.peek();

        worker.execute(() -> {
            // If exists, load local counterpart.
            localProduct = repository.getProdCommByRemoteId(remoteProduct.getRemoteProductId());

            if (localProduct != null) {
                // If exists, add the local elements ID to the remote elements ID
                remoteProduct.setId(localProduct.getId());
                }
                compareLocalWithRemote();
            }
        );
    }

    private void compareLocalWithRemote() {

        // If there is no locally matching element insert the remote.
        if (localProduct == null) {
            batchInserts.add(remoteProduct);
            moveOnToNextElement();

        } else {

            // Compare the two, if they are not equal, add to updates.
            if (!remoteProduct.toString().equals(localProduct.toString())) {
                batchUpdates.add(remoteProduct);
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
            matchWithLocalProduct();

        } else {

            worker.execute(() -> {

                if (batchInserts.size() > 0) {
                    repository.insertAllProdComm(batchInserts);
                    batchInserts.clear();
                }
            }

            ).execute(() -> {
                if (batchUpdates.size() > 0) {
                    repository.updateAllProdComm(batchUpdates);
                    batchUpdates.clear();
                }

            }).execute(() -> {
                // Send a resultMessage to let RepositoryRemote know sync is complete for this model.
                handler.sendMessage(resultMessage);
            });
        }
    }

    // Sets up the listener for this class
    private void initialiseVel() {
        Log.d(TAG, "initialiseVel: called");

        // Database reference to the community products location in Firebase.
        DatabaseReference prodCommRef = RemoteDbRefs.getRefProdComm();

        // A Queue to store the returned data.
        Queue<Product> remoteSnapShot = new LinkedList<>();

        // Reports changes in the remote database.
        ValueEventListener prodCommVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    Product pc = shot.getValue(Product.class);

                    if (pc != null) {
                        // Add the remote reference key.
                        pc.setRemoteProdRefKey(shot.getKey());
                        remoteSnapShot.add(pc);
                    }
                }
                // Copies the remote data for processing.
                remoteData.addAll(remoteSnapShot);
                Log.d(TAG, "onDataChange: returned: " + SyncProdComm.this.remoteData.size() + " objects");
                // Clears down remote data queue.
                remoteSnapShot.clear();
                // Updates the data models status in the RemoteRepository.
                repositoryRemote.dataSetReturned(
                        new ModelStatus(Product.TAG, true));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Unable to update community products, with error: "
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
        Log.d(TAG, "getListenerState: " + listenerPending.getListenerState());
        return listenerPending.getListenerState();
    }

    // Sets the listeners state
    void setListenerState(boolean requestedState) {
        if (listenerPending == null) {
            initialiseVel();
        }
        Log.d(TAG, "setListenerState: " + requestedState);
        listenerPending.setListenerState(requestedState);
    }
}