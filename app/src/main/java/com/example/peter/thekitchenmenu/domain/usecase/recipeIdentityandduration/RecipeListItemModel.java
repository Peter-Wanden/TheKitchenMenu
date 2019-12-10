package com.example.peter.thekitchenmenu.domain.usecase.recipeIdentityandduration;


import javax.annotation.Nonnull;

public class RecipeListItemModel {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String recipeName;
    private final String recipeDescription;
    private final int prepTime;
    private final int cookTime;
    private final int totalTime;

    public RecipeListItemModel(@Nonnull String recipeId,
                               @Nonnull String recipeName,
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

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
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
