package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;


public class ViewModelCommunityProducts extends AndroidViewModel {

    private static final String LOG_TAG = ViewModelCommunityProducts.class.getSimpleName();

    private Repository mRepository;
    private LiveData<List<ProductCommunity>> mListLiveData;
    private String mUserUid = Constants.ANONYMOUS;

    public ViewModelCommunityProducts(Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    public LiveData<List<ProductCommunity>> listAllCommunityProducts() {
        mListLiveData = mRepository.getAllProdComms();
        return mListLiveData;
    }
    public void setRemoteSyncEnabled(boolean syncEnabled) {
        mRepository.isLiveProdComm(syncEnabled);
    }
    public String getUserUid() {
        return mUserUid;
    }

    public void setUserId(String userId) {
        // The user ID has been set meaning this ViewModel is live and the user is logged in, so
        // request remote data sync.
        if (!userId.equals(Constants.ANONYMOUS)) {
            this.mUserUid = userId;
            setRemoteSyncEnabled(true);
        }
    }

    @Override
    protected void onCleared() {
        // This ViewModel has been disposed, so turn of remote sync.
        setRemoteSyncEnabled(false);
        super.onCleared();
    }
}