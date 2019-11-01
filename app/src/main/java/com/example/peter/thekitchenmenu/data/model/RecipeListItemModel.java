package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;

public class RecipeListItemModel {
    @NonNull
    private final String recipeId;
    @NonNull
    private final String recipeName;
    private final int prepTime;
    private final int cookTime;

    public RecipeListItemModel(@NonNull String recipeId, @NonNull String recipeName, int prepTime, int cookTime) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    @NonNull
    public String getRecipeName() {
        return recipeName;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }
}
