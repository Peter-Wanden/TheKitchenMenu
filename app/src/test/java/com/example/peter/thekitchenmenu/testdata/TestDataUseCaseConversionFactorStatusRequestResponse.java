package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;

public class TestDataUseCaseConversionFactorStatusRequestResponse {

    public static UseCaseConversionFactorStatus.RequestValues getRequestMetricNoConversionFactor() {
        return new UseCaseConversionFactorStatus.RequestValues(
                MeasurementSubtype.METRIC_MASS,
                TestDataIngredientEntity.getNewValidNameValidDescription().getId()
        );
    }

    public static UseCaseConversionFactorStatus.ResponseValues getResponseMetricNoConversionFactor() {
        return new UseCaseConversionFactorStatus.ResponseValues(UseCaseConversionFactorStatus.
                UseCaseConversionFactorResult.DISABLED);
    }
}
