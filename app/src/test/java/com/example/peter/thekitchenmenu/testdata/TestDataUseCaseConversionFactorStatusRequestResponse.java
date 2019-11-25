package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;

public class TestDataUseCaseConversionFactorStatusRequestResponse {

    public static UseCaseConversionFactorStatus.RequestValues
    getRequestMetricNoConversionFactor() {
        return new UseCaseConversionFactorStatus.RequestValues(
                MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                        getNewValidMetric().getUnitOfMeasureSubtype()),
                TestDataIngredientEntity.getNewValidNameValidDescription().getId());
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

    public static UseCaseConversionFactorStatus.RequestValues
    getRequestWithConversionFactorEnabledSet() {
        return new UseCaseConversionFactorStatus.RequestValues(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getExistingValidWithConversionFactor().getId());
    }
    public static UseCaseConversionFactorStatus.ResponseValues
    getResponseConversionFactorEnabledSet() {
        return new UseCaseConversionFactorStatus.ResponseValues(
                UseCaseConversionFactorStatus.UseCaseResult.ENABLED_EDITABLE_SET);
    }

    public static UseCaseConversionFactorStatus.RequestValues getRequestForIngredientNotFound() {
        return new UseCaseConversionFactorStatus.RequestValues(
                MeasurementSubtype.IMPERIAL_SPOON,
                "INGREDIENT_NOT_FOUND");
    }
    public static UseCaseConversionFactorStatus.ResponseValues getResponseForIngredientNotFound() {
        return new UseCaseConversionFactorStatus.ResponseValues(
                UseCaseConversionFactorStatus.UseCaseResult.INGREDIENT_DATA_NOT_AVAILABLE);
    }
}
