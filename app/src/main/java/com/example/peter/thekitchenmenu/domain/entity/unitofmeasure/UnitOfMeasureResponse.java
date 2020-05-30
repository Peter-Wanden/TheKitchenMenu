package com.example.peter.thekitchenmenu.domain.entity.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureAbstract.*;

public class UnitOfMeasureResponse implements UseCaseBase.Response {

    @Nonnull
    private final MeasurementModel model;
    @Nonnull
    private final ResultStatus resultStatus;

    public UnitOfMeasureResponse(@Nonnull MeasurementModel model,
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
        UnitOfMeasureResponse that = (UnitOfMeasureResponse) o;
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
        return "UnitOfMeasureResponse{" +
                "model=" + model +
                ", resultStatus=" + resultStatus +
                '}';
    }
}
