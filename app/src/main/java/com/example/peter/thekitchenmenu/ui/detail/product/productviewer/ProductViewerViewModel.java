package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

public class ProductViewerViewModel
        extends AndroidViewModel
        implements DataSource.GetEntityCallback<ProductEntity> {

    private static final String TAG = "tkm-ProductViewerVM";

    private DataSource<ProductEntity> productEntityDataSource;
    private ProductViewerNavigator navigator;

    private boolean dataIsLoading;
    public final ObservableField<ProductEntity> product = new ObservableField<>();
    public final ObservableBoolean canAddRemoveFavorites = new ObservableBoolean();

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
        navigator.deleteProduct(product.get().getId());
        productEntityDataSource.deleteById(product.get().getId());
    }

    void editProduct() {
        navigator.editProduct(product.get().getId());
    }

    ObservableBoolean getCanAddRemoveFavorites() {
        return canAddRemoveFavorites;
    }
}
