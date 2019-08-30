package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class RecipeIdentityModel {

    @NonNull
    private final String title;
    @Nullable
    private final String description;
    private final int prepTime;
    private final int cookTime;

    public RecipeIdentityModel(@NonNull String title,
                               @Nullable String description,
                               int prepTime,
                               int cookTime) {
        this.title = title;
        this.description = description;
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
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, prepTime, cookTime);
    }

    @Override
    public String toString() {
        return "RecipeIdentityModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                '}';
    }
}
