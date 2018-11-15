package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;

/**
 * 'DM' in a field's name means data model whereas 'VM' means view model.
 */
public class ViewModelProdComm extends AndroidViewModel {

    private static final String LOG_TAG = ViewModelProdComm.class.getSimpleName();

    private Repository mRepository;
    private LiveData<List<ProductCommunity>> mListLiveData;
    private MutableLiveData<String> mUserId;

    public ViewModelProdComm(Application application) {
        super(application);
        mRepository = new Repository(application);
        mUserId = Constants.getUserId();
    }

    public LiveData<List<ProductCommunity>> getAllProdComms() {
        mListLiveData = mRepository.getAllProdComms();
        return mListLiveData;
    }

    // Turns remote data sync on for all data model objects used by this class
    public void setRemoteSyncEnabled(boolean syncEnabled) {
        mRepository.isLiveProdComm(syncEnabled);
    }

    public MutableLiveData<String> getUserId() {
        return mUserId;
    }
}