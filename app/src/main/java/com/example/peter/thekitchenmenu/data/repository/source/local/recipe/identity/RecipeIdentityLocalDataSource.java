package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIdentityLocalDataSource implements PrimitiveDataSource<RecipeIdentityEntity> {

    private static volatile RecipeIdentityLocalDataSource INSTANCE;
    private RecipeIdentityEntityDao recipeIdentityEntityDao;
    private AppExecutors appExecutors;

    private RecipeIdentityLocalDataSource(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeIdentityEntityDao recipeIdentityEntityDao) {
        this.appExecutors = appExecutors;
        this.recipeIdentityEntityDao = recipeIdentityEntityDao;
    }

    public static RecipeIdentityLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeIdentityEntityDao recipeIdentityEntityDao) {
        if (INSTANCE == null) {
            synchronized (RecipeIdentityLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeIdentityLocalDataSource(
                            appExecutors,
                            recipeIdentityEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIdentityEntity> entities = recipeIdentityEntityDao.gatAll();
            appExecutors.mainThread().execute(() -> {
                if(entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = ()-> {
            final RecipeIdentityEntity recipeIdentityEntity = recipeIdentityEntityDao.getById(id);
            appExecutors.mainThread().execute(() -> {
                if (recipeIdentityEntity != null)
                    callback.onEntityLoaded(recipeIdentityEntity);
                else
                    callback.onDataUnavailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull RecipeIdentityEntity entity) {
        Runnable runnable = ()-> recipeIdentityEntityDao.insert(entity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = ()-> recipeIdentityEntityDao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteById(@Nonnull String id) {
        Runnable runnable = ()-> recipeIdentityEntityDao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
