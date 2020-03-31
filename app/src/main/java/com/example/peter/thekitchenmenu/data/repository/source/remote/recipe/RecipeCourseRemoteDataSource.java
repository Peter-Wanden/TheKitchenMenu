package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeCourse;

import javax.annotation.Nonnull;

public class RecipeCourseRemoteDataSource implements DataSourceRecipeCourse {

    private static RecipeCourseRemoteDataSource INSTANCE;

    public static RecipeCourseRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeCourseRemoteDataSource();
        return INSTANCE;
    }


    @Override
    public void getAllByCourseNo(int courseNo,
                                 @Nonnull GetAllDomainModelsCallback<RecipeCourseEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllDomainModelsCallback<RecipeCourseEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeCourseEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeCourseEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeCourseEntity entity) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@Nonnull String id) {

    }
}
