package com.example.peter.thekitchenmenu.domain.usecase;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.model.MeasurementModel;

import java.util.Objects;

public class MeasurementResult {

    @NonNull
    private final MeasurementModel model;

    @NonNull
    private final UseCasePortionCalculator.ResultStatus result;

    public MeasurementResult(@NonNull MeasurementModel model,
                             @NonNull UseCasePortionCalculator.ResultStatus result) {
        this.model = model;
        this.result = result;
    }

    @NonNull
    public MeasurementModel getModel() {
        return model;
    }

    @NonNull
    public UseCasePortionCalculator.ResultStatus getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementResult that = (MeasurementResult) o;
        return model.equals(that.model) &&
                result == that.result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, result);
    }

    @NonNull
    @Override
    public String toString() {
        return "MeasurementResult{" +
                "model=" + model +
                ", result=" + result +
                '}';
    }
}
