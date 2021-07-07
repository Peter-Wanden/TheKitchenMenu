package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceParent;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipePortionsLocalDataSource
        implements PrimitiveDataSourceParent<RecipePortionsEntity> {

    private static volatile RecipePortionsLocalDataSource INSTANCE;
    private AppExecutors executors;
    private RecipePortionsEntityDao dao;

    private RecipePortionsLocalDataSource(
            @Nonnull AppExecutors executors,
            @Nonnull RecipePortionsEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipePortionsLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
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
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetPrimitiveCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final RecipePortionsEntity entity = dao.getByDataId(dataId);
            executors.mainThread().execute(() -> {
                if (entity != null)
                    callback.onEntityLoaded(entity);
                else
                    callback.onDataUnavailable();
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllPrimitiveCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipePortionsEntity> entities = dao.getByDomainId(domainId);
            executors.mainThread().execute(() -> {
                if (entities != null)
                    callback.onAllLoaded(entities);
                else
                    callback.onDataUnavailable();
            });
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipePortionsEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipePortionsEntity> entities = dao.getAll();
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
    public void save(@Nonnull RecipePortionsEntity entity) {
        Runnable runnable = () -> dao.insert(entity);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable runnable = () -> dao.deleteByDataId(dataId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable runnable = () -> dao.deleteByDataId(domainId);
        executors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        executors.diskIO().execute(runnable);
    }
}
