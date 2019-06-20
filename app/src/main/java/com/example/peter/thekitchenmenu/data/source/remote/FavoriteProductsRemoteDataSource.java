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
    public void getFavoriteProducts(LoadFavoriteProductsCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getFavoriteProduct(@NonNull String favoriteProductId, @NonNull GetFavoriteProductCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getFavoriteProductByProductId(@NonNull String productId,
                                              @NonNull GetFavoriteProductCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void saveFavoriteProduct(@NonNull FavoriteProductEntity favoriteProduct) {

    }

    @Override
    public void refreshFavoriteProducts() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllFavoriteProducts() {

    }

    @Override
    public void deleteFavoriteProduct(String favoriteProductId) {

    }
}
