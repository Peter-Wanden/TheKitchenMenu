package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.*;

public final class RecipeComponentStateModel {

    @NonNull
    private final ComponentName componentName;
    @Nonnull
    private final ComponentState status;

    public RecipeComponentStateModel(@NonNull ComponentName componentName,
                                     @Nonnull ComponentState status) {
        this.componentName = componentName;
        this.status = status;
    }

    @NonNull
    ComponentName getComponentName() {
        return componentName;
    }

    @Nonnull
    public ComponentState getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentStateModel model = (RecipeComponentStateModel) o;
        return componentName == model.componentName &&
                status == model.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentName, status);
    }

    @Override
    public String toString() {
        return "RecipeComponentStateModel{" +
                "componentName=" + componentName +
                ", status=" + status +
                '}';
    }
}
