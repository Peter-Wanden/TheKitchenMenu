package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.repository.Repository;
import com.example.peter.thekitchenmenu.model.ProductCommunity;


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
        mListLiveData = mRepository.listAllCommunityProducts();
        return mListLiveData;
    }
    public void setRemoteSyncEnabled(boolean syncEnabled) {
        mRepository.productCommunityIsLive(syncEnabled);
    }
    public String getUserUid() {
        return mUserUid;
    }

    public void setUserUId(String userUid) {
        this.mUserUid = userUid;
    }
}