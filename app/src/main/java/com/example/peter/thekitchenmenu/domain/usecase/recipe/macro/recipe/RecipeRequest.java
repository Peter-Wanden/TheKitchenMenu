package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainMessageBase;

import javax.annotation.Nonnull;


public final class RecipeRequest extends UseCaseDomainMessageBase {

    public RecipeRequest(@Nonnull String id) {
        this.dataId = id;
    }

    @Override
    public String toString() {
        return "RecipeRequest{" +
                "id='" + dataId + '\'' +
                '}';
    }
}
