package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import static androidx.core.util.Preconditions.checkNotNull;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 *
 * This implements a synchronisation between locally persisted data and data obtained from the
 * server, by using the remote data source only if the local database doesn't exist or is empty.
 */
public class ProductRepository implements ProductDataSource {

    private static ProductRepository INSTANCE = null;

    private final ProductDataSource remoteDataSource;
    private final ProductDataSource localDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, ProductEntity> productCache;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

    // Prevent direct instantiation.
    private ProductRepository(@NonNull ProductDataSource remoteDataSource,
                              @NonNull ProductDataSource localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteDataSource the backend data source
     * @param localDataSource  device local storage
     * @return the {@link ProductRepository} instance
     */
    public static ProductRepository getInstance(ProductDataSource remoteDataSource,
                                                ProductDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ProductRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(ProductDataSource, ProductDataSource)} to create a new
     * instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getProducts(LoadProductsCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (productCache != null && !cacheIsDirty) {
            callback.onProductsLoaded(new ArrayList<>(productCache.values()));
            return;
        }

        // If the cache is dirty we need to fetch new data from the network.
        if (cacheIsDirty) getProductsFromRemoteDataSource(callback);
        else {
            // Query the local storage if available. If not, query the network.
            localDataSource.getProducts(new LoadProductsCallback() {
                @Override
                public void onProductsLoaded(List<ProductEntity> products) {
                    refreshProductCache(products);
                    callback.onProductsLoaded(new ArrayList<>(productCache.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getProductsFromRemoteDataSource(callback);
                }
            });

        }
    }

    @Override
    public void getProduct(@NonNull String productId, @NonNull GetProductCallback callback) {

    }

    @Override
    public void saveProduct(ProductEntity product) {
        checkNotNull(product);

        remoteDataSource.saveProduct(product);
        localDataSource.saveProduct(product);

        // Do in memory cache update to keep the app UI up to date
        if (productCache == null) productCache = new LinkedHashMap<>();
        productCache.put(product.getId(), product);
    }

    @Override
    public void refreshProducts() {

    }

    @Override
    public void deleteAllProducts() {

    }

    @Override
    public void deleteProduct(@NonNull String productId) {

    }

    private void getProductsFromRemoteDataSource(@NonNull final LoadProductsCallback callback) {
        remoteDataSource.getProducts(new LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<ProductEntity> products) {
                refreshProductCache(products);
                refreshLocalDataSource(products);
                callback.onProductsLoaded(new ArrayList<>(productCache.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshProductCache(List<ProductEntity> products) {

        if (productCache == null) productCache = new LinkedHashMap<>();
        productCache.clear();

        for (ProductEntity product:products) productCache.put(product.getId(), product);
        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<ProductEntity> products) {

        localDataSource.deleteAllProducts();
        for (ProductEntity product : products) localDataSource.saveProduct(product);
    }
}
