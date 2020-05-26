package com.example.peter.thekitchenmenu.data.repository.recipe;


import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends Repository<RecipeCoursePersistenceModel>
        implements DomainDataAccessRecipeCourse {

    public static RepositoryRecipeCourse INSTANCE = null;

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
        return INSTANCE;
    }

    @Override
    public void getAllByCourse(
            @Nonnull RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {

        List<RecipeCoursePersistenceModel> models = checkCacheForCourse(c);

        if (models != null) {
            callback.onAllLoaded(models);
            return;
        }
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).getAllByCourse(
                c,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipeCoursePersistenceModel model : models) {
                            cache.put(model.getDataId(), model);
                        }
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).getAllByCourse(
                                c,
                                new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                                        if (models == null) {
                                            onModelsUnavailable();
                                            return;
                                        }
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipeCoursePersistenceModel model : models) {
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

    private List<RecipeCoursePersistenceModel> checkCacheForCourse(RecipeCourse.Course c) {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (RecipeCoursePersistenceModel model : cache.values()) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {

        List<RecipeCoursePersistenceModel> models = checkCacheForRecipeId(domainId);

        if (models != null) {
            callback.onAllLoaded(models);
            return;
        }
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipeCoursePersistenceModel model : models) {
                            cache.put(model.getDataId(), model);
                        }
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).getAllByDomainId(
                                domainId,
                                new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                                        if (models == null) {
                                            onModelsUnavailable();
                                            return;
                                        }
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipeCoursePersistenceModel model : models) {
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

    private List<RecipeCoursePersistenceModel> checkCacheForRecipeId(String recipeId) {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (RecipeCoursePersistenceModel model : cache.values()) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {
        List<RecipeCoursePersistenceModel> activeModels = new ArrayList<>();
        getAllByDomainId(domainId, new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
            @Override
            public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                for (RecipeCoursePersistenceModel m : models) {
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
    public void update(@Nonnull RecipeCoursePersistenceModel model) {
        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).update(model);
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).update(model);
        cache.put(model.getDataId(), model);
    }
}