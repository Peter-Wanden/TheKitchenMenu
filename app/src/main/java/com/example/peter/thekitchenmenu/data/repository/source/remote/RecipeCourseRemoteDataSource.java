package com.example.peter.thekitchenmenu.data.repository.source.remote;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeCourse;

import javax.annotation.Nonnull;

public class RecipeCourseRemoteDataSource implements DataSourceRecipeCourse {

    private static RecipeCourseRemoteDataSource INSTANCE;

    public static RecipeCourseRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeCourseRemoteDataSource();
        return INSTANCE;
    }


    @Override
    public void getAllRecipesForCourseNo(int courseNo,
                                         @Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getCoursesForRecipe(@Nonnull String recipeId,
                                    @Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeCourseEntity> callback) {
        callback.onDataNotAvailable();
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
