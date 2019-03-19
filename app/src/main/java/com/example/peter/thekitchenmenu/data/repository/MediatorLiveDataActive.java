package com.example.peter.thekitchenmenu.data.repository;

import androidx.lifecycle.MediatorLiveData;

/**
 * Used for when getting access to the observed states is required.
 * @param <T> the data model being used.
 */
public class MediatorLiveDataActive<T> extends MediatorLiveData<T> {

    private static final String TAG = "MediatorLiveDataActive";

    private Repository repository;
    private String dataModel;

    MediatorLiveDataActive(Repository repository, String dataModel) {
        this.repository = repository;
        this.dataModel = dataModel;
    }

    @Override
    protected void onActive() {
        super.onActive();
        updateDataModelStatus(true);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        updateDataModelStatus(false);
    }

    private void updateDataModelStatus(boolean isActive) {
        repository.observedStateChange(dataModel, isActive);
    }
}
