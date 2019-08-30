package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeLocalDataSource implements DataSource<RecipeEntity> {

    private static volatile RecipeLocalDataSource INSTANCE;
    private RecipeEntityDao recipeEntityDao;
    private AppExecutors appExecutors;

    private RecipeLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull RecipeEntityDao recipeEntityDao) {
        this.appExecutors = appExecutors;
        this.recipeEntityDao = recipeEntityDao;
    }

    public static RecipeLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    RecipeEntityDao recipeEntityDao) {
        if (INSTANCE == null) {
            synchronized (RecipeLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeLocalDataSource(appExecutors, recipeEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull GetAllCallback<RecipeEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeEntity> recipeEntityList = recipeEntityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (recipeEntityList.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(recipeEntityList);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@NonNull String recipeId, @NonNull GetEntityCallback<RecipeEntity> callback) {
        Runnable runnable = () -> {
            final RecipeEntity recipeEntity = recipeEntityDao.getById(recipeId);
            appExecutors.mainThread().execute(() -> {
                if (recipeEntity != null)
                    callback.onEntityLoaded(recipeEntity);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@NonNull RecipeEntity recipeEntity) {
        checkNotNull(recipeEntity);
        Runnable runnable = () -> recipeEntityDao.insert(recipeEntity);
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
    public void deleteById(@NonNull final String id) {
        Runnable runnable = () -> recipeEntityDao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
