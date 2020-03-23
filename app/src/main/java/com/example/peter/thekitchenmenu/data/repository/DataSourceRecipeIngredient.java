package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;

import javax.annotation.Nonnull;

public interface DataSourceRecipeIngredient extends PrimitiveDataSource<RecipeIngredientEntity> {

    void getByRecipeId(@Nonnull String recipeId,
                       @Nonnull GetAllCallback<RecipeIngredientEntity> callback);

    void getByProductId(@Nonnull String productId,
                        @Nonnull GetAllCallback<RecipeIngredientEntity> callback);

    void getByIngredientId(@Nonnull String ingredientId,
                           @Nonnull GetAllCallback<RecipeIngredientEntity> callback);
}
