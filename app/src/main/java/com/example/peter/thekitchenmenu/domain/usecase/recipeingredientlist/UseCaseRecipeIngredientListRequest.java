package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.UseCaseCommandAbstract;

public final class UseCaseRecipeIngredientListRequest implements UseCaseCommandAbstract.Request {
    @NonNull
    private final String recipeId;

    public UseCaseRecipeIngredientListRequest(@NonNull String recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Request{" +
                "recipeId='" + recipeId + '\'' +
                '}';
    }
}
