package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import javax.annotation.Nonnull;

public final class UseCaseRecipeIngredientListRequest implements UseCaseInteractor.Request {
    @Nonnull
    private final String recipeId;

    public UseCaseRecipeIngredientListRequest(@Nonnull String recipeId) {
        this.recipeId = recipeId;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    @Override
    public String toString() {
        return "Request{" +
                "recipeId='" + recipeId + '\'' +
                '}';
    }
}
