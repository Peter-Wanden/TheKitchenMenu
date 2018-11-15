package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.data.model.ProductMy;
import com.example.peter.thekitchenmenu.data.model.UCProduct;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import java.util.List;

/**
 * 'DM' in a field's name means data model whereas 'VM' means view model.
 */
public class ViewModelProdMy extends AndroidViewModel {

    private static final String LOG_TAG = ViewModelProdMy.class.getSimpleName();

    private Repository mRepository;
    private LiveData<List<ProductCommunity>> mDMListProductComm;
    private MutableLiveData<String> userId;

    // Data models.
    private LiveData<List<ProductMy>> mDMListProductMy;
    // View models.
    private LiveData<List<UCProduct>> mVMListMyCommProducts;
    private MediatorLiveData<List<ProductCommunity>> mVMMyCommLiveData;

    public ViewModelProdMy(Application application) {
        super(application);
        mRepository = new Repository(application);
        mDMListProductMy = mRepository.getAllProdMys();
        userId = Constants.getUserId();
    }

    public LiveData<List<ProductCommunity>> getMyCommProducts() {
        mDMListProductComm = Transformations.switchMap(mDMListProductMy, productMys
                -> {
            int pmId[] = new int[productMys.size()];
            for (int i = 0; i < productMys.size(); i++) {
                pmId[i] = productMys.get(i).getCommunityProductId();
            }
            return mRepository.getProdCommsByIdArray(pmId);
        });
        return mDMListProductComm;
    }

    // Turns remote data sync on for all data model objects used by this class
    public void setRemoteSyncEnabled(boolean syncEnabled) {
        mRepository.IsLiveProdMy(syncEnabled, this.userId.getValue());
    }

    public MutableLiveData<String> getUserId() {
        return userId;
    }
}
