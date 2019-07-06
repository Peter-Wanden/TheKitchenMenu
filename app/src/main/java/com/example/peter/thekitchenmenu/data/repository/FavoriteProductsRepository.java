package com.example.peter.thekitchenmenu.data.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;

import java.util.LinkedHashMap;

import static androidx.core.util.Preconditions.checkNotNull;

public class FavoriteProductsRepository
        extends Repository<FavoriteProductEntity>
        implements FavoriteProductsDataSource {

    private static final String TAG = "tkm-FavProductRepo";

    private static FavoriteProductsRepository INSTANCE = null;

    private FavoriteProductsRepository(@NonNull FavoriteProductsDataSource remoteDataSource,
                                       @NonNull FavoriteProductsDataSource localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static FavoriteProductsRepository getInstance(
            FavoriteProductsDataSource remoteDataSource,
            FavoriteProductsDataSource localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new FavoriteProductsRepository(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getFavoriteProductByProductId(
            @NonNull String productId,
            @NonNull GetEntityCallback<FavoriteProductEntity> callback) {

        checkNotNull(productId);
        checkNotNull(callback);

        FavoriteProductEntity cachedFavoriteProduct = getFavoriteProductWithProductId(productId);
        if (cachedFavoriteProduct !=null) {
            callback.onEntityLoaded(cachedFavoriteProduct);
            return;
        }

        localDataSource.getFavoriteProductByProductId(
                productId,
                new GetEntityCallback<FavoriteProductEntity>() {
            @Override
            public void onEntityLoaded(FavoriteProductEntity favoriteProductEntity) {

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

    }

    private FavoriteProductEntity getFavoriteProductWithProductId(String productId) {
        checkNotNull(productId);

        if (entityCache == null || entityCache.isEmpty()) return null;
        else {
            for (String key : entityCache.keySet()) {
                FavoriteProductEntity favoriteProduct = entityCache.get(key);
                if (favoriteProduct.getProductId().equals(productId))
                    return favoriteProduct;
            }
            return null;
        }
    }
}