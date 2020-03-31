package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeCourse;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeCourseLocalDataSource implements DataSourceRecipeCourse {

    private static volatile RecipeCourseLocalDataSource INSTANCE;

    @Nonnull
    private AppExecutors executors;
    @Nonnull
    private RecipeCourseEntityDao dao;

    private RecipeCourseLocalDataSource(@Nonnull AppExecutors executors,
                                        @Nonnull RecipeCourseEntityDao dao) {
        this.executors = executors;
        this.dao = dao;
    }

    public static RecipeCourseLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull RecipeCourseEntityDao recipeCourseEntityDao) {
        if (INSTANCE == null) {
            synchronized (RecipeCourseLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeCourseLocalDataSource(appExecutors, recipeCourseEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllByCourseNo(int courseNo,
                                 @Nonnull GetAllDomainModelsCallback<RecipeCourseEntity> c) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> e = dao.getAllByCourseNo(courseNo);
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
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllDomainModelsCallback<RecipeCourseEntity> c) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> e = dao.getAllByRecipeId(recipeId);
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
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeCourseEntity> c) {
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
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final RecipeCourseEntity e = dao.getById(id);
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
    public void deleteById(@Nonnull String id) {
        Runnable r = () -> dao.deleteByCourseId(id);
        executors.diskIO().execute(r);
    }
}
