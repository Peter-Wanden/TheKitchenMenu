package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;

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
 * The data set is sent to its respective SyncClass (syncProduct for example) for processingSyncQueue into
 * the local database.
 * Once processingSyncQueue is complete:
 * RepositoryRemote checks the next item in the queue:
 * If its flag is set to 'data set returned':
 * Repeats loop.
 * If however, the first item has its flag set to 'data set not returned':
 * Repository remote flags the VELs data model as 'data set returned' in the queue.
 */
public class RepositoryRemote {

    private static final String TAG = "RepositoryRemote";
    private static RepositoryRemote sInstance;

    private LinkedList<ModelStatus> syncQueue;
    private SyncProduct syncProduct;
    private SyncUserProductData syncUserProductData;
    private boolean processingSyncQueue;

    private HandlerWorker worker;

    // TODO - Does this need to be pulled into a different thread???
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // TODO - This is a mess, refactor and clean.

            String syncModelCompleted = (String) msg.obj;

            if (!syncModelCompleted.equals("Sync failed, 0 items returned.")) {
                Log.d(TAG, "tkm - handleMessage: " + syncModelCompleted + " has completed sync.");
                ModelStatus completedElement = syncQueue.remove();
                Log.d(TAG, "tkm - handleMessage: Removed: " + completedElement.getModelName() +
                        " from sync queue");

                // If available move on to next element.
                if (syncQueue.size() > 0) {
                    Log.d(TAG, "tkm - handleMessage: Moving on to next item.");
                    processSyncQueue();
                } else {
                    syncComplete("tkm - Sync completed normally, resetting processingSyncQueue to: false");
                }
            } else {
                syncQueue.clear();
                syncComplete("tkm - 0 items returned, cannot process sync any further.");
            }
            // TODO - Recycle message here?
        }
    };

    private void syncComplete(String syncResultMessage) {
        worker.quit();
        processingSyncQueue = false;
        Log.d(TAG, "tkm - syncComplete: Sync completed with message: " + syncResultMessage);
    }

    /**
     * @param context the Application context
     */
    private RepositoryRemote(Context context) {
        worker = new HandlerWorker(TAG);
        syncQueue = new LinkedList<>();
        syncProduct = new SyncProduct(context, this);
        syncUserProductData = new SyncUserProductData(context, this);
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
     * @param dataModel  the data model to synchronise.
     * @param isObserved true to turn listener on, false to turn off.
     */
    void isObserved(String dataModel, boolean isObserved) {

        if (!Constants.getUserId().getValue().equals(Constants.ANONYMOUS)) {
            switch (dataModel) {

                case ProductEntity.TAG:
                    if (syncProduct.getListenerState() != isObserved) {
                        syncProduct.setListenerState(isObserved);
                        if (isObserved) {
                            syncQueue.add(new ModelStatus(ProductEntity.TAG));
                        }
                    }
                    break;

                case UsedProductEntity.TAG:
                    if (syncUserProductData.getListenerState() != isObserved) {
                        syncUserProductData.setListenerState(isObserved);
                        if (isObserved) {
                            syncQueue.add(new ModelStatus(UsedProductEntity.TAG));
                        }
                    }
                    break;

                default:
                    Log.v(TAG, "isObserved: Data model unknown, cannot sync");
                    break;
            }
        }
    }

    // Triggered when a VEL returns a data set. Updates the sync queue and initiates processingSyncQueue.
    void dataSetReturned(ModelStatus synchronisedModel) {

        if (synchronisedModel.getNoOfItemsReturned() != 0) {

            int indexOfModelInQueue = 0;

            for (int indexOfModel = 0; indexOfModel < syncQueue.size(); indexOfModel++) {
                ModelStatus modelInQueue = syncQueue.get(indexOfModel);

                if (modelInQueue.getModelName().equals(synchronisedModel.getModelName())) {
                    indexOfModelInQueue = indexOfModel;
                    break;
                }
            }

            // Taken out, updated status and placed back into queue.
            syncQueue.remove(indexOfModelInQueue);
            syncQueue.add(indexOfModelInQueue, synchronisedModel);

            Log.d(TAG, "tkm - dataSetReturned: Sync queue looks like: " + syncQueue.toString());

            if (!processingSyncQueue) {
                processSyncQueue();
            }
        } else {
            Message m = Message.obtain();
            m.obj = "Sync failed, 0 items returned.";
            handler.sendMessage(m);
        }
    }

    private void processSyncQueue() {
        processingSyncQueue = true;
        ModelStatus queueHead = syncQueue.peek();

        if (queueHead != null) {
            // Check to see if the data model has had its data set returned.
            if (queueHead.dataSetReturned()) {

                // Find out which data models data set has been returned.
                switch (queueHead.getModelName()) {

                    case ProductEntity.TAG:
                        Log.d(TAG, "tkm - processSyncQueue: ProductEntity");
                        // Send the data to its respective sync class to be processed.
                        syncProduct.syncRemoteData(handler, worker);
                        break;

                    case UsedProductEntity.TAG:
                        Log.d(TAG, "tkm - processSyncQueue: UsedProductEntity");
                        // Send the data to its respective sync class to be processed.
                        syncUserProductData.syncRemoteData(handler, worker);
                        break;

                    default:
                        // If the data models data set has not returned, log error, exit.
                        Log.d(TAG, "tkm - processSyncQueue: Data model not found: " +
                                queueHead.toString() + " Exiting sync.");
                        processingSyncQueue = false;
                        break;
                }

            } else {
                Log.d(TAG, "tkm - processSyncQueue: First item in queue is not ready, exiting sync.");
                processingSyncQueue = false;
            }
        }
    }


}
