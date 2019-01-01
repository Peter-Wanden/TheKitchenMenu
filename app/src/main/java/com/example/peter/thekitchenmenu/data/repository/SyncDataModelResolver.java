package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

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

    synchronized static void executeTask(Context context, String action, boolean observedState) {

        RepositoryRemote repositoryRemote = ((Singletons) context).getRepositoryRemote();

        // Only sync if the user is logged in
        if (!Constants.getUserId().getValue().equals(ANONYMOUS)) {

            // Resolve the type of intent
            if (ACTION_SYNC_PROD_COMM.equals(action)) {
                // Turn remote sync on / off.
                repositoryRemote.isObserved(ACTION_SYNC_PROD_COMM, observedState);

            } else if (ACTION_SYNC_PROD_MY.equals(action)) {
                // Turn remote sync on / off.
                repositoryRemote.isObserved(ACTION_SYNC_PROD_MY, observedState);
            }
        }
    }
}
