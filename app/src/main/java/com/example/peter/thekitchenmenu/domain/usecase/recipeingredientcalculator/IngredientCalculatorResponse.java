package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientcalculator;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeingredientcalculator.IngredientCalculator.*;

public final class IngredientCalculatorResponse implements UseCaseInteractor.Response {
    @Nonnull
    private final MeasurementModel model;
    @Nonnull
    private final ResultStatus resultStatus;

    public IngredientCalculatorResponse(@Nonnull MeasurementModel model,
                                        @Nonnull ResultStatus resultStatus) {
        this.model = model;
        this.resultStatus = resultStatus;
    }

    @Nonnull
    public MeasurementModel getModel() {
        return model;
    }

    @Nonnull
    public ResultStatus getResultStatus() {
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
