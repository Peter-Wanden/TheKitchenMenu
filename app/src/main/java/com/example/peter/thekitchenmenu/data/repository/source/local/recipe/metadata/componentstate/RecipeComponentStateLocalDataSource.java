package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeComponentStateLocalDataSource
        implements PrimitiveDataSource<RecipeComponentStateEntity> {

    private static volatile RecipeComponentStateLocalDataSource INSTANCE;
    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeComponentStateEntityDao dao;

    private RecipeComponentStateLocalDataSource(@Nonnull AppExecutors executors,
                                                @Nonnull RecipeComponentStateEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeComponentStateLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeComponentStateEntityDao dao) {

        if (INSTANCE == null) {
            synchronized (RecipeComponentStateLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeComponentStateLocalDataSource(appExecutors, dao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeComponentStateEntity> c) {
        Runnable r = () -> {
            final List<RecipeComponentStateEntity> entities = dao.getAll();
            executors.diskIO().execute(() -> {
                if (entities.isEmpty())
                    c.onDataUnavailable();
                else
                    c.onAllLoaded(entities);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAllByParentDataId(@Nonnull String parentDataId,
                                     @Nonnull GetAllCallback<RecipeComponentStateEntity> c) {
        Runnable r = () -> {
            final List<RecipeComponentStateEntity> e = dao.getAllByParentDataId(parentDataId);
            executors.mainThread().execute(() -> {
                if (e.isEmpty())
                    c.onDataUnavailable();
                else
                    c.onAllLoaded(e);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetEntityCallback<RecipeComponentStateEntity> c) {
        Runnable r = () -> {
            final RecipeComponentStateEntity e = dao.getByDataId(dataId);
            executors.mainThread().execute(() -> {
                if (e != null)
                    c.onEntityLoaded(e);
                else
                    c.onDataUnavailable();
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void save(@Nonnull RecipeComponentStateEntity e) {
        Runnable r = () -> dao.insert(e);
        executors.diskIO().execute(r);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable r = () -> dao.deleteByDataId(dataId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAllByParentId(String parentDataId) {
        Runnable r = () -> dao.deleteAllByParentDataId(parentDataId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAll() {
        Runnable r = () -> dao.deleteAll();
        executors.diskIO().execute(r);
    }
}
