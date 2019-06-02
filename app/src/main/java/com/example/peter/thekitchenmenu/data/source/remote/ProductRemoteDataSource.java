package com.example.peter.thekitchenmenu.data.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;

public class ProductRemoteDataSource implements ProductDataSource {

    @Override
    public void getProducts(LoadProductsCallback callback) {

    }

    @Override
    public void getProduct(@NonNull String productId, @NonNull GetProductCallback callback) {

    }

    @Override
    public void saveProduct(ProductEntity product) {

    }

    @Override
    public void refreshProducts() {

    }

    @Override
    public void deleteProduct(@NonNull String productId) {

    }
}
