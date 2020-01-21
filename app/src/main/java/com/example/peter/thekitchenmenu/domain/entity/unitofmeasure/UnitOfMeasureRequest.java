package com.example.peter.thekitchenmenu.domain.entity.unitofmeasure;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

public class UnitOfMeasureRequest implements UseCase.Request {
    @NonNull
    private final MeasurementModel model;

    public UnitOfMeasureRequest(@NonNull MeasurementModel model) {
        this.model = model;
    }

    @NonNull
    public MeasurementModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitOfMeasureRequest that = (UnitOfMeasureRequest) o;
        return model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }

    @Override
    public String toString() {
        return "UnitOfMeasureRequest{" +
                "model=" + model +
                '}';
    }
}
