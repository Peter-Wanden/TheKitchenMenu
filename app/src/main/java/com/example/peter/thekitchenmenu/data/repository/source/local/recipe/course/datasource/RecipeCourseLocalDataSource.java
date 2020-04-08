package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.RecipeCoursePrimitiveDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeCourseLocalDataSource
        implements RecipeCoursePrimitiveDataSource {

    private static volatile RecipeCourseLocalDataSource INSTANCE;

    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeCourseDao dao;

    private RecipeCourseLocalDataSource(@Nonnull AppExecutors executors,
                                        @Nonnull RecipeCourseDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeCourseLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeCourseDao recipeCourseDao) {
        if (INSTANCE == null) {
            synchronized (RecipeCourseLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeCourseLocalDataSource(appExecutors, recipeCourseDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllByCourseNo(
            int courseNo,
            @Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> c) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> e = dao.getAllByCourseNo(courseNo);
            executors.mainThread().execute(() -> {
                if (e.isEmpty()) {
                    c.onDataUnavailable();
                } else {
                    c.onAllLoaded(e);
                }
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAllByDomainId(
            String domainId,
            @Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> c) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> e = dao.getAllByRecipeId(domainId);
            executors.mainThread().execute(() -> {
                if (e.isEmpty()) {
                    c.onDataUnavailable();
                } else {
                    c.onAllLoaded(e);
                }
            });
        };
        executors.diskIO().execute(r);
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<RecipeCourseEntity> c) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> e = dao.getAll();
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
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetPrimitiveCallback<RecipeCourseEntity> c) {
        Runnable r = () -> {
            final RecipeCourseEntity e = dao.getByDataId(dataId);
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
    public void save(@Nonnull RecipeCourseEntity entity) {
        Runnable r = () -> dao.insert(entity);
        executors.diskIO().execute(r);
    }

    @Override
    public void update(@Nonnull RecipeCourseEntity e) {
        Runnable r = () -> dao.update(e);
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
    public void deleteAllByDomainId(@Nonnull String domainId) {
        Runnable r = () -> dao.deleteAllByDomainId(domainId);
        executors.diskIO().execute(r);
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        Runnable r = () -> dao.deleteByDataId(dataId);
        executors.diskIO().execute(r);
    }
}
