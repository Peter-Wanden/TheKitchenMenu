package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;

public class RecipeListItemModel {
    @NonNull
    private final String recipeId;
    @NonNull
    private final String recipeName;
    private final String recipeDescription;
    private final int prepTime;
    private final int cookTime;
    private final int totalTime;

    public RecipeListItemModel(@NonNull String recipeId,
                               @NonNull String recipeName,
                               String recipeDescription,
                               int prepTime,
                               int cookTime,
                               int totalTime) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.totalTime = totalTime;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    @NonNull
    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getTotalTime() {
        return totalTime;
    }
}
