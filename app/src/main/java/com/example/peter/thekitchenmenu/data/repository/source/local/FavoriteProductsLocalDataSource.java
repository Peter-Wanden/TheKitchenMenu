package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceFavoriteProducts;

import java.util.List;

import javax.annotation.Nonnull;

public class FavoriteProductsLocalDataSource implements DataSourceFavoriteProducts {

    private static volatile FavoriteProductsLocalDataSource INSTANCE;
    private FavoriteProductEntityDao favoriteProductEntityDao;
    private AppExecutors appExecutors;

    private FavoriteProductsLocalDataSource(
            @Nonnull AppExecutors appExecutors,
            @Nonnull FavoriteProductEntityDao favoriteProductEntityDao) {
        this.appExecutors = appExecutors;
        this.favoriteProductEntityDao = favoriteProductEntityDao;
    }

    public static FavoriteProductsLocalDataSource
    getInstance(@Nonnull AppExecutors appExecutors,
                @Nonnull FavoriteProductEntityDao favoriteProductEntityDao) {
        if (INSTANCE == null) {
            synchronized (FavoriteProductsLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new FavoriteProductsLocalDataSource(
                            appExecutors,
                            favoriteProductEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<FavoriteProductEntity> callback) {
        Runnable runnable = () -> {
            final List<FavoriteProductEntity> favoriteProducts = favoriteProductEntityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (favoriteProducts.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(favoriteProducts);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@Nonnull String favoriteProductId,
                        @Nonnull GetEntityCallback<FavoriteProductEntity> callback) {
        Runnable runnable = () -> {
            final FavoriteProductEntity favoriteProduct =
                    favoriteProductEntityDao.getById(favoriteProductId);
            appExecutors.mainThread().execute(() -> {
                if (favoriteProduct != null) {
                    callback.onEntityLoaded(favoriteProduct);
                } else
                    callback.onDataUnavailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByProductId(@Nonnull String productId,
                               @Nonnull GetEntityCallback<FavoriteProductEntity> callback) {
        Runnable runnable = () -> {
            final FavoriteProductEntity favoriteProduct =
                    favoriteProductEntityDao.getByProductId(productId);
            appExecutors.mainThread().execute(() -> {
                if (favoriteProduct != null)
                    callback.onEntityLoaded(favoriteProduct);
                else
                    callback.onDataUnavailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull FavoriteProductEntity entity) {
        Runnable runnable = () -> favoriteProductEntityDao.insert(entity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> favoriteProductEntityDao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteById(@Nonnull String favoriteProductId) {
        Runnable runnable = () ->
                favoriteProductEntityDao.deleteByFavoriteProductId(favoriteProductId);
        appExecutors.diskIO().execute(runnable);
    }
}
