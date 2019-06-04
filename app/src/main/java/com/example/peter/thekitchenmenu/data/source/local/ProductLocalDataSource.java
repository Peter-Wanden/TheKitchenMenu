package com.example.peter.thekitchenmenu.data.source.local;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;


public class ProductLocalDataSource implements ProductDataSource {

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
     * Note: {@link LoadProductsCallback#onDataNotAvailable()} is fired if the database doesn't
     * exist or the table is empty
     */
    @Override
    public void getProducts(LoadProductsCallback callback) {
        Runnable getAllProductsRunnable = () -> {
            final List<ProductEntity> products = productDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (products.isEmpty()) callback.onDataNotAvailable(); // if new table or empty
                else callback.onProductsLoaded(products);
            });
        };
        appExecutors.diskIO().execute(getAllProductsRunnable);
    }

    @Override
    public void getProduct(@NonNull String productId, @NonNull GetProductCallback callback) {
        Runnable getProductByIdRunnable = () -> {
            final ProductEntity product = productDao.getById(productId);
            appExecutors.mainThread().execute(() -> {
                if (product != null) callback.onProductLoaded(product);
                else callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(getProductByIdRunnable);
    }

    @Override
    public void saveProduct(ProductEntity product) {
        checkNotNull(product);
        Runnable saveProductRunnable = () -> productDao.insert(product);
        appExecutors.diskIO().execute(saveProductRunnable);
    }

    @Override
    public void refreshProducts() {
        // Not required because the {@link ProductRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllProducts() {
        Runnable deleteAllRunnable = () -> productDao.deleteProducts();
        appExecutors.diskIO().execute(deleteAllRunnable);
    }

    @Override
    public void deleteProduct(@NonNull final String productId) {
        Runnable deleteProductRunnable = () -> productDao.deleteById(productId);
        appExecutors.diskIO().execute(deleteProductRunnable);
    }

    @Override
    public Cursor getMatchingProducts(String searchQuery) {
        return productDao.findProductsThatMatch(searchQuery);
    }
}
