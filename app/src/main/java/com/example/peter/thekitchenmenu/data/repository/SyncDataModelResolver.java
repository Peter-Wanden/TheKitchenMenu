package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;

import static com.example.peter.thekitchenmenu.app.Constants.ANONYMOUS;

/**
 * Holds a list of synchronisation tasks and respective filters for those tasks
 * Runs within {@link SyncRemoteDataService}
 */
class SyncDataModelResolver {

    private static final String TAG = "SyncDataModelResolver";

    // Static constant names for the data models
    private static final String ACTION_SYNC_PROD_COMM = DmProdComm.TAG;
    private static final String ACTION_SYNC_PROD_MY = DmProdMy.TAG;

    synchronized static void executeTask(Context context, String action, boolean activeState) {

        RepositoryRemote repositoryRemote = ((Singletons) context).getRepositoryRemote();

        // Resolve the type of intent
        if (ACTION_SYNC_PROD_COMM.equals(action)) {
            // Turn on / off remote sync.
            Log.d(TAG, "executeTask: " + action);
            // Attach / detach listener to remote data model location and sync data model
            repositoryRemote.isLiveProdComm(activeState);

        } else if (ACTION_SYNC_PROD_MY.equals(action)) {
            Log.d(TAG, "executeTask: " + action);
            if (!Constants.getUserId().getValue().equals(ANONYMOUS)) {
                repositoryRemote.isLiveProdMy(activeState, Constants.getUserId().getValue());
            }
        }
    }
}
