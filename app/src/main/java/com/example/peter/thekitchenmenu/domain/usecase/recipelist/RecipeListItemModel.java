package com.example.peter.thekitchenmenu.domain.usecase.recipelist;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeListItemModel {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeListItemModel that = (RecipeListItemModel) o;
        return prepTime == that.prepTime &&
                cookTime == that.cookTime &&
                totalTime == that.totalTime &&
                recipeId.equals(that.recipeId) &&
                recipeName.equals(that.recipeName) &&
                recipeDescription.equals(that.recipeDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, recipeName, recipeDescription, prepTime, cookTime, totalTime);
    }

    @Override
    public String toString() {
        return "RecipeListItemModel{" +
                "recipeId='" + recipeId + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", recipeDescription='" + recipeDescription + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", totalTime=" + totalTime +
                '}';
    }
}
