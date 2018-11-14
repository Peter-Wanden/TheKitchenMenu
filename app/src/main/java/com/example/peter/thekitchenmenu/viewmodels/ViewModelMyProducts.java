package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.data.model.ProductMy;
import com.example.peter.thekitchenmenu.data.model.UCProduct;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import java.util.List;

/**
 * 'DM' in a field's name means data model, 'VM' means view model.
 */
public class ViewModelMyProducts extends AndroidViewModel {

    private static final String LOG_TAG = ViewModelMyProducts.class.getSimpleName();

    private String mUserUid = Constants.ANONYMOUS;
    private Repository mRepository;
    // Data models.
    private LiveData<List<ProductMy>> mDMListProductMy;
    private LiveData<List<ProductCommunity>> mDMListProductComm;
    // View models.
    private LiveData<List<UCProduct>> mVMListMyCommProducts;
    private MediatorLiveData<List<ProductCommunity>> mVMMyCommLiveData;

    public ViewModelMyProducts(Application application) {
        super(application);
        mRepository = new Repository(application);
        mDMListProductMy = mRepository.getAllProdMys();
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

    public void setRemoteSyncEnabled(boolean syncEnabled) {
        mRepository.IsLiveProdMy(syncEnabled, this.mUserUid);
    }

    public void setUserId(String userId) {
        // The user ID has been set meaning this ViewModel is live and the user is logged in, so
        // request remote data sync.
        if (!mUserUid.equals(Constants.ANONYMOUS)) {
            this.mUserUid = userId;
            setRemoteSyncEnabled(true);
        }
    }

    public String getUserId() {
        return mUserUid;
    }

    @Override
    protected void onCleared() {
        // This ViewModel has been disposed, so turn remote sync off.
        setRemoteSyncEnabled(false);
        super.onCleared();
    }
}
