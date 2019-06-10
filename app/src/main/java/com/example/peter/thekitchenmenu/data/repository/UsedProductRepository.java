package com.example.peter.thekitchenmenu.data.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.util.Preconditions.checkNotNull;

public class UsedProductRepository implements UsedProductDataSource {

    private static UsedProductRepository INSTANCE = null;

    private final UsedProductDataSource remoteDataSource;
    private final UsedProductDataSource localDataSource;
    Map<String, UsedProductEntity> usedProductsCache;
    boolean cacheIsDirty = false;

    private UsedProductRepository(@NonNull UsedProductDataSource remoteDataSource,
                                  @NonNull UsedProductDataSource localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static UsedProductRepository getInstance(UsedProductDataSource remoteDataSource,
                                                    UsedProductDataSource localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new UsedProductRepository(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getUsedProducts(LoadUsedProductsCallback callback) {
        checkNotNull(callback);

        if (usedProductsCache != null && !cacheIsDirty) {
            callback.onUsedProductsLoaded(new ArrayList<>(usedProductsCache.values()));
            return;
        }

        if (cacheIsDirty) getUsedProductFromRemoteDataSource(callback);

        else {
            localDataSource.getUsedProducts(new LoadUsedProductsCallback() {
                @Override
                public void onUsedProductsLoaded(List<UsedProductEntity> usedProducts) {
                    refreshUsedProductCache(usedProducts);
                    callback.onUsedProductsLoaded(new ArrayList<>(usedProductsCache.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getUsedProductFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getUsedProductFromRemoteDataSource(
            @NonNull final LoadUsedProductsCallback callback) {

        remoteDataSource.getUsedProducts(new LoadUsedProductsCallback() {
            @Override
            public void onUsedProductsLoaded(List<UsedProductEntity> usedProducts) {
                refreshUsedProductCache(usedProducts);
                refreshLocalDataSource(usedProducts);
                callback.onUsedProductsLoaded(new ArrayList<>(usedProductsCache.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshUsedProductCache(List<UsedProductEntity> usedProducts) {
        if (usedProductsCache == null) usedProductsCache = new LinkedHashMap<>();
        usedProductsCache.clear();

        for (UsedProductEntity usedProduct: usedProducts)
            usedProductsCache.put(usedProduct.getId(), usedProduct);

        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<UsedProductEntity> usedProducts) {
        localDataSource.deleteAllUsedProducts();
        for (UsedProductEntity usedProduct : usedProducts)
            localDataSource.saveUsedProduct(usedProduct);
    }

    @Override
    public void getUsedProduct(@NonNull String usedProductId,
                               @NonNull GetUsedProductCallback callback) {
        checkNotNull(usedProductId);
        checkNotNull(callback);

        UsedProductEntity cachedUsedProduct = getUsedProductWithId(usedProductId);
        if (cachedUsedProduct != null) {
            callback.onUsedProductLoaded(cachedUsedProduct);
            return;
        }

        localDataSource.getUsedProduct(usedProductId, new GetUsedProductCallback() {
            @Override
            public void onUsedProductLoaded(UsedProductEntity usedProduct) {
                if (usedProductsCache == null) usedProductsCache = new LinkedHashMap<>();
                usedProductsCache.put(usedProduct.getId(), usedProduct);
                callback.onUsedProductLoaded(usedProduct);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Nullable
    private UsedProductEntity getUsedProductWithId(String usedProductId) {
        checkNotNull(usedProductId);
        if (usedProductsCache == null || usedProductsCache.isEmpty()) return null;
        else return usedProductsCache.get(usedProductId);
    }

    @Override
    public void getUsedProductByProductId(@NonNull String productId,
                                          @NonNull GetUsedProductCallback callback) {
        checkNotNull(productId);
        checkNotNull(callback);

        UsedProductEntity cachedUsedProduct = getUsedProductWithProductId(productId);
        if (cachedUsedProduct !=null) {
            callback.onUsedProductLoaded(cachedUsedProduct);
            return;
        }

        localDataSource.getUsedProductByProductId(productId, new GetUsedProductCallback() {
            @Override
            public void onUsedProductLoaded(UsedProductEntity usedProduct) {
                if (usedProductsCache == null) usedProductsCache = new LinkedHashMap<>();
                usedProductsCache.put(usedProduct.getId(), usedProduct);
                callback.onUsedProductLoaded(usedProduct);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    private UsedProductEntity getUsedProductWithProductId(String productId) {
        checkNotNull(productId);

        if (usedProductsCache == null || usedProductsCache.isEmpty()) return null;
        else {
            for (String key : usedProductsCache.keySet()) {
                UsedProductEntity usedProduct = usedProductsCache.get(key);
                if (usedProduct.getProductId().equals(productId)) return usedProduct;
            }
            return null;
        }
    }

    @Override
    public void saveUsedProduct(@NonNull UsedProductEntity usedProduct) {
        checkNotNull(usedProduct);

        remoteDataSource.saveUsedProduct(usedProduct);
        localDataSource.saveUsedProduct(usedProduct);
        if (usedProductsCache == null) usedProductsCache = new LinkedHashMap<>();
        usedProductsCache.put(usedProduct.getId(), usedProduct);
    }

    @Override
    public void deleteAllUsedProducts() {
        remoteDataSource.deleteAllUsedProducts();
        localDataSource.deleteAllUsedProducts();

        if (usedProductsCache == null) usedProductsCache = new LinkedHashMap<>();
        usedProductsCache.clear();
    }

    @Override
    public void deleteUsedProduct(String usedProductId) {
        remoteDataSource.deleteUsedProduct(usedProductId);
        localDataSource.deleteUsedProduct(usedProductId);
        usedProductsCache.remove(usedProductId);
    }
}
