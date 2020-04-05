package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public interface DataAccessRecipeCourse extends DataAccess<RecipeCoursePersistenceModel> {

    void getAllByCourseNo(int courseNo,
                          @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback);

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModel> callback);
}
