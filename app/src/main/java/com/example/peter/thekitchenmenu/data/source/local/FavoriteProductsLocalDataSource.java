package com.example.peter.thekitchenmenu.data.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsDataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class FavoriteProductsLocalDataSource implements FavoriteProductsDataSource {

    private static volatile FavoriteProductsLocalDataSource INSTANCE;
    private FavoriteProductEntityDAO favoriteProductEntityDao;
    private AppExecutors appExecutors;

    private FavoriteProductsLocalDataSource(@NonNull AppExecutors appExecutors,
                                            @NonNull FavoriteProductEntityDAO favoriteProductEntityDao) {
        this.appExecutors = appExecutors;
        this.favoriteProductEntityDao = favoriteProductEntityDao;
    }

    public static FavoriteProductsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                              @NonNull FavoriteProductEntityDAO favoriteProductEntityDao) {
        if (INSTANCE == null) {
            synchronized (FavoriteProductsLocalDataSource.class) {
                if (INSTANCE == null) INSTANCE= new FavoriteProductsLocalDataSource(
                        appExecutors,
                        favoriteProductEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getFavoriteProducts(LoadFavoriteProductsCallback callback) {
        Runnable runnable = () -> {
            final List<FavoriteProductEntity> favoriteProducts = favoriteProductEntityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (favoriteProducts.isEmpty()) callback.onDataNotAvailable();
                else callback.onFavoriteProductsLoaded(favoriteProducts);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getFavoriteProduct(@NonNull String favoriteProductId,
                                   @NonNull GetFavoriteProductCallback callback) {
        Runnable runnable = () -> {
            final FavoriteProductEntity favoriteProduct =
                    favoriteProductEntityDao.getById(favoriteProductId);
            appExecutors.mainThread().execute(() -> {
                if (favoriteProduct != null) callback.onFavoriteProductLoaded(favoriteProduct);
                else callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getFavoriteProductByProductId(@NonNull String productId,
                                              @NonNull GetFavoriteProductCallback callback) {
        Runnable runnable = () -> {
            final FavoriteProductEntity favoriteProduct =
                    favoriteProductEntityDao.getByProductId(productId);
            appExecutors.mainThread().execute(() -> {
                if (favoriteProduct != null) callback.onFavoriteProductLoaded(favoriteProduct);
                else callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveFavoriteProduct(@NonNull FavoriteProductEntity favoriteProduct) {
        checkNotNull(favoriteProduct);
        Runnable runnable = () -> favoriteProductEntityDao.insert(favoriteProduct);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshFavoriteProducts() {
        // Not required because the {@link ProductRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllFavoriteProducts() {
        Runnable runnable = () -> favoriteProductEntityDao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteFavoriteProduct(String favoriteProductId) {
        Runnable runnable = () ->
                favoriteProductEntityDao.deleteByFavoriteProductId(favoriteProductId);
        appExecutors.diskIO().execute(runnable);
    }
}
