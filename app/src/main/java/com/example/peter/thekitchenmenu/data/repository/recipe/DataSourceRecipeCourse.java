package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseModel;

import javax.annotation.Nonnull;

public interface DataSourceRecipeCourse extends DataSource<RecipeCourseModel> {

    void getAllByCourseNo(int courseNo,
                          @Nonnull GetAllDomainModelsCallback<RecipeCourseModel> callback);

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllDomainModelsCallback<RecipeCourseModel> callback);
}
