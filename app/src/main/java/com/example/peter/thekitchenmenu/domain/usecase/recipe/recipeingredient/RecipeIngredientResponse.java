package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredient.*;

public final class RecipeIngredientResponse implements UseCase.Response {
    @Nonnull
    private final MeasurementModel model;
    @Nonnull
    private final Result resultStatus;

    public RecipeIngredientResponse(@Nonnull MeasurementModel model,
                                    @Nonnull Result resultStatus) {
        this.model = model;
        this.resultStatus = resultStatus;
    }

    @Nonnull
    public MeasurementModel getModel() {
        return model;
    }

    @Nonnull
    public Result getResultStatus() {
        return resultStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientResponse that = (RecipeIngredientResponse) o;
        return model.equals(that.model) &&
                resultStatus == that.resultStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, resultStatus);
    }

    @Nonnull
    @Override
    public String toString() {
        return "IngredientCalculatorResponse{" +
                "model=" + model +
                ", resultStatus=" + resultStatus +
                '}';
    }
}
