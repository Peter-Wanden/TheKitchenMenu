package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;

import java.util.LinkedList;

/**
 * RepositoryRemotes responsibility is to synchronise data models in the order in which they are
 * received. It achieves this by placing each model in a LinkedHashMap which preserves the order,
 * turning the models remote listeners on or off, then listening for the returned data sets. When
 * received, processes them in the order as laid out in the sync queue.
 * <p>
 * Pseudocode:
 * <p>
 * RepositoryRemote receives a model to turn remote sync on or off:
 * <p>
 * On receipt:
 * Turns the VEL on or off as appropriate.
 * If the VEL turns a data model on:
 * It places the model into the 'sync queue' and flags it as 'data set not returned'.
 * <p>
 * When the VEL returns a data set:
 * RepositoryRemote checks the first item in the sync queue:
 * If the first item has its flag set to 'data set returned':
 * The data set is sent to its respective SyncClass (syncProdComm for example) for processing into
 * the local database.
 * Once processing is complete:
 * RepositoryRemote checks the next item in the queue:
 * If its flag is set to 'data set returned':
 * Repeats loop.
 * If however, the first item has its flag set to 'data set not returned':
 * Repository remote flags the VELs data model as 'data set returned' in the queue.
 */
public class RepositoryRemote {

    private static final String TAG = "RepositoryRemote";

    private static RepositoryRemote sInstance;

    private LinkedList<ModelStatus> mSyncQueue;
    private SyncProdComm mSyncProdComm;
    private SyncProdMy mSyncProdMy;
    private boolean isProcessing;

    // For running database actions on a separate thread.
    private HandlerWorker mWorker;

    // TODO - Does this need to be pulled into a different thread???
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // Completion codes returned in msg.arg1.
            // Inserts = 1, no inserts = 2, updates = 3, no updates = 4.
            // eg, inserts and updates = 4, no inserts and no updates = 6, etc.

            // Once the data models data set has been processed remove it from the sync queue.
            mSyncQueue.remove();

            Log.d(TAG, "handleMessage: Sync " + msg.obj +
                    " complete, with code: " + msg.arg1 + "\n");

            Log.d(TAG, "handleMessage: Removing " + msg.obj +
                    ". Queue size is now: " + mSyncQueue.size());

            // If available move on to next element.
            if (mSyncQueue.size() > 0) {
                Log.d(TAG, "handleMessage: Moving on to next item.");
                processSyncQueue();

            } else {
                // Sync queue empty, sync complete.
                // Quit thread.
                mWorker.quit();
                // Reset is processing to false
                Log.d(TAG, "handleMessage: Sync complete, resetting processing to: false");
                isProcessing = false;
            }

            // TODO - Recycle message here.
            // TODO - Dont forget to shut down the thread when done.
        }
    };

    /**
     * @param context the Application context
     */
    private RepositoryRemote(Context context) {
        mWorker = new HandlerWorker(TAG);
        mSyncQueue = new LinkedList<>();
        mSyncProdComm = new SyncProdComm(context, this);
        mSyncProdMy = new SyncProdMy(context, this);
    }

    public static RepositoryRemote getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (RepositoryRemote.class) {
                if (sInstance == null) {
                    sInstance = new RepositoryRemote(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * Triggered each time the repository reports a change in the given data models observed status.
     * Turns remote sync on or off in response. If the model is being turned on adds it to the sync
     * queue awaiting a response from the server. Data models can be listed in any order.
     *
     * @param dataModel     the data model to synchronise.
     * @param observedState true to turn listener on, false to turn off.
     */
    void isObserved(String dataModel, boolean observedState) {

        // Is the user logged in?
        if (!Constants.getUserId().getValue().equals(Constants.ANONYMOUS)) {

            switch (dataModel) {

                // Is the data model a ProdComm?
                case DmProdComm.TAG:

                    // Is the requested listener state different from its current state?
                    if (mSyncProdComm.getListenerState() != observedState) {
                        // Then change the listeners state to the requested state.
                        mSyncProdComm.setListenerState(observedState);
                        // If the requested state is to turn the listener on:
                        if (observedState) {
                            // Add the model to the sync queue, with 'data set received' to false.
                            mSyncQueue.add(new ModelStatus(DmProdComm.TAG, false));
                        }
                    }
                    break;

                // Is the data model a ProdMy?
                case DmProdMy.TAG:

                    if (mSyncProdMy.getListenerState() != observedState) {

                        mSyncProdMy.setListenerState(observedState);

                        if (observedState) {
                            mSyncQueue.add(new ModelStatus(DmProdMy.TAG, false));
                        }
                    }
                    break;

                // Unknown data model.
                default:
                    Log.v(TAG, "isObserved: Data model unknown, cannot sync");
                    break;
            }
        }
    }

    // Triggered when a VEL returns a data set. Updates the sync queue and initiates processing.
    void dataSetReturned(ModelStatus modelStatus) {
        Log.d(TAG, "dataSetReturned: sync queue looks like: " + mSyncQueue);
        int count = 0;

        for (int i = 0; i < mSyncQueue.size(); i++) {
            ModelStatus ms = mSyncQueue.get(i);
            Log.d(TAG, "dataSetReturned: i = " + i);
            Log.d(TAG, "dataSetReturned: in loop, model is: " + ms.getModelName());
            if (ms.getModelName().equals(modelStatus.getModelName())) {
                count = i;
                break;
            }
        }
        mSyncQueue.remove(count);
        mSyncQueue.add(count, modelStatus);

        Log.d(TAG, "dataSetReturned: Sync queue now looks like: " + mSyncQueue.toString());
        processSyncQueue();
    }

    // Triggered by a data set being returned from a VEL, or after a data set has been processed
    // see mHandler.
    private void processSyncQueue() {

        // Check to see if the sync queue is currently being processed.
        if (!isProcessing) {
            // If not, start processing.
            isProcessing = true;
        }

        // Get the data model identifier at the queue head.
        ModelStatus queueHead = mSyncQueue.peek();

        if (queueHead != null) {

            // Check to see if the data model has had its data set returned.
            if (queueHead.dataSetReturned()) {

                // Find out which data models data set has been returned.
                switch (queueHead.getModelName()) {

                    case DmProdComm.TAG:

                        Log.d(TAG, "processSyncQueue: DmProdComm");
                        // Send the data to its respective sync class to be processed.
                        mSyncProdComm.syncRemoteData(mHandler, mWorker);

                        break;

                    case DmProdMy.TAG:

                        Log.d(TAG, "processSyncQueue: DmProdMy");
                        // Send the data to its respective sync class to be processed.
                        mSyncProdMy.syncRemoteData(mHandler, mWorker);

                        break;

                    default:
                        // If the data models data set has not returned, log error, exit.
                        Log.d(TAG, "processSyncQueue: Data model not found: " +
                                queueHead.toString() + " Exiting sync.");
                        isProcessing = false;
                        break;
                }

            } else {
                Log.d(TAG, "processSyncQueue: First item in queue is not ready, exiting sync.");
                isProcessing = false;
            }
        }
    }
}
