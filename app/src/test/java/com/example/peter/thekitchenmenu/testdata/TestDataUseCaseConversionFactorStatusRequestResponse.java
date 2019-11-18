package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;

public class TestDataUseCaseConversionFactorStatusRequestResponse {

    public static UseCaseConversionFactorStatus.RequestValues
    getRequestMetricNoConversionFactor() {
        return new UseCaseConversionFactorStatus.RequestValues(
                MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                        getNewValidMetric().getUnitOfMeasureSubtype()),
                TestDataIngredientEntity.getNewValidNameValidDescription().getId()
        );
    }

    public static UseCaseConversionFactorStatus.ResponseValues
    getResponseMetricNoConversionFactor() {
        return new UseCaseConversionFactorStatus.ResponseValues(
                UseCaseConversionFactorStatus.UseCaseResult.DISABLED);
    }

    public static UseCaseConversionFactorStatus.RequestValues
    getRequestWithConversionFactorFromAnotherUser() {
        return new UseCaseConversionFactorStatus.RequestValues(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getExistingValidNameValidDescriptionFromAnotherUser().
                        getId());
    }

    public static UseCaseConversionFactorStatus.ResponseValues
    getResponseConversionFactorUneditable() {
        return new UseCaseConversionFactorStatus.ResponseValues(
                UseCaseConversionFactorStatus.UseCaseResult.ENABLED_UNEDITABLE);
    }

    public static UseCaseConversionFactorStatus.RequestValues
    getRequestWithConversionFactorEnabledUnset() {
        return new UseCaseConversionFactorStatus.RequestValues(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getNewInvalidNameValidDescription().getId());
    }

    public static UseCaseConversionFactorStatus.ResponseValues
    getResponseConversionFactorEnabledUnset() {
        return new UseCaseConversionFactorStatus.ResponseValues(
                UseCaseConversionFactorStatus.UseCaseResult.ENABLED_EDITABLE_UNSET);
    }
}
