package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import javax.annotation.Nonnull;

public final class RecipeRequest implements UseCase.Request {
    @Nonnull
    private final String recipeId;

    public RecipeRequest(@Nonnull String recipeId) {
        this.recipeId = recipeId;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Override
    public String toString() {
        return "RecipeMacroRequest{" +
                "recipeId='" + recipeId + '\'' +
                '}';
    }
}
