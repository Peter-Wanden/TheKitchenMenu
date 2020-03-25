package com.example.peter.thekitchenmenu.data.repository.source.remote;

import android.database.Cursor;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public class ProductRemoteDataSource implements PrimitiveDataSource<ProductEntity> {

    private static ProductRemoteDataSource INSTANCE;

    public static ProductRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ProductRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<ProductEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getById(
            @Nonnull String productId,
            @Nonnull GetEntityCallback<ProductEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull ProductEntity entity) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing data
        // from all the available data sources.
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@Nonnull String productId) {

    }

    public Cursor getMatchingProducts(String searchQuery) {
        // Managed by the local data source
        return null;
    }
}
