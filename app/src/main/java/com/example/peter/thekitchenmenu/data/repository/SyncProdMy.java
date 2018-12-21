package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Message;

import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;

import java.util.LinkedList;
import java.util.Queue;


/**
 * Synchronises an incoming remote with a local item. Giving preference to the remote item.
 * For more information on Looper, Handler and message queue see:
 * https://www.youtube.com/watch?v=998tPb10DFM&list=PL6nth5sRD25hVezlyqlBO9dafKMc5fAU2&index=6
 */
class SyncProdMy {

    private static final String TAG = "SyncProdMy";
    private Repository mRepository;
    private HandlerWorker mWorker;
    private Queue<DmProdMy> mRemoteData = new LinkedList<>();
    // Ensures all threads use the same instance of this field.
    private volatile DmProdMy[] mPmArray = new DmProdMy[2];

    SyncProdMy(Context context) {
        mRepository = ((Singletons) context).getRepository();
        mWorker = new HandlerWorker(TAG);
    }

    void syncRemoteData(Queue<DmProdMy> remoteData) {
        if (remoteData != null) {
            mRemoteData.addAll(remoteData);
            syncPrep();
        }
    }

    /**
     * Prepares the remote and local objects for comparison and synchronisation
     */
    private void syncPrep() {

        // Get the first DmProdMy item in the remote data queue
        mPmArray[0] = mRemoteData.peek();

        mWorker.execute(() -> {
            // If exists, get its local DmProdMy counterpart
            mPmArray[1] = mRepository.getProdMyByRemoteId(
                    mPmArray[0].getFbProductReferenceKey());

            if (mPmArray[1] != null) {
                // If exists, add the local DmProdMy ID to the remote DmProdMy ID
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

    private void compareAndSync() {
        // If there is no locally matching DmProdMy insert the remote.
        if (mPmArray[1] == null) {

            mWorker.execute(() -> {
                mRepository.insertProdMy(mPmArray[0]);

                nextPlease();
            });

        } else {
            // Compare the two. If they are different, update the local with the remote.
            if (!mPmArray[0].toString().equals(mPmArray[1].toString())) {

                mWorker.execute(() -> {
                    mRepository.updateProdMy(mPmArray[0]);

                    nextPlease();
                });

            } else {
                // There is no change to make, so move on to next item
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
            // Sync is complete, shut down the thread, inform the handler.
            Message m = Message.obtain();
            m.obj = TAG;
            mWorker.quit();
        }
    }
}
