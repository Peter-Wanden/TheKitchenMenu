package com.example.peter.thekitchenmenu.data.repository;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.util.Preconditions.checkNotNull;

public class FavoriteProductsRepository implements FavoriteProductsDataSource {

    private static final String TAG = "tkm-FavProductRepo";

    private static FavoriteProductsRepository INSTANCE = null;

    private final FavoriteProductsDataSource remoteDataSource;
    private final FavoriteProductsDataSource localDataSource;
    private Map<String, FavoriteProductEntity> favoriteProductsCache;
    private boolean cacheIsDirty = false;

    private FavoriteProductsRepository(@NonNull FavoriteProductsDataSource remoteDataSource,
                                       @NonNull FavoriteProductsDataSource localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static FavoriteProductsRepository getInstance(FavoriteProductsDataSource remoteDataSource,
                                                         FavoriteProductsDataSource localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new FavoriteProductsRepository(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getFavoriteProducts(LoadFavoriteProductsCallback callback) {
        checkNotNull(callback);

        if (favoriteProductsCache != null && !cacheIsDirty) {
            callback.onFavoriteProductsLoaded(new ArrayList<>(favoriteProductsCache.values()));
            return;
        }

        if (cacheIsDirty) getFavoriteProductFromRemoteDataSource(callback);

        else {
            localDataSource.getFavoriteProducts(new LoadFavoriteProductsCallback() {
                @Override
                public void onFavoriteProductsLoaded(List<FavoriteProductEntity> favoriteProducts) {
                    refreshFavoriteProductCache(favoriteProducts);
                    callback.onFavoriteProductsLoaded(
                            new ArrayList<>(favoriteProductsCache.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getFavoriteProductFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getFavoriteProductFromRemoteDataSource(
            @NonNull final LoadFavoriteProductsCallback callback) {

        remoteDataSource.getFavoriteProducts(new LoadFavoriteProductsCallback() {
            @Override
            public void onFavoriteProductsLoaded(List<FavoriteProductEntity> favoriteProducts) {
                refreshFavoriteProductCache(favoriteProducts);
                refreshLocalDataSource(favoriteProducts);
                callback.onFavoriteProductsLoaded(new ArrayList<>(favoriteProductsCache.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshFavoriteProductCache(List<FavoriteProductEntity> favoriteProducts) {
        if (favoriteProductsCache == null) favoriteProductsCache = new LinkedHashMap<>();
        favoriteProductsCache.clear();

        for (FavoriteProductEntity favoriteProduct: favoriteProducts)
            favoriteProductsCache.put(favoriteProduct.getId(), favoriteProduct);

        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<FavoriteProductEntity> favoriteProducts) {
        localDataSource.deleteAllFavoriteProducts();
        for (FavoriteProductEntity favoriteProduct : favoriteProducts)
            localDataSource.saveFavoriteProduct(favoriteProduct);
    }

    @Override
    public void getFavoriteProduct(@NonNull String favoriteProductId,
                                   @NonNull GetFavoriteProductCallback callback) {
        
        checkNotNull(favoriteProductId);
        checkNotNull(callback);

        FavoriteProductEntity cachedFavoriteProduct = getFavoriteProductWithId(favoriteProductId);
        if (cachedFavoriteProduct != null) {
            Log.d(TAG, "getFavoriteProduct: cache has favorite product");
            callback.onFavoriteProductLoaded(cachedFavoriteProduct);
            return;
        }

        localDataSource.getFavoriteProduct(favoriteProductId, new GetFavoriteProductCallback() {
            @Override
            public void onFavoriteProductLoaded(FavoriteProductEntity favoriteProduct) {
                if (favoriteProductsCache == null) favoriteProductsCache = new LinkedHashMap<>();
                favoriteProductsCache.put(favoriteProduct.getId(), favoriteProduct);
                Log.d(TAG, "onFavoriteProductLoaded: local repo has product");
                callback.onFavoriteProductLoaded(favoriteProduct);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "onDataNotAvailable: ");
                callback.onDataNotAvailable();
            }
        });
    }

    @Nullable
    private FavoriteProductEntity getFavoriteProductWithId(String favoriteProductId) {
        checkNotNull(favoriteProductId);
        if (favoriteProductsCache == null || favoriteProductsCache.isEmpty()) return null;
        else return favoriteProductsCache.get(favoriteProductId);
    }

    @Override
    public void getFavoriteProductByProductId(@NonNull String productId,
                                              @NonNull GetFavoriteProductCallback callback) {
        checkNotNull(productId);
        checkNotNull(callback);

        FavoriteProductEntity cachedFavoriteProduct = getFavoriteProductWithProductId(productId);
        if (cachedFavoriteProduct !=null) {
            callback.onFavoriteProductLoaded(cachedFavoriteProduct);
            return;
        }

        localDataSource.getFavoriteProductByProductId(productId, new GetFavoriteProductCallback() {
            @Override
            public void onFavoriteProductLoaded(FavoriteProductEntity favoriteProduct) {
                if (favoriteProductsCache == null) favoriteProductsCache = new LinkedHashMap<>();
                favoriteProductsCache.put(favoriteProduct.getId(), favoriteProduct);
                callback.onFavoriteProductLoaded(favoriteProduct);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    private FavoriteProductEntity getFavoriteProductWithProductId(String productId) {
        checkNotNull(productId);

        if (favoriteProductsCache == null || favoriteProductsCache.isEmpty()) return null;
        else {
            for (String key : favoriteProductsCache.keySet()) {
                FavoriteProductEntity favoriteProduct = favoriteProductsCache.get(key);
                if (favoriteProduct.getProductId().equals(productId)) return favoriteProduct;
            }
            return null;
        }
    }

    @Override
    public void saveFavoriteProduct(@NonNull FavoriteProductEntity favoriteProduct) {
        checkNotNull(favoriteProduct);

        remoteDataSource.saveFavoriteProduct(favoriteProduct);
        localDataSource.saveFavoriteProduct(favoriteProduct);
        if (favoriteProductsCache == null) favoriteProductsCache = new LinkedHashMap<>();
        favoriteProductsCache.put(favoriteProduct.getId(), favoriteProduct);
    }

    @Override
    public void refreshFavoriteProducts() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllFavoriteProducts() {
        remoteDataSource.deleteAllFavoriteProducts();
        localDataSource.deleteAllFavoriteProducts();

        if (favoriteProductsCache == null) favoriteProductsCache = new LinkedHashMap<>();
        favoriteProductsCache.clear();
    }

    @Override
    public void deleteFavoriteProduct(String favoriteProductId) {
        remoteDataSource.deleteFavoriteProduct(favoriteProductId);
        localDataSource.deleteFavoriteProduct(favoriteProductId);
        favoriteProductsCache.remove(favoriteProductId);
    }
}
