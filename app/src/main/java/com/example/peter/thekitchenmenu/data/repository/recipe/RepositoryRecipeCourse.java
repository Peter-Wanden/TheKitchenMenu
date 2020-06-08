package com.example.peter.thekitchenmenu.data.repository.recipe;


import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends
        Repository<RecipeCoursePersistenceModelItem>
        implements
        DomainDataAccessRecipeCourse {

    protected static RepositoryRecipeCourse INSTANCE = null;

    private RepositoryRecipeCourse(@Nonnull DomainDataAccessRecipeCourse remoteDataSource,
                                   @Nonnull DomainDataAccessRecipeCourse localDataSource) {
        remoteDomainDataAccess = remoteDataSource;
        localDomainDataAccess = localDataSource;
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
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {

        List<RecipeCoursePersistenceModelItem> models = checkCacheForCourse(c);

        if (models != null) {
            callback.onAllDomainModelsLoaded(models);
            return;
        }
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).getAllByCourse(
                c,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<
                            RecipeCoursePersistenceModelItem> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipeCoursePersistenceModelItem model : models) {
                            cache.put(model.getDataId(), model);
                        }
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).getAllByCourse(
                                c,
                                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                                    @Override
                                    public void onAllDomainModelsLoaded(
                                            List<RecipeCoursePersistenceModelItem> models) {
                                        if (models == null) {
                                            onDomainModelsUnavailable();
                                            return;
                                        }
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipeCoursePersistenceModelItem model : models) {
                                            cache.put(model.getDataId(), model);
                                        }
                                        callback.onAllDomainModelsLoaded(models);
                                    }

                                    @Override
                                    public void onDomainModelsUnavailable() {
                                        callback.onDomainModelsUnavailable();
                                    }
                                }
                        );
                    }
                }
        );
    }

    private List<RecipeCoursePersistenceModelItem> checkCacheForCourse(RecipeCourse.Course c) {
        List<RecipeCoursePersistenceModelItem> models = new ArrayList<>();
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (RecipeCoursePersistenceModelItem model : cache.values()) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {

        List<RecipeCoursePersistenceModelItem> models = checkCacheForRecipeId(domainId);

        if (models != null) {
            callback.onAllDomainModelsLoaded(models);
            return;
        }
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                    @Override
                    public void onAllDomainModelsLoaded(
                            List<RecipeCoursePersistenceModelItem> models) {
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        for (RecipeCoursePersistenceModelItem model : models) {
                            cache.put(model.getDataId(), model);
                        }
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).getAllByDomainId(
                                domainId,
                                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                                    @Override
                                    public void onAllDomainModelsLoaded(
                                            List<RecipeCoursePersistenceModelItem> models) {
                                        if (models == null) {
                                            onDomainModelsUnavailable();
                                            return;
                                        }
                                        if (cache == null) {
                                            cache = new LinkedHashMap<>();
                                        }
                                        for (RecipeCoursePersistenceModelItem model : models) {
                                            cache.put(model.getDataId(), model);
                                        }
                                        callback.onAllDomainModelsLoaded(models);
                                    }

                                    @Override
                                    public void onDomainModelsUnavailable() {
                                        callback.onDomainModelsUnavailable();
                                    }
                                });
                    }
                });
    }

    private List<RecipeCoursePersistenceModelItem> checkCacheForRecipeId(String recipeId) {
        List<RecipeCoursePersistenceModelItem> models = new ArrayList<>();
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (RecipeCoursePersistenceModelItem model : cache.values()) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        List<RecipeCoursePersistenceModelItem> activeModels = new ArrayList<>();
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                    @Override
                    public void onAllDomainModelsLoaded(
                            List<RecipeCoursePersistenceModelItem> models) {
                        for (RecipeCoursePersistenceModelItem m : models) {
                            if (m.isActive()) {
                                activeModels.add(m);
                            }
                        }
                        if (activeModels.isEmpty()) {
                            callback.onDomainModelsUnavailable();
                        } else {
                            callback.onAllDomainModelsLoaded(activeModels);
                        }
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                });
    }

    @Override
    public void update(@Nonnull RecipeCoursePersistenceModelItem model) {
        ((DomainDataAccessRecipeCourse) remoteDomainDataAccess).update(model);
        ((DomainDataAccessRecipeCourse) localDomainDataAccess).update(model);
        cache.put(model.getDataId(), model);
    }
}