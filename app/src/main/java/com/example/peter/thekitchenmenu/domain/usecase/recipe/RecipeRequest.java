package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import javax.annotation.Nonnull;

public class RecipeRequest implements UseCaseCommand.Request {
    @Nonnull
    private final String recipeId;

    public RecipeRequest(@Nonnull String recipeId) {
        this.recipeId = recipeId;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }
}
