package com.example.peter.thekitchenmenu.data.repository;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.model.FavoriteProductModel;

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

    public static FavoriteProductsRepository getInstance(
            FavoriteProductsDataSource remoteDataSource,
            FavoriteProductsDataSource localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new FavoriteProductsRepository(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getAll(@NonNull LoadAllCallback callback) {
        checkNotNull(callback);

        if (favoriteProductsCache != null && !cacheIsDirty) {
            callback.onAllLoaded(new ArrayList<>(favoriteProductsCache.values()));
            return;
        }

        if (cacheIsDirty)
            getFavoriteProductFromRemoteDataSource(callback);

        else {
            localDataSource.getAll(new LoadAllCallback() {
                @Override
                public <E> void onAllLoaded(List<E> entities) {
                    refreshFavoriteProductCache((ArrayList<FavoriteProductEntity>) entities);
                    callback.onAllLoaded(new ArrayList<>(favoriteProductsCache.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getFavoriteProductFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getFavoriteProductFromRemoteDataSource(@NonNull final LoadAllCallback callback) {
        remoteDataSource.getAll(new LoadAllCallback() {
            @Override
            public <E> void onAllLoaded(List<E> entities) {
                refreshFavoriteProductCache((ArrayList<FavoriteProductEntity>) entities);
                refreshLocalDataSource((ArrayList<FavoriteProductEntity>) entities);
                callback.onAllLoaded(new ArrayList<>(favoriteProductsCache.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshFavoriteProductCache(List<FavoriteProductEntity> favoriteProducts) {
        if (favoriteProductsCache == null)
            favoriteProductsCache = new LinkedHashMap<>();
        favoriteProductsCache.clear();

        for (FavoriteProductEntity favoriteProduct: favoriteProducts)
            favoriteProductsCache.put(favoriteProduct.getId(), favoriteProduct);

        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<FavoriteProductEntity> favoriteProducts) {
        localDataSource.deleteAll();
        for (FavoriteProductEntity favoriteProduct : favoriteProducts)
            localDataSource.save(favoriteProduct);
    }

    @Override
    public void getById(@NonNull String favoriteProductId,
                                   @NonNull GetItemCallback callback) {
        
        checkNotNull(favoriteProductId);
        checkNotNull(callback);

        FavoriteProductEntity cachedFavoriteProduct = getFavoriteProductWithId(favoriteProductId);
        if (cachedFavoriteProduct != null) {
            Log.d(TAG, "getFavoriteProduct: cache has favorite product");
            callback.onItemLoaded(cachedFavoriteProduct);
            return;
        }

        localDataSource.getById(favoriteProductId, new GetItemCallback() {
            @Override
            public void onItemLoaded(Object o) {
                if (favoriteProductsCache == null)
                    favoriteProductsCache = new LinkedHashMap<>();

                FavoriteProductEntity favoriteProduct = (FavoriteProductEntity) o;
                favoriteProductsCache.put(favoriteProduct.getId(), favoriteProduct);
                callback.onItemLoaded(favoriteProduct);
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
        if (favoriteProductsCache == null || favoriteProductsCache.isEmpty())
            return null;
        else return
                favoriteProductsCache.get(favoriteProductId);
    }

    @Override
    public void getFavoriteProductByProductId(@NonNull String productId,
                                              @NonNull GetItemCallback callback) {
        checkNotNull(productId);
        checkNotNull(callback);

        FavoriteProductEntity cachedFavoriteProduct = getFavoriteProductWithProductId(productId);
        if (cachedFavoriteProduct !=null) {
            callback.onItemLoaded(cachedFavoriteProduct);
            return;
        }

        localDataSource.getFavoriteProductByProductId(productId, new GetItemCallback() {
            @Override
            public void onItemLoaded(Object o) {
                if (favoriteProductsCache == null)
                    favoriteProductsCache = new LinkedHashMap<>();
                FavoriteProductEntity favoriteProduct = (FavoriteProductEntity) o;
                favoriteProductsCache.put(favoriteProduct.getId(), favoriteProduct);
                callback.onItemLoaded(favoriteProduct);
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
    public void save(@NonNull Object o) {
        FavoriteProductEntity favoriteProduct = (FavoriteProductEntity) o;
        checkNotNull(favoriteProduct);

        remoteDataSource.save(favoriteProduct);
        localDataSource.save(favoriteProduct);

        if (favoriteProductsCache == null)
            favoriteProductsCache = new LinkedHashMap<>();
        favoriteProductsCache.put(favoriteProduct.getId(), favoriteProduct);
    }

    @Override
    public void refreshData() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAll() {
        remoteDataSource.deleteAll();
        localDataSource.deleteAll();

        if (favoriteProductsCache == null)
            favoriteProductsCache = new LinkedHashMap<>();
        favoriteProductsCache.clear();
    }

    @Override
    public void deleteById(@NonNull String favoriteProductId) {
        remoteDataSource.deleteById(favoriteProductId);
        localDataSource.deleteById(favoriteProductId);
        favoriteProductsCache.remove(favoriteProductId);

        for (Map.Entry<String, FavoriteProductEntity> model : favoriteProductsCache.entrySet()) {
            Log.d(TAG, "deleteFavoriteProduct: map has entry=" + model.getKey());
        }
    }
}
