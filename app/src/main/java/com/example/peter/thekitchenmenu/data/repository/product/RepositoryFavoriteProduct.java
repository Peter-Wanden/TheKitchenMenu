package com.example.peter.thekitchenmenu.data.repository.product;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;

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
                if (cache == null)
                    cache = new LinkedHashMap<>();

                cache.put(entity.getId(), entity);
                callback.onEntityLoaded(entity);
            }

            @Override
            public void onDataUnavailable() {
                ((DataSourceFavoriteProducts)remoteDataSource).getByProductId(
                        productId,
                        new GetEntityCallback<FavoriteProductEntity>() {
                    @Override
                    public void onEntityLoaded(FavoriteProductEntity entity) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        cache.put(entity.getId(), entity);
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

        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (FavoriteProductEntity entity : cache.values()) {
                if (entity.getProductId().equals(productId))
                    return entity;
            }
            return null;
        }
    }
}
