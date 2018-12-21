package com.example.peter.thekitchenmenu.data.repository;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;

/**
 * Used for when getting access to the observed states is required.
 * @param <T> the data model being used.
 */
public class MediatorLiveDataActive<T> extends MediatorLiveData<T> {

    private static final String TAG = "MediatorLiveDataActive";

    private Repository mRepository;
    private String mDataModel;

    MediatorLiveDataActive(Repository repository, String dataModel) {
        mRepository = repository;
        mDataModel = dataModel;
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
        mRepository.dataModelIsActive(mDataModel, isActive);
    }



}
