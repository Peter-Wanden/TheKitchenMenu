package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeStateResponse implements UseCaseCommand.Response {
    @Nonnull
    private final RecipeState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final HashMap<ComponentName, ComponentState> componentStates;

    public RecipeStateResponse(@Nonnull RecipeState state,
                               @Nonnull List<FailReasons> failReasons,
                               @Nonnull HashMap<ComponentName, ComponentState> componentStates) {
        this.state = state;
        this.failReasons = failReasons;
        this.componentStates = componentStates;
    }

    @Nonnull
    public RecipeState getState() {
        return state;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public HashMap<ComponentName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStateResponse that = (RecipeStateResponse) o;
        return state == that.state &&
                failReasons.equals(that.failReasons) &&
                componentStates.equals(that.componentStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, componentStates);
    }

    @Override
    public String toString() {
        return "RecipeStateResponse{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", componentStates=" + componentStates +
                '}';
    }
}
