package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceChild;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeComponentStateLocalDataSource
        implements PrimitiveDataSourceChild<RecipeComponentStateEntity> {

    private static volatile RecipeComponentStateLocalDataSource INSTANCE;
    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeComponentStateDao dao;

    private RecipeComponentStateLocalDataSource(@Nonnull AppExecutors executors,
                                                @Nonnull RecipeComponentStateDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeComponentStateLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeComponentStateDao dao) {

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
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeComponentStateEntity> callback) {
        Runnable r = () -> {
            final List<RecipeComponentStateEntity> entities = dao.getAll();
            executors.diskIO().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAllByParentDataId(
            @Nonnull String parentDataId,
            @Nonnull GetAllPrimitiveCallback<RecipeComponentStateEntity> callback) {
        Runnable r = () -> {
            final List<RecipeComponentStateEntity> e = dao.getAllByParentDataId(parentDataId);
            executors.mainThread().execute(() -> {
                if (e.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(e);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<RecipeComponentStateEntity> callback) {
        Runnable r = () -> {
            final RecipeComponentStateEntity e = dao.getByDataId(dataId);
            executors.mainThread().execute(() -> {
                if (e != null)
                    callback.onEntityLoaded(e);
                else
                    callback.onDataUnavailable();
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void save(@Nonnull RecipeComponentStateEntity entity) {
        Runnable r = () -> dao.insert(entity);
        executors.diskIO().execute(r);
    }

    @Override
    public void save(@Nonnull RecipeComponentStateEntity... entities) {
        Runnable r = () -> dao.insert(entities);
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
    public void deleteAllByParentDataId(@Nonnull String parentDataId) {
        Runnable r = () -> dao.deleteAllByParentDataId(parentDataId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAll() {
        Runnable r = () -> dao.deleteAll();
        executors.diskIO().execute(r);
    }
}
