package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipecopy;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;

import javax.annotation.Nonnull;

public final class RecipeCopyResponse implements UseCaseBase.Response {

    @Nonnull
    private final Recipe recipeMacro;

    public RecipeCopyResponse(@Nonnull Recipe recipeMacro) {
        this.recipeMacro = recipeMacro;
    }

    @Nonnull
    public Recipe getRecipeMacro() {
        return recipeMacro;
    }

    @Override
    public String toString() {
        return "RecipeMacroCopyResponse{" +
                "recipeMacro=" + recipeMacro +
                '}';
    }
}
