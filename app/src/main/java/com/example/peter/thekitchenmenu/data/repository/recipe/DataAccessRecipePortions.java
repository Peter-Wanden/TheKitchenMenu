package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsModelPersistence;

import javax.annotation.Nonnull;

public interface DataAccessRecipePortions extends DataAccess<RecipePortionsModelPersistence> {

    void getByRecipeId(@Nonnull String recipeId,
                       @Nonnull GetDomainModelCallback<RecipePortionsModelPersistence> callback);
}
