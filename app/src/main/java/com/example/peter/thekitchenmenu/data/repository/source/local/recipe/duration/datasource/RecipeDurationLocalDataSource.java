package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSourceParent;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeDurationLocalDataSource
        implements PrimitiveDataSourceParent<RecipeDurationEntity> {

    private static volatile RecipeDurationLocalDataSource INSTANCE;
    private RecipeDurationDao dao;
    private AppExecutors executors;

    private RecipeDurationLocalDataSource(@Nonnull AppExecutors executors,
                                          @Nonnull RecipeDurationDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeDurationLocalDataSource getInstance(@Nonnull AppExecutors appExecutors,
                                                            @Nonnull RecipeDurationDao dao) {
        if (INSTANCE == null) {
            synchronized (RecipeDurationLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeDurationLocalDataSource(appExecutors, dao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<RecipeDurationEntity> callback) {
        Runnable runnable = () -> {
            final RecipeDurationEntity entity = dao.getById(dataId);
            executors.mainThread().execute(() -> {
                if (entity != null) {
                    callback.onEntityLoaded(entity);
                } else {
                    callback.onDataUnavailable();
                }
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllPrimitiveCallback<RecipeDurationEntity> c) {
        Runnable r = () -> {
            final List<RecipeDurationEntity> e = dao.getAllByDomainId(domainId);
            executors.mainThread().execute(() -> {
                if (e.isEmpty()) {
                    c.onDataUnavailable();
                } else {
                    c.onAllLoaded(e);
                }
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeDurationEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeDurationEntity> list = dao.getAll();
            executors.mainThread().execute(() -> {
                if (list.isEmpty()) {
                    callback.onDataUnavailable();
                } else {
                    callback.onAllLoaded(list);
                }
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull RecipeDurationEntity entity) {
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
        Runnable runnable = () -> dao.deleteById(dataId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable r = () -> dao.deleteAllByDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        executors.diskIO().execute(runnable);
    }
}
