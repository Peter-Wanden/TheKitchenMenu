package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeComponentState;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.dao.RecipeComponentStateEntityDao;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeComponentStateLocalDataSource implements DataSourceRecipeComponentState {

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
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllCallback<RecipeComponentStateEntity> callback) {
        Runnable r = () -> {
            final List<RecipeComponentStateEntity> entities = dao.getAllByRecipeId(recipeId);
            executors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeComponentStateEntity> callback) {
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
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeComponentStateEntity> callback) {
        Runnable r = () -> {
            final RecipeComponentStateEntity e = dao.getById(id);
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
    public void deleteById(@Nonnull String id) {
        Runnable r = () -> dao.deleteById(id);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAllByRecipeId(@Nonnull String recipeId) {
        Runnable r = () -> dao.deleteByRecipeId(recipeId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAll() {
        Runnable r = () -> dao.deleteAll();
        executors.diskIO().execute(r);
    }
}
