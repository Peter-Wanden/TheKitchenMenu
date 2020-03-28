package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipePortions;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipePortionsLocalDataSource implements DataSourceRecipePortions {

    private static volatile RecipePortionsLocalDataSource INSTANCE;
    private AppExecutors appExecutors;
    private RecipePortionsEntityDao dao;

    private RecipePortionsLocalDataSource(@Nonnull AppExecutors appExecutors,
                                          @Nonnull RecipePortionsEntityDao dao) {
        this.appExecutors = appExecutors;
        this.dao = dao;
    }

    public static RecipePortionsLocalDataSource getInstance(@Nonnull AppExecutors appExecutors,
                                                            @Nonnull RecipePortionsEntityDao dao) {
        if (INSTANCE == null) {
            synchronized (RecipePortionsLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipePortionsLocalDataSource(appExecutors, dao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetEntityCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final RecipePortionsEntity entity = dao.getByRecipeId(recipeId);
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
    public void getAll(@Nonnull GetAllCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipePortionsEntity> entities = dao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final RecipePortionsEntity entity = dao.getById(id);
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
    public void save(@Nonnull RecipePortionsEntity entity) {
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
