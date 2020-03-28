package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeDurationLocalDataSource implements PrimitiveDataSource<RecipeDurationEntity> {

    private static volatile RecipeDurationLocalDataSource INSTANCE;
    private RecipeDurationEntityDao dao;
    private AppExecutors appExecutors;

    private RecipeDurationLocalDataSource(@Nonnull AppExecutors appExecutors,
                                          @Nonnull RecipeDurationEntityDao dao) {
        this.appExecutors = appExecutors;
        this.dao = dao;
    }

    public static RecipeDurationLocalDataSource getInstance(@Nonnull AppExecutors appExecutors,
                                                            @Nonnull RecipeDurationEntityDao dao) {
        if (INSTANCE == null) {
            synchronized (RecipeDurationLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeDurationLocalDataSource(appExecutors, dao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeDurationEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeDurationEntity> list = dao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (list.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(list);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeDurationEntity> callback) {
        Runnable runnable = () -> {
            final RecipeDurationEntity entity = dao.getById(id);
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
    public void deleteById(@Nonnull String id) {
        Runnable runnable = () -> dao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
