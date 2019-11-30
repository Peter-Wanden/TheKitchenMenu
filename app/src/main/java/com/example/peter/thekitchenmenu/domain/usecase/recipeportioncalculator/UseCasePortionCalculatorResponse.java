package com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

public class UseCasePortionCalculatorResponse implements UseCase.ResponseValues {
    @NonNull
    private final MeasurementModel model;
    @NonNull
    private final UseCasePortionCalculator.ResultStatus resultStatus;

    public UseCasePortionCalculatorResponse(@NonNull MeasurementModel model, @NonNull UseCasePortionCalculator.ResultStatus resultStatus) {
        this.model = model;
        this.resultStatus = resultStatus;
    }

    @NonNull
    public MeasurementModel getModel() {
        return model;
    }

    @NonNull
    public UseCasePortionCalculator.ResultStatus getResultStatus() {
        return resultStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UseCasePortionCalculatorResponse that = (UseCasePortionCalculatorResponse) o;
        return model.equals(that.model) &&
                resultStatus == that.resultStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, resultStatus);
    }

    @Override
    public String toString() {
        return "UseCasePortionCalculatorResponse{" +
                "model=" + model +
                ", resultStatus=" + resultStatus +
                '}';
    }
}
