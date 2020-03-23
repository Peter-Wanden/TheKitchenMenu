package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeLocalDataSource implements PrimitiveDataSource<RecipeMetadataEntity> {

    private static volatile RecipeLocalDataSource INSTANCE;
    private RecipeEntityDao recipeEntityDao;
    private AppExecutors appExecutors;

    private RecipeLocalDataSource(@Nonnull AppExecutors appExecutors,
                                  @Nonnull RecipeEntityDao recipeEntityDao) {
        this.appExecutors = appExecutors;
        this.recipeEntityDao = recipeEntityDao;
    }

    public static RecipeLocalDataSource getInstance(@Nonnull AppExecutors appExecutors,
                                                    @Nonnull RecipeEntityDao recipeEntityDao) {
        if (INSTANCE == null) {
            synchronized (RecipeLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeLocalDataSource(appExecutors, recipeEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeMetadataEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeMetadataEntity> recipeMetadataEntityList = recipeEntityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (recipeMetadataEntityList.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(recipeMetadataEntityList);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@Nonnull String recipeId,
                        @Nonnull GetEntityCallback<RecipeMetadataEntity> callback) {
        Runnable runnable = () -> {
            final RecipeMetadataEntity recipeMetadataEntity = recipeEntityDao.getById(recipeId);
            appExecutors.mainThread().execute(() -> {
                if (recipeMetadataEntity != null)
                    callback.onEntityLoaded(recipeMetadataEntity);
                else
                    callback.onDataUnavailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull RecipeMetadataEntity entity) {
        Runnable runnable = () -> recipeEntityDao.insert(entity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> recipeEntityDao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteById(@Nonnull final String id) {
        Runnable runnable = () -> recipeEntityDao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
