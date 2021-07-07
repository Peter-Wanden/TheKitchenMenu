package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.MeasurementSubtype;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class ConversionFactorStatusRequest implements UseCaseBase.Request {
    @Nonnull
    private MeasurementSubtype subtype;
    @Nonnull
    private String ingredientId;

    public ConversionFactorStatusRequest(@Nonnull MeasurementSubtype subtype,
                                         @Nonnull String ingredientId) {
        this.subtype = subtype;
        this.ingredientId = ingredientId;
    }

    @Nonnull
    public MeasurementSubtype getSubtype() {
        return subtype;
    }

    @Nonnull
    public String getIngredientId() {
        return ingredientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionFactorStatusRequest that = (ConversionFactorStatusRequest) o;
        return subtype == that.subtype &&
                ingredientId.equals(that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtype, ingredientId);
    }

    @Nonnull
    @Override
    public String toString() {
        return "ConversionFactorStatusRequest{" +
                "subtype=" + subtype +
                ", ingredientId='" + ingredientId + '\'' +
                '}';
    }
}
