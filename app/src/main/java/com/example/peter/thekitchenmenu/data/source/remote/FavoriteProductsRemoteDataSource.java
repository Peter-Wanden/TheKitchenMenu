package com.example.peter.thekitchenmenu.data.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsDataSource;

public class FavoriteProductsRemoteDataSource implements FavoriteProductsDataSource {

    private static FavoriteProductsRemoteDataSource INSTANCE;

    public static FavoriteProductsRemoteDataSource getInstance() {
        if (INSTANCE == null) INSTANCE = new FavoriteProductsRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull LoadAllCallback<FavoriteProductEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String id,
                        @NonNull GetEntityCallback<FavoriteProductEntity> callback) {
        callback.onDataNotAvailable();
    }

    public void getFavoriteProductByProductId(
            @NonNull String productId, @NonNull GetEntityCallback<FavoriteProductEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull FavoriteProductEntity favoriteProductEntity) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@NonNull String id) {

    }
}
