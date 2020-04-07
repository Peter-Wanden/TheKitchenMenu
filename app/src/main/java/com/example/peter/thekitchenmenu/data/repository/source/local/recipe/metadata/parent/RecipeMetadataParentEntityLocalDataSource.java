package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSourceParent;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataParentEntityLocalDataSource
        implements PrimitiveDataSourceParent<RecipeMetadataParentEntity> {

    private static volatile RecipeMetadataParentEntityLocalDataSource INSTANCE;

    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeMetadataParentEntityDao dao;

    private RecipeMetadataParentEntityLocalDataSource(@Nonnull AppExecutors executors,
                                                      @Nonnull RecipeMetadataParentEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeMetadataParentEntityLocalDataSource getInstance(
            @Nonnull AppExecutors executors,
            @Nonnull RecipeMetadataParentEntityDao dao) {

        if (INSTANCE == null) {
            synchronized (RecipeMetadataParentEntityLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeMetadataParentEntityLocalDataSource(executors, dao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllByDomainId(@Nonnull String domainId,
                                 @Nonnull GetAllPrimitiveCallback<RecipeMetadataParentEntity> callback) {
        Runnable r = () -> {
            final List<RecipeMetadataParentEntity> entities = dao.getAllByDomainId(domainId);
            executors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeMetadataParentEntity> callback) {
        Runnable r = () -> {
            final List<RecipeMetadataParentEntity> entities = dao.getAll();
            executors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<RecipeMetadataParentEntity> callback) {
        Runnable r = () -> {
            final RecipeMetadataParentEntity e = dao.getByDataId(dataId);
            executors.mainThread().execute(() -> {
                if (e != null)
                    callback.onEntityLoaded(e);
                else
                    callback.onDataUnavailable();
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void save(@Nonnull RecipeMetadataParentEntity e) {
        Runnable r = () -> dao.insert(e);
        executors.diskIO().execute(r);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable r = () -> dao.deleteByDataId(dataId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable r = () -> dao.deleteAllByDomainId(domainId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAll() {
        Runnable r = () -> dao.deleteAll();
        executors.diskIO().execute(r);
    }
}
