package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.ingredient;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeIngredient;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeIngredientLocalDataAccess implements DataAccessRecipeIngredient {

    private static volatile RecipeIngredientLocalDataAccess INSTANCE;
    private AppExecutors appExecutors;
    private RecipeIngredientEntityDao dao;

    private RecipeIngredientLocalDataAccess(@Nonnull AppExecutors appExecutors,
                                            @Nonnull RecipeIngredientEntityDao dao) {
        this.appExecutors = appExecutors;
        this.dao = dao;
    }

    public static RecipeIngredientLocalDataAccess getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeIngredientEntityDao dao) {
        if (INSTANCE == null) {
            synchronized (RecipeIngredientLocalDataAccess.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeIngredientLocalDataAccess(appExecutors, dao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onModelsUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAllByRecipeId(recipeId);
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onModelsUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByProductId(@Nonnull String productId,
                                  @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAllByProductId(productId);
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onModelsUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByIngredientId(@Nonnull String ingredientId,
                                     @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAllByIngredientId(ingredientId);
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onModelsUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final RecipeIngredientEntity entity = dao.getById(id);
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
    public void save(@Nonnull RecipeIngredientEntity entity) {
        Runnable runnable = () -> dao.insert(entity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable runnable = () -> dao.deleteById(domainId);
        appExecutors.diskIO().execute(runnable);
    }
}
