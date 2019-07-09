package com.example.peter.thekitchenmenu.data.source.remote;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

public class ProductRemoteDataSource implements DataSource<ProductEntity> {

    private static ProductRemoteDataSource INSTANCE;

    public static ProductRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ProductRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull GetAllCallback<ProductEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(
            @NonNull String productId,
            @NonNull GetEntityCallback<ProductEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull ProductEntity product) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // products from all the available data sources.
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@NonNull String productId) {

    }

    public Cursor getMatchingProducts(String searchQuery) {
        // Managed by the local data source
        return null;
    }
}
