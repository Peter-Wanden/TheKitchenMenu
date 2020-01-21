package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeComponentStateModel {

    @NonNull
    private final RecipeState.ComponentName componentName;
    @Nonnull
    private final RecipeState.ComponentState state;

    public RecipeComponentStateModel(@NonNull RecipeState.ComponentName componentName,
                                     @Nonnull RecipeState.ComponentState state) {
        this.componentName = componentName;
        this.state = state;
    }

    @NonNull
    RecipeState.ComponentName getComponentName() {
        return componentName;
    }

    @Nonnull
    public RecipeState.ComponentState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentStateModel model = (RecipeComponentStateModel) o;
        return componentName == model.componentName &&
                state == model.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentName, state);
    }

    @Override
    public String toString() {
        return "RecipeComponentStateModel{" +
                "componentName=" + componentName +
                ", state=" + state +
                '}';
    }
}
