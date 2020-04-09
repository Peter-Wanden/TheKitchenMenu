package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import javax.annotation.Nonnull;

public interface DomainDataAccessRecipePortions
        extends DomainDataAccess<RecipePortionsPersistenceModel> {

    void getByRecipeId(@Nonnull String recipeId,
                       @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback);
}
