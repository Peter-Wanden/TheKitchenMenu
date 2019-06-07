package com.example.peter.thekitchenmenu.data.source.remote;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;

public class ProductRemoteDataSource implements ProductDataSource {

    private static ProductRemoteDataSource INSTANCE;

    public static ProductRemoteDataSource getInstance() {
        if (INSTANCE == null) INSTANCE = new ProductRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getProducts(final @NonNull LoadProductsCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getProduct(@NonNull String productId, final @NonNull GetProductCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void saveProduct(ProductEntity product) {

    }

    @Override
    public void refreshProducts() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllProducts() {

    }

    @Override
    public void deleteProduct(@NonNull String productId) {

    }

    @Override
    public Cursor getMatchingProducts(String searchQuery) {
        // Managed by the local data source
        return null;
    }
}
