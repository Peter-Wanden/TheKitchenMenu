package com.example.peter.thekitchenmenu.data.repository;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * A service that receives receives intents to sync various remote data models in its own thread.
 */
public class SyncRemoteDataService extends IntentService {

    // The name of this service
    private static final String TAG = "SyncRemoteDataService";

    public SyncRemoteDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String action = intent.getAction();
        boolean activeState = intent.getBooleanExtra("activeState", false);

        // Launches SyncDataTasks which finds and syncs the correct data model.
        SyncDataModelResolver.executeTask(getApplicationContext(), action, activeState);
    }
}
