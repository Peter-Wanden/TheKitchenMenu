package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dao.IngredientEntityDao;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class IngredientLocalDataSource implements PrimitiveDataSource<IngredientEntity> {

    private static volatile IngredientLocalDataSource INSTANCE;
    private IngredientEntityDao entityDao;
    private AppExecutors appExecutors;

    private IngredientLocalDataSource(@Nonnull AppExecutors appExecutors,
                                      @Nonnull IngredientEntityDao entityDao) {
        this.appExecutors = appExecutors;
        this.entityDao = entityDao;
    }

    public static IngredientLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull IngredientEntityDao entityDao) {

        if (INSTANCE == null) {
            synchronized (IngredientLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new IngredientLocalDataSource(appExecutors, entityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<IngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<IngredientEntity> entityList = entityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (entityList.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entityList);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByDataId(@Nonnull String dataId, @Nonnull GetEntityCallback<IngredientEntity> callback) {
        Runnable runnable = () -> {
            final IngredientEntity entity = entityDao.getById(dataId);
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
    public void save(@Nonnull IngredientEntity entity) {
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
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable runnable = () -> entityDao.deleteById(dataId);
        appExecutors.diskIO().execute(runnable);
    }
}
