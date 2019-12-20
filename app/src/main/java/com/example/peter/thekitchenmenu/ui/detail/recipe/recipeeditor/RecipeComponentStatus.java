package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;

import java.util.Objects;

public final class RecipeComponentStatus {

    @NonNull
    private final RecipeValidator.ModelName modelName;
    private final boolean isChanged;
    private final boolean isValid;

    public RecipeComponentStatus(@NonNull RecipeValidator.ModelName modelName,
                                 boolean isChanged,
                                 boolean isValid) {
        this.modelName = modelName;
        this.isChanged = isChanged;
        this.isValid = isValid;
    }

    @NonNull
    RecipeValidator.ModelName getModelName() {
        return modelName;
    }

    boolean isChanged() {
        return isChanged;
    }

    boolean isValid() {
        return isValid;
    }

    @Override
    public String toString() {
        return "RecipeComponentStatus{" +
                "modelName=" + modelName +
                ", isChanged=" + isChanged +
                ", isModelValid=" + isValid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentStatus that = (RecipeComponentStatus) o;
        return isChanged == that.isChanged &&
                isValid == that.isValid &&
                modelName == that.modelName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelName, isChanged, isValid);
    }
}
