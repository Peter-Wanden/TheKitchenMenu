package com.example.peter.thekitchenmenu.data.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsDataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class FavoriteProductsLocalDataSource implements FavoriteProductsDataSource {

    private static volatile FavoriteProductsLocalDataSource INSTANCE;
    private FavoriteProductEntityDao favoriteProductEntityDao;
    private AppExecutors appExecutors;

    private FavoriteProductsLocalDataSource(@NonNull AppExecutors appExecutors,
                                            @NonNull FavoriteProductEntityDao favoriteProductEntityDao) {
        this.appExecutors = appExecutors;
        this.favoriteProductEntityDao = favoriteProductEntityDao;
    }

    public static FavoriteProductsLocalDataSource
    getInstance(@NonNull AppExecutors appExecutors,
                @NonNull FavoriteProductEntityDao favoriteProductEntityDao) {
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
    public void getAll(@NonNull LoadAllCallback callback) {
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
    public void getById(@NonNull String favoriteProductId, @NonNull GetItemCallback callback) {
        Runnable runnable = () -> {
            final FavoriteProductEntity favoriteProduct =
                    favoriteProductEntityDao.getById(favoriteProductId);
            appExecutors.mainThread().execute(() -> {
                if (favoriteProduct != null){
                    callback.onItemLoaded(favoriteProduct);
                }

                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    public void getFavoriteProductByProductId(@NonNull String productId,
                                              @NonNull GetItemCallback callback) {
        Runnable runnable = () -> {
            final FavoriteProductEntity favoriteProduct =
                    favoriteProductEntityDao.getByProductId(productId);
            appExecutors.mainThread().execute(() -> {
                if (favoriteProduct != null)
                    callback.onItemLoaded(favoriteProduct);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@NonNull Object object) {
        checkNotNull(object);
        Runnable runnable = () -> favoriteProductEntityDao.insert((FavoriteProductEntity) object);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link ProductRepository} handles the logic of refreshing the
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
