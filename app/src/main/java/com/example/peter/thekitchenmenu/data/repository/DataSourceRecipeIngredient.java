package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;

public interface DataSourceRecipeIngredient extends DataSource<RecipeIngredientEntity> {

    void getByRecipeId(@NonNull String recipeId,
                       @NonNull GetAllCallback<RecipeIngredientEntity> callback);

    void getByProductId(@NonNull String productId,
                        @NonNull GetAllCallback<RecipeIngredientEntity> callback);

    void getByIngredientId(@NonNull String ingredientId,
                           @NonNull GetEntityCallback<RecipeIngredientEntity> callback);
}
