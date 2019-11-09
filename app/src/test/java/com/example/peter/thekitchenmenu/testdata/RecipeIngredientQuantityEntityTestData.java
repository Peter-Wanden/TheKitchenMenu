package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class RecipeIngredientQuantityEntityTestData {

    public static RecipeIngredientQuantityEntity getNewInvalid() {
        return new RecipeIngredientQuantityEntity(
                "newId",
                RecipeEntityTestData.getNewInvalid().getId(),
                IngredientEntityTestData.getNew().getId(),
                ProductEntityTestData.getNewInvalid().getId(),
                0,
                MeasurementSubtype.METRIC_MASS.asInt(),
                Constants.getUserId().getValue(),
                10L,
                10L
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidMetric() {
        return new RecipeIngredientQuantityEntity(
                "new_valid_measurement_one_id",
                RecipeEntityTestData.getNewValid().getId(),
                IngredientEntityTestData.getNewValidName().getId(),
                "",
                150, // per portion value
                MeasurementSubtype.METRIC_MASS.asInt(),
                Constants.getUserId().getValue(),
                10L,
                10L
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidImperial() {
        return new RecipeIngredientQuantityEntity(
                getNewValidMetric().getId(),
                RecipeEntityTestData.getNewValid().getId(),
                IngredientEntityTestData.getNewValidName().getId(),
                "",
                36.145641984375, // 5.1lbs / 4 portions
                MeasurementSubtype.IMPERIAL_MASS.asInt(),
                Constants.getUserId().getValue(),
                20L,
                20L
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidMetricMaxMassDivFourPortions() {
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.fromInt(
                getNewValidMetric().getUnitOfMeasureSubtype()).getMeasurementClass();

        unitOfMeasure.totalBaseUnitsAreSet(MAX_MASS);

        int portions = RecipePortionsEntityTestData.getNewValidFourPortions().getSittings() *
                RecipePortionsEntityTestData.getNewValidFourPortions().getServings();
        unitOfMeasure.numberOfItemsIsSet(portions);

        return new RecipeIngredientQuantityEntity(
                getNewValidMetric().getId(),
                getNewValidMetric().getRecipeId(),
                getNewValidMetric().getIngredientId(),
                getNewValidMetric().getProductId(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getMeasurementSubtype().asInt(),
                getNewValidMetric().getCreatedBy(),
                getNewValidMetric().getCreateDate(),
                getNewValidMetric().getLastUpdate()
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidImperialOneTeaspoonFourPortionsNoConversionFactor() {
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
                getItemBaseUnitsWithConversionFactorApplied(),
                MeasurementSubtype.IMPERIAL_SPOON.asInt(),
                Constants.getUserId().getValue(),
                40L,
                40L
        );
    }

    public static RecipeIngredientQuantityEntity getNewValidImperialSpoonMaxConversionFactor() {
        return new RecipeIngredientQuantityEntity(
                getNewValidMetric().getId(),
                getNewInvalid().getRecipeId(),
                getNewInvalid().getIngredientId(),
                "",
                 7.5,
                getNewValidImperialOneTeaspoonConversionFactorApplied().getUnitOfMeasureSubtype(),
                getNewInvalid().getCreatedBy(),
                10L,
                10L
        );
    }

    private static double getItemBaseUnitsWithConversionFactorApplied() {
        double conversionFactor = IngredientEntityTestData.
                        getNewValidNameValidDescriptionConversionFactorUpdated().
                        getConversionFactor();

        int numberOfPortions = RecipePortionsEntityTestData.getNewValidFourPortions().getServings() *
                RecipePortionsEntityTestData.getNewValidFourPortions().getServings();

        double numberOfTeaspoons = 1.;
        double volumeOfTeaspoon = 5;

        return numberOfTeaspoons * volumeOfTeaspoon * conversionFactor / numberOfPortions;
    }

    public static RecipeIngredientQuantityEntity getExistingValidMetric() {
        return new RecipeIngredientQuantityEntity(
                "existing_valid_id",
                RecipeEntityTestData.getValidExisting().getId(),
                IngredientEntityTestData.getExistingValidNameValidDescription().getId(),
                ProductEntityTestData.getExistingValid().getId(),
                250,
                MeasurementSubtype.METRIC_VOLUME.asInt(),
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

    public static RecipeIngredientQuantityEntity getExistingValidImperialSpoon() {

        UnitOfMeasure unitOfMeasure = MeasurementSubtype.IMPERIAL_SPOON.getMeasurementClass();

        return new RecipeIngredientQuantityEntity(
                "existing_valid_imperialSpoon_id",
                RecipeEntityTestData.getValidExisting().getId(),
                IngredientEntityTestData.getExistingValidNameValidDescription().getId(),
                ProductEntityTestData.getExistingValid().getId(),
                5,
                MeasurementSubtype.IMPERIAL_SPOON.asInt(),
                Constants.getUserId().getValue(),
                40L,
                40L
        );
    }
}
