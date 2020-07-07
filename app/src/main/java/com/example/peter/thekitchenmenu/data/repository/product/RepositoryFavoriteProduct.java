package com.example.peter.thekitchenmenu.data.repository.product;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.product.component.favoriteproduct.FavoriteProductPersistenceDomainModel;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryFavoriteProduct
        extends Repository<FavoriteProductPersistenceDomainModel>
        implements DataSourceFavoriteProducts {

    public static RepositoryFavoriteProduct INSTANCE = null;

    private RepositoryFavoriteProduct(
            @Nonnull DataSourceFavoriteProducts remoteDataSource,
            @Nonnull DataSourceFavoriteProducts localDataSource) {

//        this.remoteDomainDataAccess = remoteDataSource;
//        this.localDomainDataAccess = localDataSource;
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
                               @Nonnull GetPrimitiveCallback<FavoriteProductEntity> callback) {

        FavoriteProductEntity cachedEntity = checkCacheForProductId(productId);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        ((DataSourceFavoriteProducts) localDomainDataAccess).getByProductId(
                productId,
                new GetPrimitiveCallback<FavoriteProductEntity>() {
            @Override
            public void onEntityLoaded(FavoriteProductEntity entity) {
                if (cache == null)
                    cache = new LinkedHashMap<>();

//                cache.put(entity.getDataId(), entity);
                callback.onEntityLoaded(entity);
            }

            @Override
            public void onDataUnavailable() {
                ((DataSourceFavoriteProducts) remoteDomainDataAccess).getByProductId(
                        productId,
                        new GetPrimitiveCallback<FavoriteProductEntity>() {
                    @Override
                    public void onEntityLoaded(FavoriteProductEntity entity) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

//                        cache.put(entity.getDataId(), entity);
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
//            for (FavoriteProductEntity entity : cache.values()) {
//                if (entity.getProductId().equals(productId))
//                    return entity;
//            }
            return null;
        }
    }

    @Override
    public void getByDataId(@Nonnull String dataId, @Nonnull GetPrimitiveCallback<FavoriteProductEntity> callback) {

    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<FavoriteProductEntity> callback) {

    }

    @Override
    public void save(@Nonnull FavoriteProductEntity entity) {

    }
}
