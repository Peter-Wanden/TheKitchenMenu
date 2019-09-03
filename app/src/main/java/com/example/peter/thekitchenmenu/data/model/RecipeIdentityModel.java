package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class RecipeIdentityModel {

    @NonNull
    private final String title;
    @Nullable
    private final String description;
    @NonNull
    private final String recipeId;
    private final int prepTime;
    private final int cookTime;

    public RecipeIdentityModel(@NonNull String title,
                               @Nullable String description,
                               @NonNull String recipeId,
                               int prepTime,
                               int cookTime) {
        this.title = title;
        this.description = description;
        this.recipeId = recipeId;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIdentityModel that = (RecipeIdentityModel) o;
        return prepTime == that.prepTime &&
                cookTime == that.cookTime &&
                title.equals(that.title) &&
                description.equals(that.description) &&
                recipeId.equals(that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, recipeId, prepTime, cookTime);
    }

    @Override
    public String toString() {
        return "RecipeIdentityModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                '}';
    }
}
