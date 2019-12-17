package com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.
        UseCaseRecipeIdentityAndDurationList.*;

public class UseCaseRecipeIdentityAndDurationListRequestModel
        implements UseCaseInteractor.Request {
    @Nonnull
    private final RecipeFilter filter;

    public UseCaseRecipeIdentityAndDurationListRequestModel(@Nonnull RecipeFilter filter) {
        this.filter = filter;
    }

    @Nonnull
    public RecipeFilter getFilter() {
        return filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UseCaseRecipeIdentityAndDurationListRequestModel that =
                (UseCaseRecipeIdentityAndDurationListRequestModel) o;
        return filter == that.filter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filter);
    }

    @Nonnull
    @Override
    public String toString() {
        return "UseCaseRecipeIdentityAndDurationListRequestModel{" +
                "filter=" + filter +
                '}';
    }
}
