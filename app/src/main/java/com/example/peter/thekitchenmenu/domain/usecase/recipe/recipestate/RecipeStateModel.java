package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState.*;

public final class RecipeStateModel {
    @Nonnull
    private final State recipeState;
    @Nonnull
    private final HashMap<ComponentName, ComponentState> componentStates;

    public RecipeStateModel(@Nonnull State recipeState,
                            @Nonnull HashMap<ComponentName, ComponentState> componentStates) {
        this.recipeState = recipeState;
        this.componentStates = componentStates;
    }

    @Nonnull
    public State getRecipeState() {
        return recipeState;
    }

    @Nonnull
    public HashMap<ComponentName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStateModel model = (RecipeStateModel) o;
        return recipeState == model.recipeState &&
                componentStates.equals(model.componentStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeState, componentStates);
    }

    @Override
    public String toString() {
        return "RecipeStateModel{" +
                "recipeState=" + recipeState +
                ", componentStates=" + componentStates +
                '}';
    }
}
