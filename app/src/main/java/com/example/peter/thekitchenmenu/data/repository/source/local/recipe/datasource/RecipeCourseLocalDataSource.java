package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.dao.RecipeCourseEntityDao;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeCourseLocalDataSource implements DataSourceRecipeCourse {

    private static volatile RecipeCourseLocalDataSource INSTANCE;

    @Nonnull
    private RecipeCourseEntityDao dao;
    @Nonnull
    private AppExecutors appExecutors;

    private RecipeCourseLocalDataSource(@Nonnull AppExecutors appExecutors,
                                        @Nonnull RecipeCourseEntityDao dao) {
        this.appExecutors = appExecutors;
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
                                 @Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> entities = dao.getAllByCourseNo(courseNo);
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(r);
    }

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> entities = dao.getAllByRecipeId(recipeId);
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(r);
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final List<RecipeCourseEntity> entities = dao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (entities.isEmpty())
                    callback.onDataUnavailable();
                else
                    callback.onAllLoaded(entities);
            });
        };
        appExecutors.diskIO().execute(r);
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeCourseEntity> callback) {
        Runnable r = () -> {
            final RecipeCourseEntity recipeCourseEntity = dao.getById(id);
            appExecutors.mainThread().execute(() -> {
                if (recipeCourseEntity != null)
                    callback.onEntityLoaded(recipeCourseEntity);
                else
                    callback.onDataUnavailable();
            });
        };
        appExecutors.diskIO().execute(r);
    }

    @Override
    public void save(@Nonnull RecipeCourseEntity entity) {
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
    public void deleteById(@Nonnull String id) {
        Runnable runnable = () -> dao.deleteByCourseId(id);
        appExecutors.diskIO().execute(runnable);
    }
}
