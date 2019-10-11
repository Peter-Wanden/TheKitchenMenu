package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;

public class RecipeIngredientQuantityEntityTestData {

    public static RecipeIngredientQuantityEntity getNewInvalid() {
        return new RecipeIngredientQuantityEntity(
                "newId",
                RecipeEntityTestData.getInvalidNew().getId(),
                IngredientEntityTestData.getNew().getId(),
                ProductEntityTestData.getNewInvalid().getId(),
                0,
                0,
                Constants.getUserId().getValue(),
                10L,
                10L
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidMetric() {
        return new RecipeIngredientQuantityEntity(
                "new_valid_measurement_one_id",
                RecipeEntityTestData.getValidNew().getId(),
                IngredientEntityTestData.getNewValidName().getId(),
                "",
                150, // per portion value
                0,
                Constants.getUserId().getValue(),
                10L,
                10L
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidImperial() {
        return new RecipeIngredientQuantityEntity(
                getNewValidMetric().getId(),
                RecipeEntityTestData.getValidNew().getId(),
                IngredientEntityTestData.getNewValidName().getId(),
                "",
                36.145641984375, // 5.1lbs / 4 portions
                1,
                Constants.getUserId().getValue(),
                20L,
                20L
        );
    }

    public static RecipeIngredientQuantityEntity getExistingValidMetric() {
        return new RecipeIngredientQuantityEntity(
                "existing_valid_id",
                RecipeEntityTestData.getValidExisting().getId(),
                IngredientEntityTestData.getExistingValidNameValidDescription().getId(),
                ProductEntityTestData.getExistingValid().getId(),
                250,
                2,
                Constants.getUserId().getValue(),
                30L,
                30L
        );
    }

    public static RecipeIngredientQuantityEntity getExistingValidMetricMeasurementUpdated() {
        return new RecipeIngredientQuantityEntity(
                getExistingValidMetric().getId(),
                getExistingValidMetric().getRecipeId(),
                getExistingValidMetric().getIngredientId(),
                getExistingValidMetric().getProductId(),
                getExistingValidMetric().getItemBaseUnits() + 250,
                getExistingValidMetric().getUnitOfMeasureSubtype(),
                getExistingValidMetric().getCreatedBy(),
                getExistingValidMetric().getCreateDate(),
                getExistingValidMetric().getLastUpdate() + 10
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidImperialOneTeaspoonNoConversionFactor() {
        return new RecipeIngredientQuantityEntity(
                getNewInvalid().getId(),
                getNewInvalid().getRecipeId(),
                getNewInvalid().getIngredientId(),
                "",
                1.25,
                MeasurementSubtype.IMPERIAL_SPOON.asInt(),
                Constants.getUserId().getValue(),
                40L,
                40L
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidImperialOneTeaspoonConversionFactorApplied() {
        return new RecipeIngredientQuantityEntity(
                getNewInvalid().getId(),
                getNewInvalid().getRecipeId(),
                getNewInvalid().getIngredientId(),
                "",
                getItemBaseUnits(),
                MeasurementSubtype.IMPERIAL_SPOON.asInt(),
                Constants.getUserId().getValue(),
                40L,
                40L
        );
    }

    private static double getItemBaseUnits() {
        double conversionFactor = IngredientEntityTestData.
                        getNewValidNameValidDescriptionConversionFactorUpdated().
                        getConversionFactor();

        int numberOfPortions = RecipePortionsEntityTestData.getNewValidFourPortions().getServings() *
                RecipePortionsEntityTestData.getNewValidFourPortions().getServings();

        double numberOfTeaspoons = 1.;
        double volumeOfTeaspoon = 5;

        return numberOfTeaspoons * volumeOfTeaspoon * conversionFactor / numberOfPortions;
    }
}
