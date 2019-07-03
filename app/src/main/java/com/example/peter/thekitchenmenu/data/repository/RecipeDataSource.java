package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

import java.util.List;

public interface RecipeDataSource {

    interface LoadRecipesCallback {

        void onRecipesLoaded(List<RecipeEntity> recipeEntities);

        void onDataNotAvailable();
    }

    interface GetRecipeCallback {

        void onRecipeLoaded(RecipeEntity recipeEntity);

        void onDataNotAvailable();
    }

    void getRecipes(@NonNull LoadRecipesCallback callback);

    void getRecipe(@NonNull String recipeId, @NonNull GetRecipeCallback callback);

    void saveRecipe(@NonNull RecipeEntity recipeEntity);

    void refreshRecipes();

    void deleteAll();

    void deleteById(@NonNull String id);
}
