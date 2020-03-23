package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class IngredientCalculatorRequest implements UseCase.Request {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String ingredientId;
    @Nonnull
    private final String recipeIngredientId;
    @Nonnull
    private final MeasurementModel model;

    public IngredientCalculatorRequest(@Nonnull String recipeId,
                                       @Nonnull String ingredientId,
                                       @Nonnull String recipeIngredientId,
                                       @Nonnull MeasurementModel model) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.recipeIngredientId = recipeIngredientId;
        this.model = model;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    public String getIngredientId() {
        return ingredientId;
    }

    @Nonnull
    public String getRecipeIngredientId() {
        return recipeIngredientId;
    }

    @Nonnull
    public MeasurementModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientCalculatorRequest that = (IngredientCalculatorRequest) o;
        return recipeId.equals(that.recipeId) &&
                ingredientId.equals(that.ingredientId) &&
                recipeIngredientId.equals(that.recipeIngredientId) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, ingredientId, recipeIngredientId, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "IngredientCalculatorRequest{" +
                "recipeId='" + recipeId + '\'' +
                ", ingredientId='" + ingredientId + '\'' +
                ", recipeIngredientId='" + recipeIngredientId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String recipeId;
        private String ingredientId;
        private String recipeIngredientId;
        private MeasurementModel model;

        public Builder setRecipeId(String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder setIngredientId(String ingredientId) {
            this.ingredientId = ingredientId;
            return this;
        }

        public Builder setRecipeIngredientId(String recipeIngredientId) {
            this.recipeIngredientId = recipeIngredientId;
            return this;
        }

        public Builder setMeasurementModel(MeasurementModel model) {
            this.model = model;
            return this;
        }

        public IngredientCalculatorRequest build() {
            return new IngredientCalculatorRequest(
                    recipeId,
                    ingredientId,
                    recipeIngredientId,
                    model
            );
        }
    }
}