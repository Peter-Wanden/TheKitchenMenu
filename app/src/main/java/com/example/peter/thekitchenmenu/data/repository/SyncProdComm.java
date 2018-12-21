package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Synchronises an incoming remote with a local data model. Giving preference to the remote item.
 * SEE: https://www.youtube.com/watch?v=998tPb10DFM&list=PL6nth5sRD25hVezlyqlBO9dafKMc5fAU2&index=6
 */
class SyncProdComm {

    // TODO - Send inserts and updates in batches!

    private static final String TAG = "SyncProdComm";

    private Repository mRepository;
    private HandlerWorker mWorker;
    private Queue<DmProdComm> mRemoteData = new LinkedList<>();
    private volatile DmProdComm[] mPcArray = new DmProdComm[2];

    /**
     * @param context the application context required for invoking the Repository.
     */
    SyncProdComm(Context context) {
        mRepository = ((Singletons) context).getRepository();
        mWorker = new HandlerWorker(TAG);
    }

    /**
     * Finds and matches a remote product with (if available) with a local product.
     * Position 0 contains the remote product. Position 1 the local product.
     */
    void syncRemoteData(Queue<DmProdComm> remoteData) {

        if (remoteData != null) {
            Log.d(TAG, "syncRemoteData: remote data set size is: " + remoteData.size());

            for (DmProdComm dMpc : remoteData) {
                Log.d(TAG, "syncRemoteData: " + dMpc.getDescription());
            }

            mRemoteData.addAll(remoteData);
            syncPrep();
        }
    }

    private void syncPrep() {
        // Get the first DmProdComm item in the remote data queue
        mPcArray[0] = mRemoteData.peek();

        mWorker.execute(() -> {
            // If exists, get its local counterpart by searching for the remote ID
            mPcArray[1] = mRepository.getProdCommByRemoteId(
                    mPcArray[0].getFbProductReferenceKey());

            if (mPcArray[1] != null) {
                // If exists, add the local DmProdComm ID to the remote DmProdComm ID
                mPcArray[0].setId(mPcArray[1].getId());
            }

            compareAndSync();
            }
        );
    }

    /**
     * Compares the products in the array, if they are different, stores or updates the local with
     * remote.
     */
    private void compareAndSync() {

        // If there is no locally matching DmProdComm insert the remote
        if (mPcArray[1] == null) {

            mWorker.execute(() -> {
                mRepository.insertProdComm(mPcArray[0]);
                nextPlease();
            });

        } else {

            // Compare the two, if they are not equal, update the local with the remote.
            if (!mPcArray[0].toString().equals(mPcArray[1].toString())) {

                mWorker.execute(() -> {
                    mRepository.updateProdComm(mPcArray[0]);
                    nextPlease();
                });

            } else {
                // There is no change to make so move on to next product item
                nextPlease();
            }
        }
    }

    private void nextPlease() {
        // Item has been processed, so remove from queue.
        mRemoteData.remove();
        if (mRemoteData.size() > 0) {
            // Get the next item in the queue.
            syncPrep();
        } else {
            // Sync is complete, shut down the thread.
            mWorker.quit();
        }
    }
}
