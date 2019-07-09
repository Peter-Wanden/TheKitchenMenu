package com.example.peter.thekitchenmenu.data.source.local;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;


public class ProductLocalDataSource implements DataSource<ProductEntity> {

    private static volatile ProductLocalDataSource INSTANCE;
    private ProductEntityDao productDao;
    private AppExecutors appExecutors;

    private ProductLocalDataSource(@NonNull AppExecutors appExecutors,
                                   @NonNull ProductEntityDao productDao) {
        this.appExecutors = appExecutors;
        this.productDao = productDao;
    }

    public static ProductLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                     @NonNull ProductEntityDao productDao) {
        if (INSTANCE == null) {
            synchronized (ProductLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new ProductLocalDataSource(appExecutors, productDao);
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link GetAllCallback#onDataNotAvailable()} is fired if the
     * database doesn't exist or the table is empty
     */
    @Override
    public void getAll(@NonNull GetAllCallback<ProductEntity> callback) {
        Runnable getAllProductsRunnable = () -> {
            final List<ProductEntity> products = productDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (products.isEmpty())
                    callback.onDataNotAvailable(); // if new or empty table
                else
                    callback.onAllLoaded(products);
            });
        };
        appExecutors.diskIO().execute(getAllProductsRunnable);
    }

    @Override
    public void getById(@NonNull String productId,
                        @NonNull GetEntityCallback<ProductEntity> callback) {

        Runnable getProductByIdRunnable = () -> {
            final ProductEntity product = productDao.getById(productId);
            appExecutors.mainThread().execute(() -> {
                if (product != null)
                    callback.onEntityLoaded(product);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(getProductByIdRunnable);
    }

    @Override
    public void save(@NonNull ProductEntity product) {
        checkNotNull(product);
        Runnable saveProductRunnable = () -> productDao.insert(product);
        appExecutors.diskIO().execute(saveProductRunnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable deleteAllRunnable = () -> productDao.deleteProducts();
        appExecutors.diskIO().execute(deleteAllRunnable);
    }

    @Override
    public void deleteById(@NonNull final String productId) {
        Runnable deleteProductRunnable = () -> productDao.deleteById(productId);
        appExecutors.diskIO().execute(deleteProductRunnable);
    }

    public Cursor getMatchingProducts(String searchQuery) {
        return productDao.findProductsThatMatch(searchQuery);
    }
}
