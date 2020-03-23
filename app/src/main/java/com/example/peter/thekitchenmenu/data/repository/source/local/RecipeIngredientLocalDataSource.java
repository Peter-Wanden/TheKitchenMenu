package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeIngredient;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeIngredientLocalDataSource implements DataSourceRecipeIngredient {

    private static volatile RecipeIngredientLocalDataSource INSTANCE;
    private AppExecutors appExecutors;
    private RecipeIngredientEntityDao dao;

    private RecipeIngredientLocalDataSource(@Nonnull AppExecutors appExecutors,
                                            @Nonnull RecipeIngredientEntityDao dao) {
        this.appExecutors = appExecutors;
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
    public void getAll(@Nonnull GetAllCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getByRecipeId(recipeId);
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByProductId(@Nonnull String productId,
                               @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getByProductId(productId);
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByIngredientId(@Nonnull String ingredientId,
                                  @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIngredientEntity> entities = dao.getByIngredientId(ingredientId);
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataNotAvailable();
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
    public void deleteById(@Nonnull String id) {
        Runnable runnable = () -> dao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
