package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataAccess;

import javax.annotation.Nonnull;

public interface DataAccessRecipeIngredient extends DataAccess<RecipeIngredientModel> {

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback);

    void getAllByProductId(@Nonnull String productId,
                           @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback);

    void getAllByIngredientId(@Nonnull String ingredientId,
                              @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback);
}
