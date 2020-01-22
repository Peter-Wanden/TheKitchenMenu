package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatusRequest;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatusResponse;

public class TestDataUseCaseConversionFactorStatusRequestResponse {

    public static ConversionFactorStatusRequest
    getRequestMetricNoConversionFactor() {
        return new ConversionFactorStatusRequest(
                MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                        getNewValidMetric().getUnitOfMeasureSubtype()),
                TestDataIngredientEntity.getNewValidNameValidDescription().getId());
    }

    public static ConversionFactorStatusResponse
    getResponseMetricNoConversionFactor() {
        return new ConversionFactorStatusResponse(
                ConversionFactorStatus.Result.DISABLED);
    }

    public static ConversionFactorStatusRequest
    getRequestWithConversionFactorFromAnotherUser() {
        return new ConversionFactorStatusRequest(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getExistingValidNameValidDescriptionFromAnotherUser().
                        getId());
    }
    public static ConversionFactorStatusResponse
    getResponseConversionFactorUneditable() {
        return new ConversionFactorStatusResponse(
                ConversionFactorStatus.Result.ENABLED_UNEDITABLE);
    }

    public static ConversionFactorStatusRequest
    getRequestWithConversionFactorEnabledUnset() {
        return new ConversionFactorStatusRequest(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getNewInvalidNameValidDescription().getId());
    }
    public static ConversionFactorStatusResponse
    getResponseConversionFactorEnabledUnset() {
        return new ConversionFactorStatusResponse(
                ConversionFactorStatus.Result.ENABLED_EDITABLE_UNSET);
    }

    public static ConversionFactorStatusRequest
    getRequestWithConversionFactorEnabledSet() {
        return new ConversionFactorStatusRequest(
                MeasurementSubtype.IMPERIAL_SPOON,
                TestDataIngredientEntity.getExistingValidWithConversionFactor().getId());
    }
    public static ConversionFactorStatusResponse
    getResponseConversionFactorEnabledSet() {
        return new ConversionFactorStatusResponse(
                ConversionFactorStatus.Result.ENABLED_EDITABLE_SET);
    }

    public static ConversionFactorStatusRequest getRequestForIngredientNotFound() {
        return new ConversionFactorStatusRequest(
                MeasurementSubtype.IMPERIAL_SPOON,
                "INGREDIENT_NOT_FOUND");
    }
    public static ConversionFactorStatusResponse getResponseForIngredientNotFound() {
        return new ConversionFactorStatusResponse(
                ConversionFactorStatus.Result.DATA_UNAVAILABLE);
    }
}