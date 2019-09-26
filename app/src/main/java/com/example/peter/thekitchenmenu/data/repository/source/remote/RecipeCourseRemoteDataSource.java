package com.example.peter.thekitchenmenu.data.repository.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeCourse;

public class RecipeCourseRemoteDataSource implements DataSourceRecipeCourse {

    private static RecipeCourseRemoteDataSource INSTANCE;

    public static RecipeCourseRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeCourseRemoteDataSource();
        return INSTANCE;
    }


    @Override
    public void getAllRecipesForCourseNo(int courseNo, @NonNull GetAllCallback<RecipeCourseEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getCoursesForRecipe(@NonNull String recipeId, @NonNull GetAllCallback<RecipeCourseEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getAll(@NonNull GetAllCallback<RecipeCourseEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String id, @NonNull GetEntityCallback<RecipeCourseEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull RecipeCourseEntity object) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@NonNull String id) {

    }
}
