package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import javax.annotation.Nonnull;

public interface DataAccessRecipePortions extends DataAccess<RecipePortionsPersistenceModel> {

    void getByRecipeId(@Nonnull String recipeId,
                       @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback);
}
