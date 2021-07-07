package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIngredientLocalDataSource
        implements RecipeIngredientLocal {

    private static volatile RecipeIngredientLocalDataSource INSTANCE;
    private AppExecutors executors;
    private RecipeIngredientEntityDao dao;

    private RecipeIngredientLocalDataSource(@Nonnull AppExecutors executors,
                                            @Nonnull RecipeIngredientEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeIngredientLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeIngredientEntityDao dao) {
        if (INSTANCE == null) {
            synchronized (RecipeIngredientLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeIngredientLocalDataSource(appExecutors, dao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final RecipeIngredientEntity entity = dao.getByDataId(dataId);
            executors.mainThread().execute(() -> {
                        if (entity != null) {
                            callback.onEntityLoaded(entity);
                        } else {
                            callback.onDataUnavailable();
                        }
                    }
            );
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllPrimitiveCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAllByRecipeIngredientId(domainId);
            executors.mainThread().execute(() -> {
                        if (entities.isEmpty()) {
                            callback.onDataUnavailable();
                        } else {
                            callback.onAllLoaded(entities);
                        }
                    }
            );
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllPrimitiveCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAllByRecipeId(recipeId);
            executors.mainThread().execute(() -> {
                        if (entities.isEmpty())
                            callback.onDataUnavailable();
                        else
                            callback.onAllLoaded(entities);
                    }
            );
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByIngredientId(
            @Nonnull String ingredientId,
            @Nonnull GetAllPrimitiveCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAllByIngredientId(ingredientId);
            executors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onAllLoaded(entities);
                else
                    callback.onAllLoaded(entities);
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByProductId(
            @Nonnull String productDomainId,
            @Nonnull GetAllPrimitiveCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAllByProductId(productDomainId);
            executors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onAllLoaded(entities);
                else
                    callback.onAllLoaded(entities);
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAll();
            executors.mainThread().execute(() -> {
                        if (entities.isEmpty()) {
                            callback.onAllLoaded(entities);
                        } else {
                            callback.onAllLoaded(entities);
                        }
                    }
            );
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull RecipeIngredientEntity entity) {
        Runnable runnable = () -> dao.insert(entity);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable runnable = () -> dao.deleteByDataId(dataId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable runnable = () -> dao.deleteByDataId(domainId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        executors.diskIO().execute(runnable);
    }
}
