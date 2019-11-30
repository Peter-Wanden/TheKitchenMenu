package com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

public class UseCasePortionCalculatorRequest implements UseCase.RequestValues {
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
        return "RequestValues{" +
                "recipeId='" + recipeId + '\'' +
                ", ingredientId='" + ingredientId + '\'' +
                ", recipeIngredientId='" + recipeIngredientId + '\'' +
                ", model=" + model +
                '}';
    }
}