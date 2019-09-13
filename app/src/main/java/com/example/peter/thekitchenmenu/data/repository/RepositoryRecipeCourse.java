package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryRecipeCourse
        extends Repository<RecipeCourseEntity>
        implements DataSourceRecipeCourse {

    public static RepositoryRecipeCourse INSTANCE;
    private DataSourceRecipeCourse recipeCourseRemoteDataSource;
    private DataSourceRecipeCourse recipeCourseLocalDataSource;

    private RepositoryRecipeCourse(@NonNull DataSourceRecipeCourse remoteDataSource,
                                   @NonNull DataSourceRecipeCourse localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
        recipeCourseRemoteDataSource = checkNotNull(remoteDataSource);
        recipeCourseLocalDataSource = checkNotNull(localDataSource);
    }

    public static RepositoryRecipeCourse getInstance(DataSourceRecipeCourse remoteDataSource,
                                                     DataSourceRecipeCourse localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeCourse(remoteDataSource, localDataSource);
        return INSTANCE;
    }

    @Override
    public void getAllRecipesForCourseNo(int courseNo,
                                         @NonNull GetAllCallback<RecipeCourseEntity> callback) {
        checkNotNull(courseNo);
        checkNotNull(callback);

        List<RecipeCourseEntity> recipeCourseEntities = checkCacheForCourseNo(courseNo);

        if (recipeCourseEntities != null) {
            callback.onAllLoaded(recipeCourseEntities);
            return;
        }
        recipeCourseLocalDataSource.getAllRecipesForCourseNo(
                courseNo,
                new GetAllCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> courseEntities) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        for (RecipeCourseEntity entity : courseEntities)
                            entityCache.put(entity.getId(), entity);

                        callback.onAllLoaded(courseEntities);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        recipeCourseRemoteDataSource.getAllRecipesForCourseNo(
                                courseNo,
                                new GetAllCallback<RecipeCourseEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCourseEntity>
                                                                    courseEntities) {
                                        if (courseEntities == null) {
                                            onDataNotAvailable();
                                            return;
                                        }

                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeCourseEntity courseEntity : courseEntities)
                                            entityCache.put(courseEntity.getId(), courseEntity);

                                        callback.onAllLoaded(courseEntities);
                                    }

                                    @Override
                                    public void onDataNotAvailable() {
                                        callback.onDataNotAvailable();
                                    }
                                });
                    }
                });
    }

    private List<RecipeCourseEntity> checkCacheForCourseNo(int courseNo) {
        List<RecipeCourseEntity> recipeCourseEntityList = new ArrayList<>();
        if (entityCache == null || entityCache.isEmpty())
            return null;
        else {
            for (RecipeCourseEntity recipeCourseEntity : entityCache.values()) {
                if (recipeCourseEntity.getCourseNo() == courseNo)
                    recipeCourseEntityList.add(recipeCourseEntity);
            }
            return recipeCourseEntityList.isEmpty() ? null : recipeCourseEntityList;
        }
    }

    @Override
    public void getCoursesForRecipe(@NonNull String recipeId,
                                    @NonNull GetAllCallback<RecipeCourseEntity> callback) {
        checkNotNull(recipeId);
        checkNotNull(callback);

        List<RecipeCourseEntity> recipeCourseEntities = checkCacheForRecipeId(recipeId);

        if (recipeCourseEntities != null) {
            callback.onAllLoaded(recipeCourseEntities);
            return;
        }
        recipeCourseLocalDataSource.getCoursesForRecipe(
                recipeId,
                new GetAllCallback<RecipeCourseEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseEntity> courseEntities) {
                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        for (RecipeCourseEntity courseEntity : courseEntities)
                            entityCache.put(courseEntity.getId(), courseEntity);

                        callback.onAllLoaded(courseEntities);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        recipeCourseRemoteDataSource.getCoursesForRecipe(
                                recipeId,
                                new GetAllCallback<RecipeCourseEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCourseEntity>
                                                                    courseEntities) {
                                        if (courseEntities == null) {
                                            onDataNotAvailable();
                                            return;
                                        }

                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeCourseEntity courseEntity : recipeCourseEntities)
                                            entityCache.put(courseEntity.getId(), courseEntity);

                                        callback.onAllLoaded(courseEntities);
                                    }

                                    @Override
                                    public void onDataNotAvailable() {
                                        callback.onDataNotAvailable();
                                    }
                                });
                    }
                });
    }

    private List<RecipeCourseEntity> checkCacheForRecipeId(String recipeId) {
        List<RecipeCourseEntity> recipeCourseEntities = new ArrayList<>();
        if (entityCache == null || entityCache.isEmpty())
            return null;
        else {
            for (RecipeCourseEntity recipeCourseEntity : entityCache.values()) {
                if (recipeCourseEntity.getRecipeId().equals(recipeId))
                    recipeCourseEntities.add(recipeCourseEntity);
            }
            return recipeCourseEntities.isEmpty() ? null : recipeCourseEntities;
        }
    }
}