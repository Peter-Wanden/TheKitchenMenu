package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeCourse;

import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeCourseLocalDataSource implements DataSourceRecipeCourse {

    private static volatile RecipeCourseLocalDataSource INSTANCE;
    private RecipeCourseEntityDao recipeCourseEntityDao;
    private AppExecutors appExecutors;

    private RecipeCourseLocalDataSource(@Nonnull AppExecutors appExecutors,
                                        @Nonnull RecipeCourseEntityDao recipeCourseEntityDao) {
        this.appExecutors = appExecutors;
        this.recipeCourseEntityDao = recipeCourseEntityDao;
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
    public void getAllRecipesForCourseNo(int courseNo,
                                         @Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeCourseEntity> recipeCourseEntities =
                    recipeCourseEntityDao.getAllRecipesForCourseNo(courseNo);
            if (recipeCourseEntities.isEmpty())
                callback.onDataNotAvailable();
            else
                callback.onAllLoaded(recipeCourseEntities);
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getCoursesForRecipe(@Nonnull String recipeId,
                                    @Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeCourseEntity> recipeCourseEntities =
                    recipeCourseEntityDao.getCoursesForRecipe(recipeId);
            if (recipeCourseEntities.isEmpty())
                callback.onDataNotAvailable();
            else
                callback.onAllLoaded(recipeCourseEntities);
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeCourseEntity> recipeCourseEntities = recipeCourseEntityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (recipeCourseEntities.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(recipeCourseEntities);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeCourseEntity> callback) {
        Runnable runnable = () -> {
            final RecipeCourseEntity recipeCourseEntity = recipeCourseEntityDao.getById(id);
            appExecutors.mainThread().execute(() -> {
                if (recipeCourseEntity != null)
                    callback.onEntityLoaded(recipeCourseEntity);
                else
                    callback.onDataUnavailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@Nonnull RecipeCourseEntity entity) {
        Runnable runnable = () -> recipeCourseEntityDao.insert(entity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> recipeCourseEntityDao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteById(@Nonnull String id) {
        Runnable runnable = () -> recipeCourseEntityDao.deleteByCourseId(id);
        appExecutors.diskIO().execute(runnable);
    }
}
