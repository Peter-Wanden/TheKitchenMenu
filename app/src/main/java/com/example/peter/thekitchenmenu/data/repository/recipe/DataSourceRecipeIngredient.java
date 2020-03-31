package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

import javax.annotation.Nonnull;

public interface DataSourceRecipeIngredient extends DataSource<RecipeIngredientModel> {

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback);

    void getAllByProductId(@Nonnull String productId,
                           @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback);

    void getAllByIngredientId(@Nonnull String ingredientId,
                              @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback);
}
