package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceParent;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class IngredientLocalDataSource
        implements PrimitiveDataSourceParent<IngredientEntity> {

    private static volatile IngredientLocalDataSource INSTANCE;
    private IngredientDao dao;
    private AppExecutors executors;

    private IngredientLocalDataSource(@Nonnull AppExecutors executors,
                                      @Nonnull IngredientDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static IngredientLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull IngredientDao entityDao) {

        if (INSTANCE == null) {
            synchronized (IngredientLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new IngredientLocalDataSource(appExecutors, entityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<IngredientEntity> callback) {
        Runnable runnable = () -> {
            final IngredientEntity entity = dao.getById(dataId);
            executors.mainThread().execute(() -> {
                if (entity != null)
                    callback.onEntityLoaded(entity);
                else
                    callback.onDataUnavailable();
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByDomainId(@Nonnull String domainId,
                                 @Nonnull GetAllPrimitiveCallback<IngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<IngredientEntity> entities = dao.getAllByDomainId(domainId);
            executors.mainThread().execute(() -> {
                if (entities.isEmpty()) {
                    callback.onDataUnavailable();
                } else {
                    callback.onAllLoaded(entities);
                }
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<IngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<IngredientEntity> entityList = dao.getAll();
            executors.mainThread().execute(() -> {
                if (entityList.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entityList);
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull IngredientEntity entity) {
        Runnable runnable = () -> dao.insert(entity);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing data
        // from all the available data sources.
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable runnable = () -> dao.deleteByDataId(dataId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable runnable = () -> dao.deleteAllByDomainId(domainId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        executors.diskIO().execute(runnable);
    }
}
