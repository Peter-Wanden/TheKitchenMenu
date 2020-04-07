package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientModelPersistence;

import javax.annotation.Nonnull;

public interface DataAccessRecipeIngredient extends DataAccess<RecipeIngredientModelPersistence> {

    void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback);

    void getAllByProductId(
            @Nonnull String productId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback);

    void getAllByIngredientId(
            @Nonnull String ingredientId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback);
}
