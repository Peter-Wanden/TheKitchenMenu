package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSourceChild;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeFailReasonsLocalDataSource
        implements PrimitiveDataSourceChild<RecipeFailReasonEntity> {

    private static volatile RecipeFailReasonsLocalDataSource INSTANCE;

    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeFailReasonEntityDao dao;

    private RecipeFailReasonsLocalDataSource(@Nonnull AppExecutors executors,
                                             @Nonnull RecipeFailReasonEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeFailReasonsLocalDataSource getInstance(
            @Nonnull AppExecutors executors,
            @Nonnull RecipeFailReasonEntityDao dao) {

        if (INSTANCE == null) {
            synchronized (RecipeFailReasonsLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeFailReasonsLocalDataSource(executors, dao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeFailReasonEntity> c) {
        Runnable r = () -> {
            final List<RecipeFailReasonEntity> e = dao.getAll();
            executors.mainThread().execute(() -> {
                if (e.isEmpty())
                    c.onDataUnavailable();
                else
                    c.onAllLoaded(e);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAllByParentDataId(@Nonnull String parentDataId,
                                     @Nonnull GetAllCallback<RecipeFailReasonEntity> c) {
        Runnable r = () -> {
            final List<RecipeFailReasonEntity> e = dao.getAllByParentDataId(parentDataId);
            executors.mainThread().execute(() -> {
                if (e.isEmpty())
                    c.onDataUnavailable();
                else
                    c.onAllLoaded(e);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetEntityCallback<RecipeFailReasonEntity> c) {
        Runnable r = () -> {
            final RecipeFailReasonEntity e = dao.getByDataId(dataId);
            executors.mainThread().execute(() -> {
                if (e != null)
                    c.onEntityLoaded(e);
                else
                    c.onDataUnavailable();
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void save(@Nonnull RecipeFailReasonEntity e) {
        Runnable r = () -> dao.insert(e);
        executors.diskIO().execute(r);
    }

    @Override
    public void saveAll(@Nonnull RecipeFailReasonEntity... entities) {
        Runnable r = () -> dao.insertAll(entities);
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
    public void deleteAllByParentId(@Nonnull String parentDataId) {
        Runnable r = () -> dao.deleteAllByParentDataId(parentDataId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAll() {
        Runnable r = () -> dao.deleteAll();
        executors.diskIO().execute(r);
    }
}
