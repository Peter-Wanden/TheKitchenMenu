package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceFavoriteProducts;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class LocalDataSourceFavoriteProducts implements DataSourceFavoriteProducts {

    private static volatile LocalDataSourceFavoriteProducts INSTANCE;
    private FavoriteProductEntityDao favoriteProductEntityDao;
    private AppExecutors appExecutors;

    private LocalDataSourceFavoriteProducts(
            @NonNull AppExecutors appExecutors,
            @NonNull FavoriteProductEntityDao favoriteProductEntityDao) {
        this.appExecutors = appExecutors;
        this.favoriteProductEntityDao = favoriteProductEntityDao;
    }

    public static LocalDataSourceFavoriteProducts
    getInstance(@NonNull AppExecutors appExecutors,
                @NonNull FavoriteProductEntityDao favoriteProductEntityDao) {
        if (INSTANCE == null) {
            synchronized (LocalDataSourceFavoriteProducts.class) {
                if (INSTANCE == null)
                    INSTANCE = new LocalDataSourceFavoriteProducts(
                            appExecutors,
                            favoriteProductEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull GetAllCallback<FavoriteProductEntity> callback) {
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
    public void getById(@NonNull String favoriteProductId,
                        @NonNull GetEntityCallback<FavoriteProductEntity> callback) {
        Runnable runnable = () -> {
            final FavoriteProductEntity favoriteProduct =
                    favoriteProductEntityDao.getById(favoriteProductId);
            appExecutors.mainThread().execute(() -> {
                if (favoriteProduct != null) {
                    callback.onEntityLoaded(favoriteProduct);
                } else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByProductId(@NonNull String productId,
                               @NonNull GetEntityCallback<FavoriteProductEntity> callback) {
        Runnable runnable = () -> {
            final FavoriteProductEntity favoriteProduct =
                    favoriteProductEntityDao.getByProductId(productId);
            appExecutors.mainThread().execute(() -> {
                if (favoriteProduct != null)
                    callback.onEntityLoaded(favoriteProduct);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@NonNull FavoriteProductEntity favoriteProductEntity) {
        checkNotNull(favoriteProductEntity);
        Runnable runnable = () -> favoriteProductEntityDao.insert(favoriteProductEntity);
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
    public void deleteById(@NonNull String favoriteProductId) {
        Runnable runnable = () ->
                favoriteProductEntityDao.deleteByFavoriteProductId(favoriteProductId);
        appExecutors.diskIO().execute(runnable);
    }
}
