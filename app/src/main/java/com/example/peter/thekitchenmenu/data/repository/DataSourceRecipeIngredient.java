package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;

public interface DataSourceRecipeIngredient extends DataSource<RecipeIngredientQuantityEntity> {

    void getByRecipeId(@NonNull String recipeId,
                       @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback);

    void getByProductId(@NonNull String productId,
                        @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback);

    void getByIngredientId(@NonNull String ingredientId,
                           @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback);
}
