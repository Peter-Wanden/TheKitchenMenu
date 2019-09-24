package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipePortions;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class LocalDataSourceRecipePortions implements DataSourceRecipePortions {

    private static volatile LocalDataSourceRecipePortions INSTANCE;
    private AppExecutors appExecutors;
    private RecipePortionsEntityDao dao;

    private LocalDataSourceRecipePortions(@NonNull AppExecutors appExecutors,
                                          @NonNull RecipePortionsEntityDao dao) {
        this.appExecutors = appExecutors;
        this.dao = dao;
    }

    public static LocalDataSourceRecipePortions getInstance(@NonNull AppExecutors appExecutors,
                                                            @NonNull RecipePortionsEntityDao dao) {
        if (INSTANCE == null) {
            synchronized (LocalDataSourceRecipePortions.class) {
                if (INSTANCE == null)
                    INSTANCE = new LocalDataSourceRecipePortions(appExecutors, dao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getPortionsForRecipe(@NonNull String recipeId,
                                     @NonNull GetEntityCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final RecipePortionsEntity entity = dao.getByRecipeId(recipeId);
            appExecutors.mainThread().execute(() -> {
                if (entity != null)
                    callback.onEntityLoaded(entity);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getAll(@NonNull GetAllCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipePortionsEntity> entities = dao.getAll();
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
    public void getById(@NonNull String id, @NonNull GetEntityCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final RecipePortionsEntity entity = dao.getById(id);
            appExecutors.mainThread().execute(() -> {
                if (entity != null)
                    callback.onEntityLoaded(entity);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@NonNull RecipePortionsEntity entity) {
        checkNotNull(entity);
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
    public void deleteById(@NonNull String id) {
        Runnable runnable = () -> dao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
