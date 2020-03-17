package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import javax.annotation.Nonnull;

public final class RecipeIngredientListRequest implements UseCase.Request {
    @Nonnull
    private final String recipeId;

    public RecipeIngredientListRequest(@Nonnull String recipeId) {
        this.recipeId = recipeId;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIngredientListRequest{" +
                "recipeId='" + recipeId + '\'' +
                '}';
    }
}
