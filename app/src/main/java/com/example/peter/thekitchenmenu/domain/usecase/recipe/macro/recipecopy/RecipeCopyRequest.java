package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipecopy;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;

import javax.annotation.Nonnull;


public final class RecipeCopyRequest implements UseCaseBase.Request {
    @Nonnull
    private final String sourceId;

    public RecipeCopyRequest(@Nonnull String sourceId) {
        this.sourceId = sourceId;
    }

    @Nonnull
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public String toString() {
        return "RecipeMacroCopyRequest{" +
                "sourceId='" + sourceId + '\'' +
                '}';
    }
}
