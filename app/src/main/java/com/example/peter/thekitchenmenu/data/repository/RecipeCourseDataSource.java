package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;

public interface RecipeCourseDataSource extends DataSource<RecipeCourseEntity> {

    void getAllRecipesForCourseNo(int courseNo,
                                  @NonNull GetAllCallback<RecipeCourseEntity> callback);

    void getCoursesForRecipe(@NonNull String recipeId,
                             @NonNull GetAllCallback<RecipeCourseEntity> callback);
}
