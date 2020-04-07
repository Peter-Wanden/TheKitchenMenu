package com.example.peter.thekitchenmenu.data.repository.recipe;


import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends Repository<RecipeCourseModelPersistence>
        implements DataAccessRecipeCourse {

    private RepositoryRecipeCourse(@Nonnull DataAccessRecipeCourse remoteDataSource,
                                   @Nonnull DataAccessRecipeCourse localDataSource) {
        this.remoteDataAccess = remoteDataSource;
        this.localDataAccess = localDataSource;
    }

    public static RepositoryRecipeCourse getInstance(DataAccessRecipeCourse remoteDataSource,
                                                     DataAccessRecipeCourse localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeCourse(remoteDataSource, localDataSource);
        }
        return (RepositoryRecipeCourse) INSTANCE;
    }

    @Override
    public void getAllByCourse(
            RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {

        List<RecipeCourseModelPersistence> models = checkCacheForCourse(c);

        if (models != null) {
            callback.onAllLoaded(models);
            return;
        }
        ((DataAccessRecipeCourse) localDataAccess).getAllByCourse(
                c,
                new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipeCourseModelPersistence model : models) {
                            cache.put(model.getDataId(), model);
                        }
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeCourse) remoteDataAccess).getAllByCourse(
                                c,
                                new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                                        if (models == null) {
                                            onModelsUnavailable();
                                            return;
                                        }
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipeCourseModelPersistence model : models) {
                                            cache.put(model.getDataId(), model);
                                        }
                                        callback.onAllLoaded(models);
                                    }

                                    @Override
                                    public void onModelsUnavailable() {
                                        callback.onModelsUnavailable();
                                    }
                                }
                        );
                    }
                }
        );
    }

    private List<RecipeCourseModelPersistence> checkCacheForCourse(RecipeCourse.Course c) {
        List<RecipeCourseModelPersistence> models = new ArrayList<>();
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (RecipeCourseModelPersistence model : cache.values()) {
                if (c == model.getCourse()) {
                    models.add(model);
                }
            }
            return models.isEmpty() ? null : models;
        }
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {

        List<RecipeCourseModelPersistence> models = checkCacheForRecipeId(domainId);

        if (models != null) {
            callback.onAllLoaded(models);
            return;
        }
        ((DataAccessRecipeCourse) localDataAccess).getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipeCourseModelPersistence model : models) {
                            cache.put(model.getDataId(), model);
                        }
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeCourse) remoteDataAccess).getAllByDomainId(
                                domainId,
                                new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                                        if (models == null) {
                                            onModelsUnavailable();
                                            return;
                                        }
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipeCourseModelPersistence model : models) {
                                            cache.put(model.getDataId(), model);
                                        }
                                        callback.onAllLoaded(models);
                                    }

                                    @Override
                                    public void onModelsUnavailable() {
                                        callback.onModelsUnavailable();
                                    }
                                });
                    }
                });
    }

    private List<RecipeCourseModelPersistence> checkCacheForRecipeId(String recipeId) {
        List<RecipeCourseModelPersistence> models = new ArrayList<>();
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (RecipeCourseModelPersistence model : cache.values()) {
                if (model.getDataId().equals(recipeId)) {
                    models.add(model);
                }
            }
            return models.isEmpty() ? null : models;
        }
    }

    @Override
    public void update(@Nonnull RecipeCourseModelPersistence model) {
        remoteDataAccess.update(model);
        localDataAccess.update(model);
        cache.put(model.getDataId(), model);
    }
}