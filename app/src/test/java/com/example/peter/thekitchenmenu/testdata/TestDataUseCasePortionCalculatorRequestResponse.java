package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator.IngredientCalculatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator.IngredientCalculatorResponse;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator.IngredientCalculator.*;

public class TestDataUseCasePortionCalculatorRequestResponse {

    private static String NO_RECIPE_ID = "";
    private static String NO_INGREDIENT_ID = "";
    private static String NO_RECIPE_INGREDIENT_ID = "";

    public static IngredientCalculatorRequest getRequestEmptyFourPortions() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static IngredientCalculatorResponse getResponseEmptyFourPortions() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getInvalidEmptyFourPortionsSet(),
                Result.INVALID_MEASUREMENT);
    }

    public static IngredientCalculatorRequest getRequestInvalidTotalUnitOne() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitOne());
    }
    public static IngredientCalculatorResponse getResponseInvalidTotalUnitOne() {

        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidTotalUnitOne(),
                Result.INVALID_TOTAL_UNIT_ONE);
    }

    public static IngredientCalculatorRequest getRequestValidTotalUnitOne() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitOne());
    }
    public static IngredientCalculatorResponse getResponseValidTotalUnitOne() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getNewValidTotalUnitOne(),
                Result.RESULT_OK);
    }

    public static IngredientCalculatorRequest getRequestInvalidTotalUnitTwo() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo());
    }
    public static IngredientCalculatorResponse getResponseInvalidTotalUnitTwo() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo(),
                Result.INVALID_TOTAL_UNIT_TWO);
    }

    public static IngredientCalculatorRequest getRequestValidTotalUnitTwo() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitTwo());
    }
    public static IngredientCalculatorResponse getResponseValidTotalUnitTwo() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getNewValidTotalUnitTwo(),
                Result.RESULT_OK);
    }

    public static IngredientCalculatorRequest getRequestUnitOfMeasureChangeImperialSpoon() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon());
    }
    public static IngredientCalculatorResponse getResponseUnitOfMeasureChangeImperialSpoon() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                Result.INVALID_MEASUREMENT);
    }

    public static IngredientCalculatorRequest getRequestNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated());
    }
    public static IngredientCalculatorResponse getResponseNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated(),
                Result.RESULT_OK);
    }

    public static IngredientCalculatorRequest getRequestNewValidImperialSpoonInvalidConversionFactor() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidConversionFactor());
    }
    public static IngredientCalculatorResponse getResponseNewValidImperialSpoonInvalidConversionFactor() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidConversionFactor(),
                Result.INVALID_CONVERSION_FACTOR);
    }

    public static IngredientCalculatorRequest getRequestNewValidImperialSpoonValidConversionFactor() {
        return new IngredientCalculatorRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor());
    }
    public static IngredientCalculatorResponse getResponseNewValidImperialSpoonValidConversionFactor() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor(),
                Result.RESULT_OK);
    }

    public static IngredientCalculatorRequest getRequestExistingValidMetric() {
        return new IngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static IngredientCalculatorResponse getResponseExistingValidMetric() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getValidExistingMetric(),
                Result.RESULT_OK);
    }

    public static IngredientCalculatorRequest getRequestExistingMetricInvalidTotalUnitOne() {
        return new IngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne());
    }
    public static IngredientCalculatorResponse getResponseExistingMetricInvalidTotalUnitOne() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne(),
                Result.INVALID_TOTAL_UNIT_ONE);
    }

    public static IngredientCalculatorRequest getRequestExistingMetricValidTotalUnitOne() {
        return new IngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidUnitOneUpdated()
        );
    }
    public static IngredientCalculatorResponse getResponseExistingMetricValidTotalUnitOne() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricValidTotalOne(),
                Result.RESULT_OK
        );
    }

    public static IngredientCalculatorRequest getRequestExistingMetricValidTotalUnitTwo() {
        return new IngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated());
    }
    public static IngredientCalculatorResponse getResponseExistingMetricValidTotalUnitTwo() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated(),
                Result.RESULT_OK);
    }

    public static IngredientCalculatorRequest getRequestExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new IngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial());
    }
    public static IngredientCalculatorResponse getResponseExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial(),
                Result.INVALID_MEASUREMENT);
    }

    public static IngredientCalculatorRequest getRequestExistingValidImperialSpoon() {
        return new IngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static IngredientCalculatorResponse getResponseExistingImperialSpoon() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingImperialSpoonValidConversionFactorNotSet(),
                Result.RESULT_OK
        );
    }

    public static IngredientCalculatorRequest getRequestExistingImperialSpoonInvalidConversionFactor() {
        return new IngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor());
    }
    public static IngredientCalculatorResponse getResponseExistingImperialSpoonInvalidConversionFactor() {
        return new IngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor(),
                Result.INVALID_CONVERSION_FACTOR);
    }
}
