package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeList.*;

public class RecipeListRequest implements UseCase.Request {
    @Nonnull
    private final RecipeFilter filter;

    public RecipeListRequest(@Nonnull RecipeFilter filter) {
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
        RecipeListRequest that = (RecipeListRequest) o;
        return filter == that.filter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filter);
    }

    @Nonnull
    @Override
    public String toString() {
        return "Model{" +
                "filter=" + filter +
                '}';
    }
}
