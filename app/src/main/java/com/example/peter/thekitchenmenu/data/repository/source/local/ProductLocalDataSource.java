package com.example.peter.thekitchenmenu.data.repository.source.local;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class ProductLocalDataSource implements DataSource<ProductEntity> {

    private static volatile ProductLocalDataSource INSTANCE;
    private ProductEntityDao entityDao;
    private AppExecutors appExecutors;

    private ProductLocalDataSource(@NonNull AppExecutors appExecutors,
                                   @NonNull ProductEntityDao entityDao) {
        this.appExecutors = appExecutors;
        this.entityDao = entityDao;
    }

    public static ProductLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                     @NonNull ProductEntityDao entityDao) {
        if (INSTANCE == null) {
            synchronized (ProductLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new ProductLocalDataSource(appExecutors, entityDao);
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
        Runnable runnable = () -> {
            final List<ProductEntity> entityList = entityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (entityList.isEmpty())
                    callback.onDataNotAvailable(); // if new or empty table
                else
                    callback.onAllLoaded(entityList);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@NonNull String id,
                        @NonNull GetEntityCallback<ProductEntity> callback) {

        Runnable runnable = () -> {
            final ProductEntity entity = entityDao.getById(id);
            appExecutors.mainThread().execute(() -> {
                if (entity != null)
                    callback.onEntityLoaded(entity);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@NonNull ProductEntity entity) {
        checkNotNull(entity);
        Runnable runnable = () -> entityDao.insert(entity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing data
        // from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> entityDao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteById(@NonNull final String id) {
        Runnable runnable = () -> entityDao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }

    public Cursor getMatchingProducts(String searchQuery) {
        return entityDao.findProductsThatMatch(searchQuery);
    }
}
