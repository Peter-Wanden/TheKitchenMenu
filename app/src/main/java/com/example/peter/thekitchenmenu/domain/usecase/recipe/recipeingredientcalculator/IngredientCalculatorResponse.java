package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator.IngredientCalculator.*;

public final class IngredientCalculatorResponse implements UseCase.Response {
    @Nonnull
    private final MeasurementModel model;
    @Nonnull
    private final Result resultStatus;

    public IngredientCalculatorResponse(@Nonnull MeasurementModel model,
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
        IngredientCalculatorResponse that = (IngredientCalculatorResponse) o;
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
