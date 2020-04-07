package com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource;

import android.database.Cursor;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.dao.ProductEntityDao;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class ProductLocalDataSource implements PrimitiveDataSource<ProductEntity> {

    private static volatile ProductLocalDataSource INSTANCE;
    private ProductEntityDao entityDao;
    private AppExecutors appExecutors;

    private ProductLocalDataSource(@Nonnull AppExecutors appExecutors,
                                   @Nonnull ProductEntityDao entityDao) {
        this.appExecutors = appExecutors;
        this.entityDao = entityDao;
    }

    public static ProductLocalDataSource getInstance(@Nonnull AppExecutors appExecutors,
                                                     @Nonnull ProductEntityDao entityDao) {
        if (INSTANCE == null) {
            synchronized (ProductLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new ProductLocalDataSource(appExecutors, entityDao);
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link GetAllPrimitiveCallback#onDataUnavailable()} is fired if the
     * database doesn't exist or the table is empty
     */
    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<ProductEntity> callback) {
        Runnable runnable = () -> {
            final List<ProductEntity> entityList = entityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (entityList.isEmpty())
                    callback.onDataUnavailable(); // if new or empty table
                else
                    callback.onAllLoaded(entityList);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<ProductEntity> callback) {

        Runnable runnable = () -> {
            final ProductEntity entity = entityDao.getById(dataId);
            appExecutors.mainThread().execute(() -> {
                if (entity != null)
                    callback.onEntityLoaded(entity);
                else
                    callback.onDataUnavailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull ProductEntity entity) {
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
    public void deleteByDataId(@Nonnull final String dataId) {
        Runnable runnable = () -> entityDao.deleteById(dataId);
        appExecutors.diskIO().execute(runnable);
    }

    public Cursor getMatchingProducts(String searchQuery) {
        return entityDao.findProductsThatMatch(searchQuery);
    }
}
