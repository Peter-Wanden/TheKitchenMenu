package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import javax.annotation.Nonnull;

public final class RecipeIngredientListRequest implements UseCaseCommand.Request {
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
