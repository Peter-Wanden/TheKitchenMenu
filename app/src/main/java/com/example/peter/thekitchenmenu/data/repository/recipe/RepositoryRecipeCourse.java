package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryRecipeCourse
        extends Repository<RecipeCourseEntity>
        implements DataSourceRecipeCourse {

    private RepositoryRecipeCourse(@Nonnull DataSourceRecipeCourse remoteDataSource,
                                   @Nonnull DataSourceRecipeCourse localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryRecipeCourse getInstance(DataSourceRecipeCourse remoteDataSource,
                                                     DataSourceRecipeCourse localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeCourse(remoteDataSource, localDataSource);
        }
        return (RepositoryRecipeCourse) INSTANCE;
    }

    @Override
    public void getAllByCourseNo(int courseNo,
                                 @Nonnull GetAllCallback<RecipeCourseEntity> callback) {

        List<RecipeCourseEntity> recipeCourseEntities = checkCacheForCourseNo(courseNo);

        if (recipeCourseEntities != null) {
            callback.onAllLoaded(recipeCourseEntities);
            return;
        }
        ((DataSourceRecipeCourse)localDataSource).getAllByCourseNo(
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
                    public void onDataUnavailable() {
                        ((DataSourceRecipeCourse)remoteDataSource).getAllByCourseNo(
                                courseNo,
                                new GetAllCallback<RecipeCourseEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCourseEntity>
                                                                    courseEntities) {
                                        if (courseEntities == null) {
                                            onDataUnavailable();
                                            return;
                                        }

                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeCourseEntity courseEntity : courseEntities)
                                            entityCache.put(courseEntity.getId(), courseEntity);

                                        callback.onAllLoaded(courseEntities);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
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
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllCallback<RecipeCourseEntity> callback) {

        List<RecipeCourseEntity> recipeCourseEntities = checkCacheForRecipeId(recipeId);

        if (recipeCourseEntities != null) {
            callback.onAllLoaded(recipeCourseEntities);
            return;
        }
        ((DataSourceRecipeCourse)localDataSource).getAllByRecipeId(
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
                    public void onDataUnavailable() {
                        ((DataSourceRecipeCourse)remoteDataSource).getAllByRecipeId(
                                recipeId,
                                new GetAllCallback<RecipeCourseEntity>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCourseEntity>
                                                                    courseEntities) {
                                        if (courseEntities == null) {
                                            onDataUnavailable();
                                            return;
                                        }

                                        if (entityCache == null)
                                            entityCache = new LinkedHashMap<>();

                                        for (RecipeCourseEntity courseEntity : recipeCourseEntities)
                                            entityCache.put(courseEntity.getId(), courseEntity);

                                        callback.onAllLoaded(courseEntities);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
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
                if (recipeCourseEntity.getRecipeId().equals(recipeId)) {
                    recipeCourseEntities.add(recipeCourseEntity);
                }
            }
            return recipeCourseEntities.isEmpty() ? null : recipeCourseEntities;
        }
    }
}