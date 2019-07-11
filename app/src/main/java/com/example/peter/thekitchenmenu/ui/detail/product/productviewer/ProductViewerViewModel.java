package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

public class ProductViewerViewModel
        extends AndroidViewModel
        implements DataSource.GetEntityCallback<ProductEntity> {

    private static final String TAG = "tkm-ProductViewerVM";

    private DataSource<ProductEntity> productEntityDataSource;
    private ProductViewerNavigator navigator;

    private boolean dataIsLoading;
    private boolean newProductAdded;
    public final ObservableField<ProductEntity> product = new ObservableField<>();
    public final ObservableBoolean canAddRemoveFavorites = new ObservableBoolean();

    private final SingleLiveEvent<Boolean> hasOptionsMenuEvent = new SingleLiveEvent<>();

    public ProductViewerViewModel(Application application,
                                  DataSource<ProductEntity> productEntityDataSource) {
        super(application);
        this.productEntityDataSource = productEntityDataSource;
    }

    void setNavigator(ProductViewerNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(String productId) {
        if (productId != null) {
            dataIsLoading = true;
            productEntityDataSource.getById(productId, this);
        }
    }

    boolean isNewProductAdded() {
        return newProductAdded;
    }

    void setNewProductAdded(boolean newProductAdded) {
        this.newProductAdded = newProductAdded;
    }

    @Override
    public void onEntityLoaded(ProductEntity product) {
        setProduct(product);
        dataIsLoading = false;
    }

    private void setProduct(ProductEntity product) {
        this.product.set(product);
    }

    @Override
    public void onDataNotAvailable() {
        product.set(null);
        dataIsLoading = false;
    }

    void deleteProduct() {
        productEntityDataSource.deleteById(product.get().getId());
        navigator.deleteProduct(product.get().getId());
    }

    void editProduct() {
        navigator.editProduct(product.get().getId());
    }

    ObservableBoolean getCanAddRemoveFavorites() {
        return canAddRemoveFavorites;
    }

    SingleLiveEvent<Boolean> getHasOptionsMenuEvent() {
        return hasOptionsMenuEvent;
    }
}
