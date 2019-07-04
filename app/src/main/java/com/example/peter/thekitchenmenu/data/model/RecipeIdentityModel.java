package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
}
