package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceParent;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIdentityLocalDataSource
        implements PrimitiveDataSourceParent<RecipeIdentityEntity> {

    private static volatile RecipeIdentityLocalDataSource INSTANCE;
    private RecipeIdentityEntityDao dao;
    private AppExecutors executors;

    private RecipeIdentityLocalDataSource(
            @Nonnull AppExecutors executors,
            @Nonnull RecipeIdentityEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeIdentityLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeIdentityEntityDao dao) {

        if (INSTANCE == null) {
            synchronized (RecipeIdentityLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeIdentityLocalDataSource(
                            appExecutors,
                            dao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = () -> {
            final RecipeIdentityEntity recipeIdentityEntity = dao.getByDataId(dataId);
            executors.mainThread().execute(() -> {
                if (recipeIdentityEntity != null) {
                    callback.onEntityLoaded(recipeIdentityEntity);
                } else {
                    callback.onDataUnavailable();
                }
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllPrimitiveCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIdentityEntity> entities =
                    dao.getAllByDomainId(domainId);
            if (entities.isEmpty()) {
                callback.onDataUnavailable();
            } else {
                callback.onAllLoaded(entities);
            }
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeIdentityEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeIdentityEntity> entities = dao.getAll();
            executors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull RecipeIdentityEntity entity) {
        Runnable runnable = () -> dao.insert(entity);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable runnable = () -> dao.deleteById(dataId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable runnable = () -> dao.deleteAllByDomainId(domainId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        executors.diskIO().execute(runnable);
    }
}
