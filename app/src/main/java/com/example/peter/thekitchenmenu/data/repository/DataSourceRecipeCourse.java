package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeCourseEntity;

import javax.annotation.Nonnull;

public interface DataSourceRecipeCourse extends PrimitiveDataSource<RecipeCourseEntity> {

    void getAllRecipesForCourseNo(int courseNo,
                                  @Nonnull GetAllCallback<RecipeCourseEntity> callback);

    void getCoursesForRecipe(@Nonnull String recipeId,
                             @Nonnull GetAllCallback<RecipeCourseEntity> callback);
}
