package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeMetaData;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.dao.RecipeMetadataEntityDao;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalDataSource implements DataSourceRecipeMetaData {

    private static volatile RecipeMetadataLocalDataSource INSTANCE;
    private RecipeMetadataEntityDao dao;
    private AppExecutors executors;

    private RecipeMetadataLocalDataSource(@Nonnull AppExecutors executors,
                                          @Nonnull RecipeMetadataEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeMetadataLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeMetadataEntityDao recipeMetadataEntityDao) {
        if (INSTANCE == null) {
            synchronized (RecipeMetadataLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeMetadataLocalDataSource(
                            appExecutors,
                            recipeMetadataEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeMetadataEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeMetadataEntity> recipeMetadataEntityList = dao.getAll();
            executors.mainThread().execute(() -> {
                if (recipeMetadataEntityList.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(recipeMetadataEntityList);
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@Nonnull String recipeId,
                        @Nonnull GetEntityCallback<RecipeMetadataEntity> callback) {
        Runnable runnable = () -> {
            final RecipeMetadataEntity recipeMetadataEntity = dao.getById(recipeId);
            executors.mainThread().execute(() -> {
                if (recipeMetadataEntity != null)
                    callback.onEntityLoaded(recipeMetadataEntity);
                else
                    callback.onDataUnavailable();
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetEntityCallback<RecipeMetadataEntity> callback) {
        Runnable r = () -> {
            final RecipeMetadataEntity e = dao.getByRecipeId(recipeId);
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
    public void save(@Nonnull RecipeMetadataEntity entity) {
        Runnable runnable = () -> dao.insert(entity);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteById(@Nonnull final String id) {
        Runnable runnable = () -> dao.deleteById(id);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteByRecipeId(@Nonnull String recipeId) {
        Runnable r = () -> dao.deleteByRecipeId(recipeId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        executors.diskIO().execute(runnable);
    }
}
