package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.app.Application;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.repository.UsedProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.UsedProductRepository;
import com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor.UsedProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

public class UsedProductViewerViewModel
        extends AndroidViewModel
        implements UsedProductDataSource.GetUsedProductCallback {

    private static final String TAG = "tkm-UsedProductViewerVM";

    private UsedProductRepository repository;
    private boolean dataIsLoading;
    public final ObservableBoolean isInUsedList = new ObservableBoolean();
    public final ObservableField<UsedProductEntity> usedProduct = new ObservableField<>();
    private final SingleLiveEvent<Void> addUsedProduct = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> editUsedProduct = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> setFabIcon = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> removeUsedProduct = new SingleLiveEvent<>();

    public UsedProductViewerViewModel(Application application, UsedProductRepository repository) {
        super(application);
        this.repository = repository;
    }

    public void start(String productId) {
        if (productId != null) {
            dataIsLoading = true;
            repository.getUsedProductByProductId(productId, this);
        }
    }

    @Override
    public void onUsedProductLoaded(UsedProductEntity usedProduct) {
        Log.d(TAG, "onUsedProductLoaded: ");
        setUsedProduct(usedProduct);
    }

    private void setUsedProduct(UsedProductEntity usedProduct) {
        this.usedProduct.set(usedProduct);
        isInUsedList.set(true);
        setFabIcon.setValue(true);
        dataIsLoading = false;
    }

    @Override
    public void onDataNotAvailable() {
        Log.d(TAG, "onDataNotAvailable: ");
        usedProduct.set(null);
        isInUsedList.set(false);
        setFabIcon.setValue(false);
        dataIsLoading = false;
    }

    public SingleLiveEvent<Boolean> getSetFabIcon() {
        return setFabIcon;
    }

    void onFabClicked() {
        if(isInUsedList.get()) editUsedProduct.call();
        else addUsedProduct.call();
    }

    SingleLiveEvent<Void> getAddUsedProduct() {
        return addUsedProduct;
    }

    SingleLiveEvent<Void> getEditUsedProduct() {
        return editUsedProduct;
    }

    void handleActivityResult(int requestCode, int resultCode) {
        Log.d(TAG, "handleActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);
        if (UsedProductEditorActivity.REQUEST_ADD_EDIT_USED_PRODUCT == requestCode) {
            Log.d(TAG, "handleActivityResult: Yay, used product saved!");
        }
    }

    void removeUsedProduct() {
        if (usedProduct.get() != null) {
            repository.deleteUsedProduct(usedProduct.get().getId());
            removeUsedProduct.call();
        }
    }

    public SingleLiveEvent<Void> getRemoveUsedProduct() {
        return removeUsedProduct;
    }
}
