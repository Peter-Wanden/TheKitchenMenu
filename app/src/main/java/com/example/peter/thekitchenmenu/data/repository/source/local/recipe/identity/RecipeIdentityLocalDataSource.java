package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource;

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
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeIdentityEntity> callback) {
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
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = ()-> {
            final RecipeIdentityEntity recipeIdentityEntity = recipeIdentityEntityDao.getById(dataId);
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
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable runnable = ()-> recipeIdentityEntityDao.deleteById(dataId);
        appExecutors.diskIO().execute(runnable);
    }
}
