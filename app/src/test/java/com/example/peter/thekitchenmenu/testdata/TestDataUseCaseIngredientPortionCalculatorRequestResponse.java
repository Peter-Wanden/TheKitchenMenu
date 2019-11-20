package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator.ResponseValues;
import com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator.RequestValues;

import static com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL;
import static com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator.*;

public class TestDataUseCaseIngredientPortionCalculatorRequestResponse {

    private static String NO_RECIPE_ID = "";
    private static String NO_INGREDIENT_ID;
    private static String NO_RECIPE_INGREDIENT_ID = "";

    public static RequestValues getRequestEmptyFourPortions() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static ResponseValues getResponseEmptyFourPortions() {
        return new ResponseValues(TestDataMeasurementModel.getInvalidEmptyFourPortionsSet(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static RequestValues getRequestInvalidTotalUnitOne() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitOne());
    }
    public static ResponseValues getResponseInvalidTotalUnitOne() {
        return new ResponseValues(TestDataMeasurementModel.getNewInvalidTotalUnitOne(),
                ResultStatus.INVALID_TOTAL_UNIT_ONE);
    }

    public static RequestValues getRequestValidTotalUnitOne() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitOne());
    }
    public static ResponseValues getResponseValidTotalUnitOne() {
        return new ResponseValues(TestDataMeasurementModel.getNewValidTotalUnitOne(),
                ResultStatus.RESULT_OK);
    }

    public static RequestValues getRequestInvalidTotalUnitTwo() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo());
    }
    public static ResponseValues getResponseInvalidTotalUnitTwo() {
        return new ResponseValues(TestDataMeasurementModel.getNewInvalidTotalUnitTwo(),
                ResultStatus.INVALID_TOTAL_UNIT_TWO);
    }

    public static RequestValues getRequestValidTotalUnitTwo() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitTwo());
    }
    public static ResponseValues getResponseValidTotalUnitTwo() {
        return new ResponseValues(TestDataMeasurementModel.getNewValidTotalUnitTwo(),
                ResultStatus.RESULT_OK);
    }

    public static RequestValues getRequestUnitOfMeasureChangeImperialSpoon() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon());
    }
    public static ResponseValues getResponseUnitOfMeasureChangeImperialSpoon() {
        return new ResponseValues(TestDataMeasurementModel.
                getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static RequestValues getRequestNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated());
    }
    public static ResponseValues getResponseNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new ResponseValues(TestDataMeasurementModel.
                getNewValidHalfImperialSpoonUnitOneUpdated(),
                ResultStatus.RESULT_OK);
    }

    public static RequestValues getRequestNewValidImperialSpoonInvalidConversionFactor() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidConversionFactor());
    }
    public static ResponseValues getResponseNewValidImperialSpoonInvalidConversionFactor() {
        return new ResponseValues(
                TestDataMeasurementModel.getNewInvalidConversionFactor(),
                ResultStatus.INVALID_CONVERSION_FACTOR);
    }

    public static RequestValues getRequestNewValidImperialSpoonValidConversionFactor() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor());
    }
    public static ResponseValues getResponseNewValidImperialSpoonValidConversionFactor() {
        return new ResponseValues(
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor(),
                ResultStatus.RESULT_OK);
    }

    public static RequestValues getRequestExistingValidMetric() {
        return new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static ResponseValues getResponseExistingValidMetric() {
        return new ResponseValues(
                TestDataMeasurementModel.getValidExistingMetric(),
                ResultStatus.RESULT_OK);
    }

    public static RequestValues getRequestExistingMetricInvalidTotalUnitOne() {
        return new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne());
    }
    public static ResponseValues getResponseExistingMetricInvalidTotalUnitOne() {
        return new ResponseValues(
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne(),
                ResultStatus.INVALID_TOTAL_UNIT_ONE);
    }

    public static RequestValues getRequestExistingMetricValidTotalUnitTwo() {
        return new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated());
    }
    public static ResponseValues getResponseExistingMetricValidTotalUnitTwo() {
        return new ResponseValues(
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated(),
                ResultStatus.RESULT_OK);
    }

    public static RequestValues getRequestExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial());
    }
    public static ResponseValues getResponseExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new ResponseValues(
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial(),
                ResultStatus.INVALID_MEASUREMENT);
    }

    public static RequestValues getRequestExistingValidImperialSpoon() {
        return new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static ResponseValues getResponseExistingImperialSpoon() {
        return new ResponseValues(
                TestDataMeasurementModel.getExistingImperialSpoonValidConversionFactorNotSet(),
                ResultStatus.RESULT_OK
        );
    }

    public static RequestValues getRequestExistingImperialSpoonInvalidConversionFactor() {
        return new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor());
    }

    public static ResponseValues getResponseExistingImperialSpoonInvalidConversionFactor() {
        return new ResponseValues(
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor(),
                ResultStatus.INVALID_CONVERSION_FACTOR);
    }
}
