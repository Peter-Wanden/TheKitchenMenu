package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public interface DataSourceRecipeCourse extends PrimitiveDataSource<RecipeCourseEntity> {

    void getAllByCourseNo(int courseNo,
                          @Nonnull GetAllCallback<RecipeCourseEntity> callback);

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllCallback<RecipeCourseEntity> callback);
}
