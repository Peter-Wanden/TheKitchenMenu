package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.app.Application;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

public class ProductViewerViewModel
        extends AndroidViewModel
        implements ProductDataSource.GetProductCallback {

    private static final String TAG = "tkm-ProductViewerVM";

    private ProductRepository repository;
    private ProductViewerNavigator navigator;

    private boolean dataIsLoading;
    public final ObservableField<ProductEntity> product = new ObservableField<>();
    private final SingleLiveEvent<Boolean> productEditedEvent = new SingleLiveEvent<>();

    public ProductViewerViewModel(Application application, ProductRepository repository) {
        super(application);
        this.repository = repository;
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
            repository.getProduct(productId, this);
        }
    }

    @Override
    public void onProductLoaded(ProductEntity product) {
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
        repository.deleteProduct(product.get().getId());
    }

    void editProduct() {
        navigator.editProduct(product.get().getId());
    }

    SingleLiveEvent<Boolean> getProductEditedEvent() {
        return productEditedEvent;
    }

    void handleActivityResult(int requestCode, int resultCode) {
        Log.d(TAG, "handleActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);

        if(resultCode == ProductEditorActivity.RESULT_ADD_EDIT_PRODUCT_OK) {
            //TODO - if not in favorites, add it automatically
            // TODO - Product has been edited so set result for catalog activity to refresh data
            productEditedEvent.setValue(true);
        }
    }
}