package com.example.peter.thekitchenmenu.data.model;

import javax.annotation.Nonnull;

public final class FavoriteRecipeModel {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String recipeName;
    private final int prepTime;
    private final int cookTime;

    public FavoriteRecipeModel(@Nonnull String recipeId,
                               @Nonnull String recipeName,
                               int prepTime,
                               int cookTime) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
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
