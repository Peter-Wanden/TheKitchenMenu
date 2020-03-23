package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryFavoriteProduct
        extends Repository<FavoriteProductEntity>
        implements DataSourceFavoriteProducts {

    public static RepositoryFavoriteProduct INSTANCE = null;

    private RepositoryFavoriteProduct(
            @Nonnull DataSourceFavoriteProducts remoteDataSource,
            @Nonnull DataSourceFavoriteProducts localDataSource) {

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryFavoriteProduct getInstance(
            DataSourceFavoriteProducts remoteDataSource,
            DataSourceFavoriteProducts localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryFavoriteProduct(remoteDataSource, localDataSource);
        return INSTANCE;

    }

    @Override
    public void getByProductId(@Nonnull String productId,
                               @Nonnull GetEntityCallback<FavoriteProductEntity> callback) {

        FavoriteProductEntity cachedEntity = checkCacheForProductId(productId);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        ((DataSourceFavoriteProducts)localDataSource).getByProductId(
                productId,
                new GetEntityCallback<FavoriteProductEntity>() {
            @Override
            public void onEntityLoaded(FavoriteProductEntity entity) {
                if (entityCache == null)
                    entityCache = new LinkedHashMap<>();

                entityCache.put(entity.getId(), entity);
                callback.onEntityLoaded(entity);
            }

            @Override
            public void onDataUnavailable() {
                ((DataSourceFavoriteProducts)remoteDataSource).getByProductId(
                        productId,
                        new GetEntityCallback<FavoriteProductEntity>() {
                    @Override
                    public void onEntityLoaded(FavoriteProductEntity entity) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        entityCache.put(entity.getId(), entity);
                        callback.onEntityLoaded(entity);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDataUnavailable();
                    }
                });
            }
        });
    }

    private FavoriteProductEntity checkCacheForProductId(String productId) {

        if (entityCache == null || entityCache.isEmpty())
            return null;
        else {
            for (FavoriteProductEntity entity : entityCache.values()) {
                if (entity.getProductId().equals(productId))
                    return entity;
            }
            return null;
        }
    }
}
