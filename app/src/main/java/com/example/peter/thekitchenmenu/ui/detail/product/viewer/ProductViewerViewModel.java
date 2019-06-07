package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;

public class    ProductViewerViewModel
        extends AndroidViewModel
        implements ProductDataSource.GetProductCallback {

    private ProductRepository repository;
    private boolean dataIsLoading;
    public final ObservableField<ProductEntity> product = new ObservableField<>();


    public ProductViewerViewModel(@NonNull Application application, ProductRepository repository) {
        super(application);
        this.repository = repository;
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
}
