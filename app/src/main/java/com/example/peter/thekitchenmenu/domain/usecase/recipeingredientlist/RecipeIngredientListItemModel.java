package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

public final class RecipeIngredientListItemModel {
    @NonNull
    private final String recipeIngredientId;
    @NonNull
    private final String ingredientId;
    @NonNull
    private final String ingredientName;
    @NonNull
    private final String ingredientDescription;
    @NonNull
    private final MeasurementModel measurementModel;

    public RecipeIngredientListItemModel(@NonNull String recipeIngredientId,
                                         @NonNull String ingredientId,
                                         @NonNull String ingredientName,
                                         @NonNull String ingredientDescription,
                                         @NonNull MeasurementModel measurementModel) {
        this.recipeIngredientId = recipeIngredientId;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.ingredientDescription = ingredientDescription;
        this.measurementModel = measurementModel;
    }

    @NonNull
    public String getRecipeIngredientId() {
        return recipeIngredientId;
    }

    @NonNull
    public String getIngredientId() {
        return ingredientId;
    }

    @NonNull
    public String getIngredientName() {
        return ingredientName;
    }

    @NonNull
    public String getIngredientDescription() {
        return ingredientDescription;
    }

    @NonNull
    public MeasurementModel getMeasurementModel() {
        return measurementModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientListItemModel that = (RecipeIngredientListItemModel) o;
        return recipeIngredientId.equals(that.recipeIngredientId) &&
                ingredientId.equals(that.ingredientId) &&
                ingredientName.equals(that.ingredientName) &&
                ingredientDescription.equals(that.ingredientDescription) &&
                measurementModel.equals(that.measurementModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeIngredientId, ingredientId, ingredientName, ingredientDescription, measurementModel);
    }

    @Override
    public String toString() {
        return "RecipeIngredientListItemModel{" +
                "recipeIngredientId='" + recipeIngredientId + '\'' +
                ", ingredientId='" + ingredientId + '\'' +
                ", ingredientName='" + ingredientName + '\'' +
                ", ingredientDescription='" + ingredientDescription + '\'' +
                ", measurementModel=" + measurementModel +
                '}';
    }
}
