package com.example.peter.thekitchenmenu.data.repository.source.remote;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.product.DataSourceFavoriteProducts;

import javax.annotation.Nonnull;

public class FavoriteProductsRemoteDataSource implements DataSourceFavoriteProducts {

    private static FavoriteProductsRemoteDataSource INSTANCE;

    public static FavoriteProductsRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new FavoriteProductsRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetEntityCallback<FavoriteProductEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getByProductId(@Nonnull String productId,
                               @Nonnull GetEntityCallback<FavoriteProductEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull FavoriteProductEntity entity) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {

    }
}
