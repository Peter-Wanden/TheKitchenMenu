package com.example.peter.thekitchenmenu.domain.entity.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public class UnitOfMeasureRequest implements UseCaseBase.Request {
    @Nonnull
    private final MeasurementModel model;

    public UnitOfMeasureRequest(@Nonnull MeasurementModel model) {
        this.model = model;
    }

    @Nonnull
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

    @Nonnull
    @Override
    public String toString() {
        return "UnitOfMeasureRequest{" +
                "model=" + model +
                '}';
    }
}
