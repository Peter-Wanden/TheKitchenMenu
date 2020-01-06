package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeComponentStatusModel {

    @NonNull
    private final RecipeValidator.ComponentName componentName;
    @Nonnull
    private final RecipeValidator.ComponentStatus status;
    private final boolean isChanged;
    private final boolean isValid;

    public RecipeComponentStatusModel(@NonNull RecipeValidator.ComponentName componentName,
                                      @Nonnull RecipeValidator.ComponentStatus status,
                                      boolean isChanged,
                                      boolean isValid) {
        this.componentName = componentName;
        this.status = status;
        this.isChanged = isChanged;
        this.isValid = isValid;
    }

    @NonNull
    RecipeValidator.ComponentName getComponentName() {
        return componentName;
    }

    @Nonnull
    public RecipeValidator.ComponentStatus getStatus() {
        return status;
    }

    boolean isChanged() {
        return isChanged;
    }

    boolean isValid() {
        return isValid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentStatusModel that = (RecipeComponentStatusModel) o;
        return isChanged == that.isChanged &&
                isValid == that.isValid &&
                componentName == that.componentName &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentName, status, isChanged, isValid);
    }

    @Override
    public String toString() {
        return "RecipeComponentStatusModel{" +
                "componentName=" + componentName +
                ", status=" + status +
                ", isChanged=" + isChanged +
                ", isValid=" + isValid +
                '}';
    }
}
