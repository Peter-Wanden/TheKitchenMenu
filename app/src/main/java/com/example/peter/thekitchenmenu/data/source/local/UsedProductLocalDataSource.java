package com.example.peter.thekitchenmenu.data.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.repository.UsedProductDataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class UsedProductLocalDataSource implements UsedProductDataSource {

    private static volatile UsedProductLocalDataSource INSTANCE;
    private UsedProductEntityDAO usedProductEntityDao;
    private AppExecutors appExecutors;

    private UsedProductLocalDataSource (@NonNull AppExecutors appExecutors,
                                        @NonNull UsedProductEntityDAO usedProductEntityDao) {
        this.appExecutors = appExecutors;
        this.usedProductEntityDao = usedProductEntityDao;
    }

    public static UsedProductLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                         @NonNull UsedProductEntityDAO usedProductEntityDao) {
        if (INSTANCE == null) {
            synchronized (UsedProductLocalDataSource.class) {
                if (INSTANCE == null) INSTANCE= new UsedProductLocalDataSource(
                        appExecutors,
                        usedProductEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getUsedProducts(LoadUsedProductsCallback callback) {
        Runnable getAllUsedProductsRunnable = () -> {
            final List<UsedProductEntity> usedProducts = usedProductEntityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (usedProducts.isEmpty()) callback.onDataNotAvailable();
                else callback.onUsedProductsLoaded(usedProducts);
            });
        };
        appExecutors.diskIO().execute(getAllUsedProductsRunnable);
    }

    @Override
    public void getUsedProduct(@NonNull String usedProductId,
                               @NonNull GetUsedProductCallback callback) {
        Runnable getUsedProductByIdRunnable = () -> {
            final UsedProductEntity usedProduct = usedProductEntityDao.getById(usedProductId);
            appExecutors.mainThread().execute(() -> {
                if (usedProduct != null) callback.onUsedProductLoaded(usedProduct);
                else callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(getUsedProductByIdRunnable);
    }

    @Override
    public void saveUsedProduct(@NonNull UsedProductEntity usedProduct) {
        checkNotNull(usedProduct);
        Runnable saveUsedProductRunnable = () -> usedProductEntityDao.insert(usedProduct);
        appExecutors.diskIO().execute(saveUsedProductRunnable);
    }

    @Override
    public void deleteAllUsedProducts() {
        Runnable deleteAllUsedProductsRunnable = () -> usedProductEntityDao.deleteAll();
        appExecutors.diskIO().execute(deleteAllUsedProductsRunnable);
    }

    @Override
    public void deleteUsedProduct(String usedProductId) {
        Runnable deleteUsedProductById = () -> usedProductEntityDao.deleteByUsedProductId(usedProductId);
        appExecutors.diskIO().execute(deleteUsedProductById);
    }
}
