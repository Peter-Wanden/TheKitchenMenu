package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientcalculator.UseCaseIngredientCalculatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientcalculator.UseCaseIngredientCalculatorResponse;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeingredientcalculator.UseCaseIngredientCalculator.*;

public class TestDataUseCasePortionCalculatorRequestResponse {

    private static String NO_RECIPE_ID = "";
    private static String NO_INGREDIENT_ID = "";
    private static String NO_RECIPE_INGREDIENT_ID = "";

    public static UseCaseIngredientCalculatorRequest getRequestEmptyFourPortions() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static UseCaseIngredientCalculatorResponse getResponseEmptyFourPortions() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getInvalidEmptyFourPortionsSet(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static UseCaseIngredientCalculatorRequest getRequestInvalidTotalUnitOne() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitOne());
    }
    public static UseCaseIngredientCalculatorResponse getResponseInvalidTotalUnitOne() {

        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidTotalUnitOne(),
                ResultStatus.INVALID_TOTAL_UNIT_ONE);
    }

    public static UseCaseIngredientCalculatorRequest getRequestValidTotalUnitOne() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitOne());
    }
    public static UseCaseIngredientCalculatorResponse getResponseValidTotalUnitOne() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getNewValidTotalUnitOne(),
                ResultStatus.RESULT_OK);
    }

    public static UseCaseIngredientCalculatorRequest getRequestInvalidTotalUnitTwo() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo());
    }
    public static UseCaseIngredientCalculatorResponse getResponseInvalidTotalUnitTwo() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo(),
                ResultStatus.INVALID_TOTAL_UNIT_TWO);
    }

    public static UseCaseIngredientCalculatorRequest getRequestValidTotalUnitTwo() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitTwo());
    }
    public static UseCaseIngredientCalculatorResponse getResponseValidTotalUnitTwo() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getNewValidTotalUnitTwo(),
                ResultStatus.RESULT_OK);
    }

    public static UseCaseIngredientCalculatorRequest getRequestUnitOfMeasureChangeImperialSpoon() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon());
    }
    public static UseCaseIngredientCalculatorResponse getResponseUnitOfMeasureChangeImperialSpoon() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static UseCaseIngredientCalculatorRequest getRequestNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated());
    }
    public static UseCaseIngredientCalculatorResponse getResponseNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated(),
                ResultStatus.RESULT_OK);
    }

    public static UseCaseIngredientCalculatorRequest getRequestNewValidImperialSpoonInvalidConversionFactor() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidConversionFactor());
    }
    public static UseCaseIngredientCalculatorResponse getResponseNewValidImperialSpoonInvalidConversionFactor() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getNewInvalidConversionFactor(),
                ResultStatus.INVALID_CONVERSION_FACTOR);
    }

    public static UseCaseIngredientCalculatorRequest getRequestNewValidImperialSpoonValidConversionFactor() {
        return new UseCaseIngredientCalculatorRequest(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor());
    }
    public static UseCaseIngredientCalculatorResponse getResponseNewValidImperialSpoonValidConversionFactor() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor(),
                ResultStatus.RESULT_OK);
    }

    public static UseCaseIngredientCalculatorRequest getRequestExistingValidMetric() {
        return new UseCaseIngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static UseCaseIngredientCalculatorResponse getResponseExistingValidMetric() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getValidExistingMetric(),
                ResultStatus.RESULT_OK);
    }

    public static UseCaseIngredientCalculatorRequest getRequestExistingMetricInvalidTotalUnitOne() {
        return new UseCaseIngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne());
    }
    public static UseCaseIngredientCalculatorResponse getResponseExistingMetricInvalidTotalUnitOne() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne(),
                ResultStatus.INVALID_TOTAL_UNIT_ONE);
    }

    public static UseCaseIngredientCalculatorRequest getRequestExistingMetricValidTotalUnitOne() {
        return new UseCaseIngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidUnitOneUpdated()
        );
    }
    public static UseCaseIngredientCalculatorResponse getResponseExistingMetricValidTotalUnitOne() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricValidTotalOne(),
                ResultStatus.RESULT_OK
        );
    }

    public static UseCaseIngredientCalculatorRequest getRequestExistingMetricValidTotalUnitTwo() {
        return new UseCaseIngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated());
    }
    public static UseCaseIngredientCalculatorResponse getResponseExistingMetricValidTotalUnitTwo() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated(),
                ResultStatus.RESULT_OK);
    }

    public static UseCaseIngredientCalculatorRequest getRequestExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new UseCaseIngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial());
    }
    public static UseCaseIngredientCalculatorResponse getResponseExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static UseCaseIngredientCalculatorRequest getRequestExistingValidImperialSpoon() {
        return new UseCaseIngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static UseCaseIngredientCalculatorResponse getResponseExistingImperialSpoon() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingImperialSpoonValidConversionFactorNotSet(),
                ResultStatus.RESULT_OK
        );
    }

    public static UseCaseIngredientCalculatorRequest getRequestExistingImperialSpoonInvalidConversionFactor() {
        return new UseCaseIngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor());
    }
    public static UseCaseIngredientCalculatorResponse getResponseExistingImperialSpoonInvalidConversionFactor() {
        return new UseCaseIngredientCalculatorResponse(
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor(),
                ResultStatus.INVALID_CONVERSION_FACTOR);
    }
}
