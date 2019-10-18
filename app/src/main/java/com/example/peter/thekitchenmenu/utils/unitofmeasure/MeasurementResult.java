package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.model.MeasurementModel;

import java.util.Objects;

public class MeasurementResult {

    public enum ResultStatus {
        INVALID_CONVERSION_FACTOR,
        INVALID_PORTIONS,
        INVALID_TOTAL_MEASUREMENT_ONE,
        INVALID_TOTAL_MEASUREMENT_TWO,
        INVALID_MEASUREMENT,
        RESULT_OK
    }

    @NonNull
    private final MeasurementModel model;
    @NonNull
    private final ResultStatus result;

    public MeasurementResult(@NonNull MeasurementModel model, @NonNull ResultStatus result) {
        this.model = model;
        this.result = result;
    }

    @NonNull
    public MeasurementModel getModel() {
        return model;
    }

    @NonNull
    public ResultStatus getResult() {
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
