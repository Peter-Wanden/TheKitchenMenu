package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSourceParent;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeCourseParentLocalDataSource
        implements PrimitiveDataSourceParent<RecipeCourseParentEntity> {

    private static volatile RecipeCourseParentLocalDataSource INSTANCE;

    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeCourseParentEntityDao dao;

    public RecipeCourseParentLocalDataSource(@Nonnull AppExecutors executors,
                                             @Nonnull RecipeCourseParentEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeCourseParentLocalDataSource getInstance(
            @Nonnull AppExecutors executors,
            @Nonnull RecipeCourseParentEntityDao dao) {

        if (INSTANCE == null) {
            synchronized (RecipeCourseItemLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeCourseParentLocalDataSource(executors, dao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllPrimitiveCallback<RecipeCourseParentEntity> callback) {
        Runnable r = () -> {
            final List<RecipeCourseParentEntity> entities = dao.getAllByDomainId(domainId);
            executors.mainThread().execute(() -> {
                if (entities.isEmpty()) {
                    callback.onDataUnavailable();
                } else {
                    callback.onAllLoaded(entities);
                }
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeCourseParentEntity> callback) {
        Runnable r = () -> {
            final List<RecipeCourseParentEntity> entities = dao.getAll();
            executors.mainThread().execute(() -> {
                if (entities.isEmpty()) {
                    callback.onDataUnavailable();
                } else {
                    callback.onAllLoaded(entities);
                }
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<RecipeCourseParentEntity> callback) {
        Runnable r = () -> {
            final RecipeCourseParentEntity e = dao.getByDataId(dataId);
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
    public void save(@Nonnull RecipeCourseParentEntity e) {
        Runnable r = () -> dao.insert(e);
        executors.diskIO().execute(r);
    }

    @Override
    public void refreshData() {
        // Not required as the Repository handles the logic of refreshing the
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
