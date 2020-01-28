package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeComponentStateModel {

    @NonNull
    private final RecipeStateCalculator.ComponentName componentName;
    @Nonnull
    private final RecipeStateCalculator.ComponentState state;

    public RecipeComponentStateModel(@NonNull RecipeStateCalculator.ComponentName componentName,
                                     @Nonnull RecipeStateCalculator.ComponentState state) {
        this.componentName = componentName;
        this.state = state;
    }

    @NonNull
    RecipeStateCalculator.ComponentName getComponentName() {
        return componentName;
    }

    @Nonnull
    public RecipeStateCalculator.ComponentState getState() {
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
