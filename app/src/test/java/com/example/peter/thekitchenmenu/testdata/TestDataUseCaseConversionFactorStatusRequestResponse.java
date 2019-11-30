package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatusRequest;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatusResponse;

public class TestDataUseCaseConversionFactorStatusRequestResponse {

    public static UseCaseConversionFactorStatusRequest
    getRequestMetricNoConversionFactor() {
        return new UseCaseConversionFactorStatusRequest(
                MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                        getNewValidMetric().getUnitOfMeasureSubtype()),
                TestDataIngredientEntity.getNewValidNameValidDescription().getId());
    }

    public static UseCaseConversionFactorStatusResponse
    getResponseMetricNoConversionFactor() {
        return new UseCaseConversionFactorStatusResponse(
                UseCaseConversionFactorStatus.UseCaseResult.DISABLED);
    }

    public static UseCaseConversionFactorStatusRequest
    getRequestWithConversionFactorFromAnotherUser() {
        return new UseCaseConversionFactorStatusRequest(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getExistingValidNameValidDescriptionFromAnotherUser().
                        getId());
    }
    public static UseCaseConversionFactorStatusResponse
    getResponseConversionFactorUneditable() {
        return new UseCaseConversionFactorStatusResponse(
                UseCaseConversionFactorStatus.UseCaseResult.ENABLED_UNEDITABLE);
    }

    public static UseCaseConversionFactorStatusRequest
    getRequestWithConversionFactorEnabledUnset() {
        return new UseCaseConversionFactorStatusRequest(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getNewInvalidNameValidDescription().getId());
    }
    public static UseCaseConversionFactorStatusResponse
    getResponseConversionFactorEnabledUnset() {
        return new UseCaseConversionFactorStatusResponse(
                UseCaseConversionFactorStatus.UseCaseResult.ENABLED_EDITABLE_UNSET);
    }

    public static UseCaseConversionFactorStatusRequest
    getRequestWithConversionFactorEnabledSet() {
        return new UseCaseConversionFactorStatusRequest(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getExistingValidWithConversionFactor().getId());
    }
    public static UseCaseConversionFactorStatusResponse
    getResponseConversionFactorEnabledSet() {
        return new UseCaseConversionFactorStatusResponse(
                UseCaseConversionFactorStatus.UseCaseResult.ENABLED_EDITABLE_SET);
    }

    public static UseCaseConversionFactorStatusRequest getRequestForIngredientNotFound() {
        return new UseCaseConversionFactorStatusRequest(
                MeasurementSubtype.IMPERIAL_SPOON,
                "INGREDIENT_NOT_FOUND");
    }
    public static UseCaseConversionFactorStatusResponse getResponseForIngredientNotFound() {
        return new UseCaseConversionFactorStatusResponse(
                UseCaseConversionFactorStatus.UseCaseResult.INGREDIENT_DATA_NOT_AVAILABLE);
    }
}
