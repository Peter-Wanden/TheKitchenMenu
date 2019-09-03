package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeCourse;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class LocalDataSourceRecipeCourse implements DataSourceRecipeCourse {

    private static volatile LocalDataSourceRecipeCourse INSTANCE;
    private RecipeCourseEntityDao recipeCourseEntityDao;
    private AppExecutors appExecutors;

    private LocalDataSourceRecipeCourse(@NonNull AppExecutors appExecutors,
                                        @NonNull RecipeCourseEntityDao recipeCourseEntityDao) {
        this.appExecutors = appExecutors;
        this.recipeCourseEntityDao = recipeCourseEntityDao;
    }

    public static LocalDataSourceRecipeCourse getInstance(
            @NonNull AppExecutors appExecutors,
            @NonNull RecipeCourseEntityDao recipeCourseEntityDao) {
        if (INSTANCE == null) {
            synchronized (LocalDataSourceRecipeCourse.class) {
                if (INSTANCE == null)
                    INSTANCE = new LocalDataSourceRecipeCourse(appExecutors, recipeCourseEntityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllRecipesForCourseNo(int courseNo,
                                         @NonNull GetAllCallback<RecipeCourseEntity> callback) {
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
    public void getCoursesForRecipe(@NonNull String recipeId,
                                    @NonNull GetAllCallback<RecipeCourseEntity> callback) {
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
    public void getAll(@NonNull GetAllCallback<RecipeCourseEntity> callback) {
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
    public void getById(@NonNull String id,
                        @NonNull GetEntityCallback<RecipeCourseEntity> callback) {
        Runnable runnable = () -> {
            final RecipeCourseEntity recipeCourseEntity = recipeCourseEntityDao.getById(id);
            appExecutors.mainThread().execute(() -> {
                if (recipeCourseEntity != null)
                    callback.onEntityLoaded(recipeCourseEntity);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@NonNull RecipeCourseEntity recipeCourseEntity) {
        checkNotNull(recipeCourseEntity);
        Runnable runnable = () -> recipeCourseEntityDao.insert(recipeCourseEntity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> recipeCourseEntityDao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteById(@NonNull String id) {
        Runnable runnable = () -> recipeCourseEntityDao.deleteByCourseId(id);
        appExecutors.diskIO().execute(runnable);
    }
}
