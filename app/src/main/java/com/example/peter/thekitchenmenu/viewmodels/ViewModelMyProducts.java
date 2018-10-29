package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.ProductMy;
import com.example.peter.thekitchenmenu.repository.Repository;

import java.util.List;

public class ViewModelMyProducts extends AndroidViewModel {

    private static final String LOG_TAG = ViewModelMyProducts.class.getSimpleName();

    private Repository mRepository;
    private LiveData<List<ProductMy>> mListLiveData;
    private String mUserUid = Constants.ANONYMOUS;

    public ViewModelMyProducts(Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    public LiveData<List<ProductMy>> listAllMyProducts() {
        mListLiveData = mRepository.listAllMyProducts();
        return mListLiveData;
    }
    public void setUserId(String userId) {
        this.mUserUid = userId;
    }
    public String getUserId() {
        return mUserUid;
    }
}
