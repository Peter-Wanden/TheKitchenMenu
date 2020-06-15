package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.RecipeCoursePrimitiveDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeCourseItemLocalDataSource
        implements RecipeCoursePrimitiveDataSource {

    private static volatile RecipeCourseItemLocalDataSource INSTANCE;

    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeCourseItemDao dao;

    private RecipeCourseItemLocalDataSource(@Nonnull AppExecutors executors,
                                            @Nonnull RecipeCourseItemDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeCourseItemLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeCourseItemDao recipeCourseItemDao) {

        if (INSTANCE == null) {
            synchronized (RecipeCourseItemLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeCourseItemLocalDataSource(appExecutors, recipeCourseItemDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> entities = dao.getAll();
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
    public void getAllByCourseNo(
            int courseNo,
            @Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> entities = dao.getAllByCourseNo(courseNo);
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
    public void getAllByParentDataId(
            @Nonnull String parentDataId,
            @Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> e = dao.getAllByParentDataId(parentDataId);
            executors.mainThread().execute(() -> {
                if (e.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(e);
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetPrimitiveCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final RecipeCourseEntity e = dao.getByDataId(dataId);
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
    public void save(@Nonnull RecipeCourseEntity entity) {
        Runnable r = () -> dao.insert(entity);
        executors.diskIO().execute(r);
    }

    @Override
    public void save(@Nonnull RecipeCourseEntity... entities) {
        Runnable r = () -> dao.insert(entities);
        executors.diskIO().execute(r);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteAll() {
        Runnable r = () -> dao.deleteAll();
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteAllByParentDataId(@Nonnull String parentId) {
        Runnable r = () -> dao.deleteAllByParentDataId(parentId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable r = () -> dao.deleteByDataId(dataId);
        executors.diskIO().execute(r);
    }
}
