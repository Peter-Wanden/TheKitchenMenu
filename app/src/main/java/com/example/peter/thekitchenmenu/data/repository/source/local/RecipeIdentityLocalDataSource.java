package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeIdentity;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeIdentityLocalDataSource implements DataSourceRecipeIdentity {

    private static volatile RecipeIdentityLocalDataSource INSTANCE;
    private RecipeIdentityEntityDao recipeIdentityEntityDao;
    private AppExecutors appExecutors;

    private RecipeIdentityLocalDataSource(
            @NonNull AppExecutors appExecutors,
            @NonNull RecipeIdentityEntityDao recipeIdentityEntityDao) {
        this.appExecutors = appExecutors;
        this.recipeIdentityEntityDao = recipeIdentityEntityDao;
    }

    public static RecipeIdentityLocalDataSource getInstance(
            @NonNull AppExecutors appExecutors,
            @NonNull RecipeIdentityEntityDao recipeIdentityEntityDao) {
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
    public void getAll(@NonNull GetAllCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIdentityEntity> recipeIdentityEntityList =
                    recipeIdentityEntityDao.gatAll();
            appExecutors.mainThread().execute(() -> {
                if(recipeIdentityEntityList.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(recipeIdentityEntityList);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@NonNull String id,
                        @NonNull GetEntityCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = ()-> {
            final RecipeIdentityEntity recipeIdentityEntity = recipeIdentityEntityDao.getById(id);
            appExecutors.mainThread().execute(() -> {
                if (recipeIdentityEntity != null)
                    callback.onEntityLoaded(recipeIdentityEntity);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getByRecipeId(@NonNull String recipeId,
                              @NonNull GetEntityCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = ()-> {
            final RecipeIdentityEntity recipeIdentityEntity =
                    recipeIdentityEntityDao.getByRecipeId(recipeId);
            appExecutors.mainThread().execute(()-> {
                if (recipeIdentityEntity != null)
                    callback.onEntityLoaded(recipeIdentityEntity);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@NonNull RecipeIdentityEntity recipeIdentityEntity) {
        checkNotNull(recipeIdentityEntity);
        Runnable runnable = ()-> recipeIdentityEntityDao.insert(recipeIdentityEntity);
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
    public void deleteById(@NonNull String id) {
        Runnable runnable = ()-> recipeIdentityEntityDao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
