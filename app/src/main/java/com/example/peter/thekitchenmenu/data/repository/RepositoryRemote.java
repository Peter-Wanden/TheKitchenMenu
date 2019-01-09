package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.HandlerWorker;
import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.entity.UsersProductData;

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
 * The data set is sent to its respective SyncClass (syncProdComm for example) for processingSyncQueue into
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
    private SyncProdComm syncProdComm;
    private SyncProdMy syncProdMy;
    private boolean processingSyncQueue;

    private HandlerWorker worker;

    // TODO - Does this need to be pulled into a different thread???
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String syncModelCompleted = (String) msg.obj;
            Log.d(TAG, "handleMessage: " + syncModelCompleted + " has completed sync.");
            ModelStatus completedElement = syncQueue.remove();
            Log.d(TAG, "handleMessage: Removing: " + completedElement.getModelName() +
                    " from queue");

            // If available move on to next element.
            if (syncQueue.size() > 0) {
                Log.d(TAG, "handleMessage: Moving on to next item.");
                processSyncQueue();

            } else {
                // Sync complete.
                worker.quit();
                Log.d(TAG, "handleMessage: Sync complete, reset processingSyncQueue to: false");
                processingSyncQueue = false;
            }

            // TODO - Recycle message here?
            // TODO - Dont forget to shut down the thread when done.
        }
    };

    /**
     * @param context the Application context
     */
    private RepositoryRemote(Context context) {
        worker = new HandlerWorker(TAG);
        syncQueue = new LinkedList<>();
        syncProdComm = new SyncProdComm(context, this);
        syncProdMy = new SyncProdMy(context, this);
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
                case Product.TAG:

                    // Is the requested listener state different from its current state?
                    if (syncProdComm.getListenerState() != observedState) {
                        // Then change the listeners state to the requested state.
                        syncProdComm.setListenerState(observedState);
                        // If the requested state is to turn the listener on:
                        if (observedState) {
                            // Add the model to the sync queue, with 'data set received' to false.
                            syncQueue.add(new ModelStatus(Product.TAG, false));
                        }
                    }
                    break;

                case UsersProductData.TAG:

                    if (syncProdMy.getListenerState() != observedState) {

                        syncProdMy.setListenerState(observedState);

                        if (observedState) {
                            syncQueue.add(new ModelStatus(UsersProductData.TAG, false));
                        }
                    }
                    break;

                default:
                    Log.v(TAG, "observedState: Data model unknown, cannot sync");
                    break;
            }
        }
    }

    // Triggered when a VEL returns a data set. Updates the sync queue and initiates processingSyncQueue.
    void dataSetReturned(ModelStatus model) {
        int count = 0;

        for (int i = 0; i < syncQueue.size(); i++) {
            ModelStatus ms = syncQueue.get(i);
            if (ms.getModelName().equals(model.getModelName())) {
                count = i;
                break;
            }
        }

        syncQueue.remove(count);
        syncQueue.add(count, model);

        Log.d(TAG, "dataSetReturned: Sync queue looks like: " + syncQueue.toString());

        if (!processingSyncQueue) {
            processSyncQueue();
        }
    }

    // Triggered by a data set being returned from a VEL, or after a data set has been processed
    // see handler.
    private void processSyncQueue() {

        processingSyncQueue = true;

        // Get the data model identifier at the queue head.
        ModelStatus queueHead = syncQueue.peek();

        if (queueHead != null) {

            // Check to see if the data model has had its data set returned.
            if (queueHead.dataSetReturned()) {

                // Find out which data models data set has been returned.
                switch (queueHead.getModelName()) {

                    case Product.TAG:

                        Log.d(TAG, "processSyncQueue: Product");
                        // Send the data to its respective sync class to be processed.
                        syncProdComm.syncRemoteData(handler, worker);

                        break;

                    case UsersProductData.TAG:

                        Log.d(TAG, "processSyncQueue: UsersProductData");
                        // Send the data to its respective sync class to be processed.
                        syncProdMy.syncRemoteData(handler, worker);

                        break;

                    default:
                        // If the data models data set has not returned, log error, exit.
                        Log.d(TAG, "processSyncQueue: Data model not found: " +
                                queueHead.toString() + " Exiting sync.");
                        processingSyncQueue = false;
                        break;
                }

            } else {
                Log.d(TAG, "processSyncQueue: First item in queue is not ready, exiting sync.");
                processingSyncQueue = false;
            }
        }
    }
}
