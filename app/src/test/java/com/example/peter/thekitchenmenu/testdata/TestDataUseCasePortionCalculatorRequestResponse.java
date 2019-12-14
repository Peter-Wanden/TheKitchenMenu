package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculatorResponse;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculator.*;

public class TestDataUseCasePortionCalculatorRequestResponse {

    private static String NO_RECIPE_ID = "";
    private static String NO_INGREDIENT_ID = "";
    private static String NO_RECIPE_INGREDIENT_ID = "";

    public static UseCasePortionCalculatorRequest getRequestEmptyFourPortions() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static UseCasePortionCalculatorResponse getResponseEmptyFourPortions() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getInvalidEmptyFourPortionsSet(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static UseCasePortionCalculatorRequest getRequestInvalidTotalUnitOne() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitOne());
    }
    public static UseCasePortionCalculatorResponse getResponseInvalidTotalUnitOne() {

        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidTotalUnitOne(),
                ResultStatus.INVALID_TOTAL_UNIT_ONE);
    }

    public static UseCasePortionCalculatorRequest getRequestValidTotalUnitOne() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitOne());
    }
    public static UseCasePortionCalculatorResponse getResponseValidTotalUnitOne() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getNewValidTotalUnitOne(),
                ResultStatus.RESULT_OK);
    }

    public static UseCasePortionCalculatorRequest getRequestInvalidTotalUnitTwo() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo());
    }
    public static UseCasePortionCalculatorResponse getResponseInvalidTotalUnitTwo() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo(),
                ResultStatus.INVALID_TOTAL_UNIT_TWO);
    }

    public static UseCasePortionCalculatorRequest getRequestValidTotalUnitTwo() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitTwo());
    }
    public static UseCasePortionCalculatorResponse getResponseValidTotalUnitTwo() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getNewValidTotalUnitTwo(),
                ResultStatus.RESULT_OK);
    }

    public static UseCasePortionCalculatorRequest getRequestUnitOfMeasureChangeImperialSpoon() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon());
    }
    public static UseCasePortionCalculatorResponse getResponseUnitOfMeasureChangeImperialSpoon() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static UseCasePortionCalculatorRequest getRequestNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated());
    }
    public static UseCasePortionCalculatorResponse getResponseNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated(),
                ResultStatus.RESULT_OK);
    }

    public static UseCasePortionCalculatorRequest getRequestNewValidImperialSpoonInvalidConversionFactor() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidConversionFactor());
    }
    public static UseCasePortionCalculatorResponse getResponseNewValidImperialSpoonInvalidConversionFactor() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidConversionFactor(),
                ResultStatus.INVALID_CONVERSION_FACTOR);
    }

    public static UseCasePortionCalculatorRequest getRequestNewValidImperialSpoonValidConversionFactor() {
        return new UseCasePortionCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor());
    }
    public static UseCasePortionCalculatorResponse getResponseNewValidImperialSpoonValidConversionFactor() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor(),
                ResultStatus.RESULT_OK);
    }

    public static UseCasePortionCalculatorRequest getRequestExistingValidMetric() {
        return new UseCasePortionCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static UseCasePortionCalculatorResponse getResponseExistingValidMetric() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getValidExistingMetric(),
                ResultStatus.RESULT_OK);
    }

    public static UseCasePortionCalculatorRequest getRequestExistingMetricInvalidTotalUnitOne() {
        return new UseCasePortionCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne());
    }
    public static UseCasePortionCalculatorResponse getResponseExistingMetricInvalidTotalUnitOne() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne(),
                ResultStatus.INVALID_TOTAL_UNIT_ONE);
    }

    public static UseCasePortionCalculatorRequest getRequestExistingMetricValidTotalUnitOne() {
        return new UseCasePortionCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidUnitOneUpdated()
        );
    }
    public static UseCasePortionCalculatorResponse getResponseExistingMetricValidTotalUnitOne() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricValidTotalOne(),
                ResultStatus.RESULT_OK
        );
    }

    public static UseCasePortionCalculatorRequest getRequestExistingMetricValidTotalUnitTwo() {
        return new UseCasePortionCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated());
    }
    public static UseCasePortionCalculatorResponse getResponseExistingMetricValidTotalUnitTwo() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated(),
                ResultStatus.RESULT_OK);
    }

    public static UseCasePortionCalculatorRequest getRequestExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new UseCasePortionCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial());
    }
    public static UseCasePortionCalculatorResponse getResponseExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static UseCasePortionCalculatorRequest getRequestExistingValidImperialSpoon() {
        return new UseCasePortionCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static UseCasePortionCalculatorResponse getResponseExistingImperialSpoon() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getExistingImperialSpoonValidConversionFactorNotSet(),
                ResultStatus.RESULT_OK
        );
    }

    public static UseCasePortionCalculatorRequest getRequestExistingImperialSpoonInvalidConversionFactor() {
        return new UseCasePortionCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor());
    }
    public static UseCasePortionCalculatorResponse getResponseExistingImperialSpoonInvalidConversionFactor() {
        return new UseCasePortionCalculatorResponse(
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor(),
                ResultStatus.INVALID_CONVERSION_FACTOR);
    }
}
