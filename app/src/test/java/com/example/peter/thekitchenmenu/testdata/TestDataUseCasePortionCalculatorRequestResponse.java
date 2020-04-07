package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientResponse;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredient.*;

public class TestDataUseCasePortionCalculatorRequestResponse {

    private static String NO_RECIPE_ID = "";
    private static String NO_INGREDIENT_ID = "";
    private static String NO_RECIPE_INGREDIENT_ID = "";

    public static RecipeIngredientRequest getRequestEmptyFourPortions() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static RecipeIngredientResponse getResponseEmptyFourPortions() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getInvalidEmptyFourPortionsSet(),
                Result.INVALID_MEASUREMENT);
    }

    public static RecipeIngredientRequest getRequestInvalidTotalUnitOne() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitOne());
    }
    public static RecipeIngredientResponse getResponseInvalidTotalUnitOne() {

        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getNewInvalidTotalUnitOne(),
                Result.INVALID_TOTAL_UNIT_ONE);
    }

    public static RecipeIngredientRequest getRequestValidTotalUnitOne() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitOne());
    }
    public static RecipeIngredientResponse getResponseValidTotalUnitOne() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getNewValidTotalUnitOne(),
                Result.RESULT_OK);
    }

    public static RecipeIngredientRequest getRequestInvalidTotalUnitTwo() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo());
    }
    public static RecipeIngredientResponse getResponseInvalidTotalUnitTwo() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo(),
                Result.INVALID_TOTAL_UNIT_TWO);
    }

    public static RecipeIngredientRequest getRequestValidTotalUnitTwo() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidTotalUnitTwo());
    }
    public static RecipeIngredientResponse getResponseValidTotalUnitTwo() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getNewValidTotalUnitTwo(),
                Result.RESULT_OK);
    }

    public static RecipeIngredientRequest getRequestUnitOfMeasureChangeImperialSpoon() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon());
    }
    public static RecipeIngredientResponse getResponseUnitOfMeasureChangeImperialSpoon() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                Result.INVALID_MEASUREMENT);
    }

    public static RecipeIngredientRequest getRequestNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated());
    }
    public static RecipeIngredientResponse getResponseNewValidImperialSpoonUnitOneUpdatedHalfSpoon() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated(),
                Result.RESULT_OK);
    }

    public static RecipeIngredientRequest getRequestNewValidImperialSpoonInvalidConversionFactor() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewInvalidConversionFactor());
    }
    public static RecipeIngredientResponse getResponseNewValidImperialSpoonInvalidConversionFactor() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getNewInvalidConversionFactor(),
                Result.INVALID_CONVERSION_FACTOR);
    }

    public static RecipeIngredientRequest getRequestNewValidImperialSpoonValidConversionFactor() {
        return new RecipeIngredientRequest(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_RECIPE_INGREDIENT_ID,
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor());
    }
    public static RecipeIngredientResponse getResponseNewValidImperialSpoonValidConversionFactor() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor(),
                Result.RESULT_OK);
    }

    public static RecipeIngredientRequest getRequestExistingValidMetric() {
        return new RecipeIngredientRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static RecipeIngredientResponse getResponseExistingValidMetric() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getValidExistingMetric(),
                Result.RESULT_OK);
    }

    public static RecipeIngredientRequest getRequestExistingMetricInvalidTotalUnitOne() {
        return new RecipeIngredientRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne());
    }
    public static RecipeIngredientResponse getResponseExistingMetricInvalidTotalUnitOne() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne(),
                Result.INVALID_TOTAL_UNIT_ONE);
    }

    public static RecipeIngredientRequest getRequestExistingMetricValidTotalUnitOne() {
        return new RecipeIngredientRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidUnitOneUpdated()
        );
    }
    public static RecipeIngredientResponse getResponseExistingMetricValidTotalUnitOne() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getExistingMetricValidTotalOne(),
                Result.RESULT_OK
        );
    }

    public static RecipeIngredientRequest getRequestExistingMetricValidTotalUnitTwo() {
        return new RecipeIngredientRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated());
    }
    public static RecipeIngredientResponse getResponseExistingMetricValidTotalUnitTwo() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated(),
                Result.RESULT_OK);
    }

    public static RecipeIngredientRequest getRequestExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new RecipeIngredientRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getId(),
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial());
    }
    public static RecipeIngredientResponse getResponseExistingMetricUnitOfMeasureChangedToImperialSpoon() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial(),
                Result.INVALID_MEASUREMENT);
    }

    public static RecipeIngredientRequest getRequestExistingValidImperialSpoon() {
        return new RecipeIngredientRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                DEFAULT_MEASUREMENT_MODEL);
    }
    public static RecipeIngredientResponse getResponseExistingImperialSpoon() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getExistingImperialSpoonValidConversionFactorNotSet(),
                Result.RESULT_OK
        );
    }

    public static RecipeIngredientRequest getRequestExistingImperialSpoonInvalidConversionFactor() {
        return new RecipeIngredientRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons().getId(),
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor());
    }
    public static RecipeIngredientResponse getResponseExistingImperialSpoonInvalidConversionFactor() {
        return new RecipeIngredientResponse(
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor(),
                Result.INVALID_CONVERSION_FACTOR);
    }
}
