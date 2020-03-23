package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIngredientListItemModel {
    @Nonnull
    private final String recipeIngredientId;
    @Nonnull
    private final String ingredientId;
    @Nonnull
    private final String ingredientName;
    @Nonnull
    private final String ingredientDescription;
    @Nonnull
    private final MeasurementModel measurementModel;

    public RecipeIngredientListItemModel(@Nonnull String recipeIngredientId,
                                         @Nonnull String ingredientId,
                                         @Nonnull String ingredientName,
                                         @Nonnull String ingredientDescription,
                                         @Nonnull MeasurementModel measurementModel) {
        this.recipeIngredientId = recipeIngredientId;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.ingredientDescription = ingredientDescription;
        this.measurementModel = measurementModel;
    }

    @Nonnull
    public String getRecipeIngredientId() {
        return recipeIngredientId;
    }

    @Nonnull
    public String getIngredientId() {
        return ingredientId;
    }

    @Nonnull
    public String getIngredientName() {
        return ingredientName;
    }

    @Nonnull
    public String getIngredientDescription() {
        return ingredientDescription;
    }

    @Nonnull
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
        return Objects.hash(recipeIngredientId, ingredientId, ingredientName,
                ingredientDescription, measurementModel);
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
