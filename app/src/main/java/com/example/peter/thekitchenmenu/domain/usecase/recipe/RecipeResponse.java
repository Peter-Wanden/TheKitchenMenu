package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeResponse implements UseCaseCommand.Response {
    @Nonnull
    private final RecipeState recipeState;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final HashMap<ComponentName, Response> componentResponses;

    public RecipeResponse(@Nonnull RecipeState recipeState,
                          @Nonnull List<FailReasons> failReasons,
                          @Nonnull HashMap<ComponentName, Response> componentResponses) {
        this.recipeState = recipeState;
        this.failReasons = failReasons;
        this.componentResponses = componentResponses;
    }

    @Nonnull
    public RecipeState getRecipeState() {
        return recipeState;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public HashMap<ComponentName, Response> getComponentResponses() {
        return componentResponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponse that = (RecipeResponse) o;
        return recipeState == that.recipeState &&
                failReasons.equals(that.failReasons) &&
                componentResponses.equals(that.componentResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeState, failReasons, componentResponses);
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipeState=" + recipeState +
                ", failReasons=" + failReasons +
                ", componentResponses=" + componentResponses +
                '}';
    }
}
