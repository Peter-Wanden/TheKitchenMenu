package com.example.peter.thekitchenmenu.domain.usecase;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

public final class RecipeIngredientListItemModel {
    private final String recipeIngredientId;
    private final String ingredientName;
    private final String ingredientDescription;
    private final MeasurementModel measurementModel;

    public RecipeIngredientListItemModel(String recipeIngredientId,
                                         String ingredientName,
                                         String ingredientDescription,
                                         MeasurementModel measurementModel) {
        this.recipeIngredientId = recipeIngredientId;
        this.ingredientName = ingredientName;
        this.ingredientDescription = ingredientDescription;
        this.measurementModel = measurementModel;
    }

    public String getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getIngredientDescription() {
        return ingredientDescription;
    }

    public MeasurementModel getMeasurementModel() {
        return measurementModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientListItemModel model = (RecipeIngredientListItemModel) o;
        return recipeIngredientId.equals(model.recipeIngredientId) &&
                ingredientName.equals(model.ingredientName) &&
                ingredientDescription.equals(model.ingredientDescription) &&
                measurementModel.equals(model.measurementModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeIngredientId, ingredientName, ingredientDescription, measurementModel);
    }

    @Override
    public String toString() {
        return "RecipeIngredientListItemModel{" +
                "recipeIngredientId='" + recipeIngredientId + '\'' +
                ", ingredientName='" + ingredientName + '\'' +
                ", ingredientDescription='" + ingredientDescription + '\'' +
                ", measurementModel=" + measurementModel +
                '}';
    }
}
