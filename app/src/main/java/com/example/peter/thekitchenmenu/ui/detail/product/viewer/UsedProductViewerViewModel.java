package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.repository.UsedProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.UsedProductRepository;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

public class UsedProductViewerViewModel
        extends AndroidViewModel
        implements UsedProductDataSource.GetUsedProductCallback {

    private UsedProductRepository repository;
    private boolean dataIsLoading;
    public final ObservableBoolean isInUsedList = new ObservableBoolean();
    public final ObservableField<UsedProductEntity> usedProduct = new ObservableField<>();
    private final SingleLiveEvent<Void> addUsedProduct = new SingleLiveEvent<>();

    public UsedProductViewerViewModel(Application application, UsedProductRepository repository) {
        super(application);
        this.repository = repository;
    }

    public void start(String productId) {
        if (productId != null) {
            dataIsLoading = true;
            repository.getUsedProduct(productId, this);
        }
    }

    @Override
    public void onUsedProductLoaded(UsedProductEntity usedProduct) {
        setUsedProduct(usedProduct);
    }

    private void setUsedProduct(UsedProductEntity usedProduct) {
        this.usedProduct.set(usedProduct);
        isInUsedList.set(true);
        dataIsLoading = false;
    }

    @Override
    public void onDataNotAvailable() {
        usedProduct.set(null);
        isInUsedList.set(false);
        dataIsLoading = false;
    }

    void addUsedProduct() {
        addUsedProduct.call();
    }

    public SingleLiveEvent<Void> getAddUsedProduct() {
        return addUsedProduct;
    }
}
