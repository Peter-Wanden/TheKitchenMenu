package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends Repository<RecipeCoursePersistenceModel>
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
    public void getAllByCourseNo(int courseNo,
                                 @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {

        List<RecipeCoursePersistenceModel> models = checkCacheForCourseNo(courseNo);

        if (models != null) {
            callback.onAllLoaded(models);
            return;
        }
        ((DataAccessRecipeCourse) localDataAccess).getAllByCourseNo(
                courseNo,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeCoursePersistenceModel model : models)
                            cache.put(model.getDataId(), model);

                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeCourse) remoteDataAccess).getAllByCourseNo(
                                courseNo,
                                new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                                        if (models == null) {
                                            onModelsUnavailable();
                                            return;
                                        }

                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeCoursePersistenceModel model : models)
                                            cache.put(model.getDataId(), model);

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

    private List<RecipeCoursePersistenceModel> checkCacheForCourseNo(int courseNo) {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipeCoursePersistenceModel model : cache.values()) {
                if (courseNo == model.getCourse().getCourseNo())
                    models.add(model);
            }
            return models.isEmpty() ? null : models;
        }
    }

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback) {

        List<RecipeCoursePersistenceModel> models = checkCacheForRecipeId(recipeId);

        if (models != null) {
            callback.onAllLoaded(models);
            return;
        }
        ((DataAccessRecipeCourse) localDataAccess).getAllByRecipeId(
                recipeId,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeCoursePersistenceModel model : models)
                            cache.put(model.getDataId(), model);

                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        ((DataAccessRecipeCourse) remoteDataAccess).getAllByRecipeId(
                                recipeId,
                                new GetAllDomainModelsCallback<RecipeCoursePersistenceModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCoursePersistenceModel> models) {
                                        if (models == null) {
                                            onModelsUnavailable();
                                            return;
                                        }

                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeCoursePersistenceModel model : models)
                                            cache.put(model.getDataId(), model);

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
        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipeCoursePersistenceModel model : cache.values()) {
                if (model.getRecipeId().equals(recipeId)) {
                    models.add(model);
                }
            }
            return models.isEmpty() ? null : models;
        }
    }
}