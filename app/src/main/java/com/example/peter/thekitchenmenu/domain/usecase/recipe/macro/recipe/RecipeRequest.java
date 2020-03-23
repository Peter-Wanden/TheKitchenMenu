package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

import javax.annotation.Nonnull;


public final class RecipeRequest extends RecipeRequestAbstract {

    public RecipeRequest(@Nonnull String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RecipeRequest{" +
                "id='" + id + '\'' +
                '}';
    }
}
