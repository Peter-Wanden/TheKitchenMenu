package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;

import java.util.LinkedHashMap;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryFavoriteProduct
        extends Repository<FavoriteProductEntity>
        implements FavoriteProductsDataSource {

    public static RepositoryFavoriteProduct INSTANCE = null;
    private FavoriteProductsDataSource favoriteRemoteDataSource;
    private FavoriteProductsDataSource favoriteLocalDataSource;

    private RepositoryFavoriteProduct(
            @NonNull FavoriteProductsDataSource remoteDataSource,
            @NonNull FavoriteProductsDataSource localDataSource) {

        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
        favoriteRemoteDataSource = checkNotNull(remoteDataSource);
        favoriteLocalDataSource = checkNotNull(localDataSource);
    }

    public static RepositoryFavoriteProduct getInstance(
            FavoriteProductsDataSource remoteDataSource,
            FavoriteProductsDataSource localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryFavoriteProduct(remoteDataSource, localDataSource);
        return INSTANCE;

    }

    @Override
    public void getByProductId(@NonNull String productId,
                               @NonNull GetEntityCallback<FavoriteProductEntity> callback) {
        checkNotNull(productId);
        checkNotNull(callback);

        FavoriteProductEntity cachedEntity = checkCacheForProductId(productId);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        favoriteLocalDataSource.getByProductId(productId, new GetEntityCallback<FavoriteProductEntity>() {
            @Override
            public void onEntityLoaded(FavoriteProductEntity favoriteProductEntity) {
                if (entityCache == null)
                    entityCache = new LinkedHashMap<>();

                entityCache.put(favoriteProductEntity.getId(), favoriteProductEntity);
                callback.onEntityLoaded(favoriteProductEntity);
            }

            @Override
            public void onDataNotAvailable() {
                favoriteRemoteDataSource.getByProductId(
                        productId,
                        new GetEntityCallback<FavoriteProductEntity>() {
                    @Override
                    public void onEntityLoaded(FavoriteProductEntity favoriteProductEntity) {
                        if (favoriteProductEntity == null) {
                            onDataNotAvailable();
                            return;
                        }

                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        entityCache.put(favoriteProductEntity.getId(), favoriteProductEntity);
                        callback.onEntityLoaded(favoriteProductEntity);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
                callback.onDataNotAvailable();
            }
        });
    }

    private FavoriteProductEntity checkCacheForProductId(String productId) {
        checkNotNull(productId);

        if (entityCache == null || entityCache.isEmpty())
            return null;
        else {
            for (FavoriteProductEntity entity : entityCache.values()) {
                if (entity.getProductId().equals(productId)) {
                    return entity;
                }
            }
            return null;
        }
    }
}
