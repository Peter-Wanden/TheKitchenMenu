package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipePortions;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipePortionsLocalDomainDataAccess implements DomainDataAccessRecipePortions {

    private static volatile RecipePortionsLocalDomainDataAccess INSTANCE;
    private AppExecutors appExecutors;
    private RecipePortionsEntityDao dao;

    private RecipePortionsLocalDomainDataAccess(@Nonnull AppExecutors appExecutors,
                                                @Nonnull RecipePortionsEntityDao dao) {
        this.appExecutors = appExecutors;
        this.dao = dao;
    }

    public static RecipePortionsLocalDomainDataAccess getInstance(@Nonnull AppExecutors appExecutors,
                                                                  @Nonnull RecipePortionsEntityDao dao) {
        if (INSTANCE == null) {
            synchronized (RecipePortionsLocalDomainDataAccess.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipePortionsLocalDomainDataAccess(appExecutors, dao);
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
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipePortionsEntity> entities = dao.getAll();
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
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable runnable = () -> dao.deleteById(domainId);
        appExecutors.diskIO().execute(runnable);
    }
}
