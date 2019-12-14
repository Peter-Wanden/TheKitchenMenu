package com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

public class UseCasePortionCalculatorRequest implements UseCaseInteractor.Request {
    @NonNull
    private final String recipeId;
    @NonNull
    private final String ingredientId;
    @NonNull
    private final String recipeIngredientId;
    @NonNull
    private final MeasurementModel model;

    public UseCasePortionCalculatorRequest(@NonNull String recipeId,
                                           @NonNull String ingredientId,
                                           @NonNull String recipeIngredientId,
                                           @NonNull MeasurementModel model) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.recipeIngredientId = recipeIngredientId;
        this.model = model;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    @NonNull
    public String getIngredientId() {
        return ingredientId;
    }

    @NonNull
    public String getRecipeIngredientId() {
        return recipeIngredientId;
    }

    @NonNull
    public MeasurementModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UseCasePortionCalculatorRequest that = (UseCasePortionCalculatorRequest) o;
        return recipeId.equals(that.recipeId) &&
                ingredientId.equals(that.ingredientId) &&
                recipeIngredientId.equals(that.recipeIngredientId) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, ingredientId, recipeIngredientId, model);
    }

    @NonNull
    @Override
    public String toString() {
        return "Request{" +
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

        public UseCasePortionCalculatorRequest build() {
            return new UseCasePortionCalculatorRequest(
                    recipeId,
                    ingredientId,
                    recipeIngredientId,
                    model
            );
        }
    }
}