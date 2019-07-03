package com.example.peter.thekitchenmenu.data.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.RecipeDataSource;

public class RecipeRemoteDataSource implements RecipeDataSource {

    private static RecipeRemoteDataSource INSTANCE;

    public static RecipeRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeRemoteDataSource();
        return INSTANCE;

    }

    @Override
    public void getRecipes(@NonNull LoadRecipesCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getRecipe(@NonNull String recipeId, @NonNull GetRecipeCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void saveRecipe(@NonNull RecipeEntity recipeEntity) {
        // Not required because the {@link RecipeRepository} handles the logic of refreshing the
        // recipes from all the available data sources.
    }

    @Override
    public void refreshRecipes() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@NonNull String id) {

    }
}
