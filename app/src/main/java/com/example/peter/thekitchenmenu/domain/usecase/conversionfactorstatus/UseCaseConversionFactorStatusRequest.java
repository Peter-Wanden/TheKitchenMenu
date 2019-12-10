package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;

import java.util.Objects;

public class UseCaseConversionFactorStatusRequest implements UseCaseInteractor.Request {
    @NonNull
    private MeasurementSubtype subtype;
    @NonNull
    private String ingredientId;

    public UseCaseConversionFactorStatusRequest(@NonNull MeasurementSubtype subtype,
                                                @NonNull String ingredientId) {
        this.subtype = subtype;
        this.ingredientId = ingredientId;
    }

    @NonNull
    public MeasurementSubtype getSubtype() {
        return subtype;
    }

    @NonNull
    public String getIngredientId() {
        return ingredientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UseCaseConversionFactorStatusRequest that = (UseCaseConversionFactorStatusRequest) o;
        return subtype == that.subtype &&
                ingredientId.equals(that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtype, ingredientId);
    }

    @NonNull
    @Override
    public String toString() {
        return "UseCaseConversionFactorStatusRequest{" +
                "subtype=" + subtype +
                ", ingredientId='" + ingredientId + '\'' +
                '}';
    }
}
