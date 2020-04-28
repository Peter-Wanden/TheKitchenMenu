package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceChild;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeFailReasonsLocalDataSource
        implements PrimitiveDataSourceChild<RecipeFailReasonEntity> {

    private static volatile RecipeFailReasonsLocalDataSource INSTANCE;

    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeFailReasonDao dao;

    private RecipeFailReasonsLocalDataSource(@Nonnull AppExecutors executors,
                                             @Nonnull RecipeFailReasonDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeFailReasonsLocalDataSource getInstance(
            @Nonnull AppExecutors executors,
            @Nonnull RecipeFailReasonDao dao) {

        if (INSTANCE == null) {
            synchronized (RecipeFailReasonsLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeFailReasonsLocalDataSource(executors, dao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeFailReasonEntity> c) {
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
                                     @Nonnull GetAllPrimitiveCallback<RecipeFailReasonEntity> c) {
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
                            @Nonnull GetPrimitiveCallback<RecipeFailReasonEntity> c) {
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
    public void save(@Nonnull RecipeFailReasonEntity[] entities) {
        Runnable r = () -> dao.insert(entities);
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
