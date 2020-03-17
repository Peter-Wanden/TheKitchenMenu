package com.example.peter.thekitchenmenu.domain.usecase.recipe.state;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;

public final class RecipeStateRequest implements UseCase.Request {
    @Nonnull
    private final HashMap<ComponentName, ComponentState> componentStates;

    public RecipeStateRequest(@Nonnull HashMap<ComponentName, ComponentState> componentStates) {
        this.componentStates = componentStates;
    }

    @Nonnull
    public HashMap<ComponentName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStateRequest that = (RecipeStateRequest) o;
        return componentStates.equals(that.componentStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentStates);
    }

    @Override
    public String toString() {
        return "RecipeStateRequest{" +
                "componentStates=" + componentStates +
                '}';
    }
}
