package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends Repository<RecipeCourseModel>
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
                                 @Nonnull GetAllDomainModelsCallback<RecipeCourseModel> callback) {

        List<RecipeCourseModel> models = checkCacheForCourseNo(courseNo);

        if (models != null) {
            callback.onAllLoaded(models);
            return;
        }
        ((DataSourceRecipeCourse)localDataSource).getAllByCourseNo(
                courseNo,
                new GetAllDomainModelsCallback<RecipeCourseModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseModel> models) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeCourseModel model : models)
                            cache.put(model.getDataId(), model);

                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onDataUnavailable() {
                        ((DataSourceRecipeCourse)remoteDataSource).getAllByCourseNo(
                                courseNo,
                                new GetAllDomainModelsCallback<RecipeCourseModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCourseModel> models) {
                                        if (models == null) {
                                            onDataUnavailable();
                                            return;
                                        }

                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeCourseModel model : models)
                                            cache.put(model.getDataId(), model);

                                        callback.onAllLoaded(models);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
                                    }
                                });
                    }
                });
    }

    private List<RecipeCourseModel> checkCacheForCourseNo(int courseNo) {
        List<RecipeCourseModel> models = new ArrayList<>();
        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipeCourseModel model : cache.values()) {
                if (courseNo == model.getCourse().getCourseNo())
                    models.add(model);
            }
            return models.isEmpty() ? null : models;
        }
    }

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllDomainModelsCallback<RecipeCourseModel> callback) {

        List<RecipeCourseModel> models = checkCacheForRecipeId(recipeId);

        if (models != null) {
            callback.onAllLoaded(models);
            return;
        }
        ((DataSourceRecipeCourse)localDataSource).getAllByRecipeId(
                recipeId,
                new GetAllDomainModelsCallback<RecipeCourseModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseModel> models) {
                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        for (RecipeCourseModel model : models)
                            cache.put(model.getDataId(), model);

                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onDataUnavailable() {
                        ((DataSourceRecipeCourse)remoteDataSource).getAllByRecipeId(
                                recipeId,
                                new GetAllDomainModelsCallback<RecipeCourseModel>() {
                                    @Override
                                    public void onAllLoaded(List<RecipeCourseModel> models) {
                                        if (models == null) {
                                            onDataUnavailable();
                                            return;
                                        }

                                        if (cache == null)
                                            cache = new LinkedHashMap<>();

                                        for (RecipeCourseModel model : models)
                                            cache.put(model.getDataId(), model);

                                        callback.onAllLoaded(models);
                                    }

                                    @Override
                                    public void onDataUnavailable() {
                                        callback.onDataUnavailable();
                                    }
                                });
                    }
                });
    }

    private List<RecipeCourseModel> checkCacheForRecipeId(String recipeId) {
        List<RecipeCourseModel> models = new ArrayList<>();
        if (cache == null || cache.isEmpty())
            return null;
        else {
            for (RecipeCourseModel model : cache.values()) {
                if (model.getRecipeId().equals(recipeId)) {
                    models.add(model);
                }
            }
            return models.isEmpty() ? null : models;
        }
    }
}