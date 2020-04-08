package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeDurationLocalDataSource implements PrimitiveDataSource<RecipeDurationEntity> {

    private static volatile RecipeDurationLocalDataSource INSTANCE;
    private RecipeDurationDao dao;
    private AppExecutors appExecutors;

    private RecipeDurationLocalDataSource(@Nonnull AppExecutors appExecutors,
                                          @Nonnull RecipeDurationDao dao) {
        this.appExecutors = appExecutors;
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
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeDurationEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeDurationEntity> list = dao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (list.isEmpty()) {
                    callback.onDataUnavailable();
                } else {
                    callback.onAllLoaded(list);
                }
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<RecipeDurationEntity> callback) {
        Runnable runnable = () -> {
            final RecipeDurationEntity entity = dao.getById(dataId);
            appExecutors.mainThread().execute(() -> {
                if (entity != null) {
                    callback.onEntityLoaded(entity);
                } else {
                    callback.onDataUnavailable();
                }
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull RecipeDurationEntity entity) {
        Runnable runnable = () -> dao.insert(entity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing data
        // from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable runnable = () -> dao.deleteById(dataId);
        appExecutors.diskIO().execute(runnable);
    }
}
