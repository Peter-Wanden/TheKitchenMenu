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
        implements DomainDataAccessRecipeCourse {

    private RepositoryRecipeCourse(@Nonnull DomainDataAccessRecipeCourse remoteDataSource,
                                   @Nonnull DomainDataAccessRecipeCourse localDataSource) {
        this.remoteDomainDataAccess = remoteDataSource;
        this.localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipeCourse getInstance(DomainDataAccessRecipeCourse remoteDataSource,
                                                     DomainDataAccessRecipeCourse localDataSource) {
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
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).getAllByCourse(
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
                        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).getAllByCourse(
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
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).getAllByDomainId(
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
                        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).getAllByDomainId(
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
    public void getAllActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        List<RecipeCourseModelPersistence> activeModels = new ArrayList<>();
        getAllByDomainId(domainId, new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
            @Override
            public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                for (RecipeCourseModelPersistence m : models) {
                    if (m.isActive()) {
                        activeModels.add(m);
                    }
                }
                if (activeModels.isEmpty()) {
                    callback.onModelsUnavailable();
                } else {
                    callback.onAllLoaded(activeModels);
                }
            }

            @Override
            public void onModelsUnavailable() {
                callback.onModelsUnavailable();
            }
        });
    }

    @Override
    public void update(@Nonnull RecipeCourseModelPersistence model) {
        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).update(model);
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).update(model);
        cache.put(model.getDataId(), model);
    }
}