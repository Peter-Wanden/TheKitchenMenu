package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;

import static com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants.*;

public class TestDataRecipeIngredientQuantityEntity {

    public static RecipeIngredientQuantityEntity getNewInvalid() {
        return new RecipeIngredientQuantityEntity(
                "newId",
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNew().getId(),
                TestDataProductEntity.getNewInvalid().getId(),
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
                TestDataRecipeEntity.getNewValid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
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
                TestDataRecipeEntity.getNewValid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
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

        unitOfMeasure.isTotalBaseUnitsSet(MAX_MASS);

        int portions = TestDataRecipePortionsEntity.getNewValidFourPortions().getSittings() *
                TestDataRecipePortionsEntity.getNewValidFourPortions().getServings();
        unitOfMeasure.isNumberOfItemsSet(portions);

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
        double conversionFactor = TestDataIngredientEntity.
                        getNewValidNameValidDescriptionConversionFactorUpdated().
                        getConversionFactor();

        int numberOfPortions = TestDataRecipePortionsEntity.getNewValidFourPortions().getServings() *
                TestDataRecipePortionsEntity.getNewValidFourPortions().getServings();

        double numberOfTeaspoons = 1.;
        double volumeOfTeaspoon = 5;

        return numberOfTeaspoons * volumeOfTeaspoon * conversionFactor / numberOfPortions;
    }

    public static RecipeIngredientQuantityEntity getExistingValidMetric() {
        return new RecipeIngredientQuantityEntity(
                "existing_valid_id",
                TestDataRecipeEntity.getValidExisting().getId(),
                TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor().getId(),
                TestDataProductEntity.getExistingValid().getId(),
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

    public static RecipeIngredientQuantityEntity getExistingValidImperialTwoSpoons() {

        UnitOfMeasure unitOfMeasure = MeasurementSubtype.IMPERIAL_SPOON.getMeasurementClass();
        int portions = TestDataRecipePortionsEntity.getExistingValidNinePortions().getServings() *
                TestDataRecipePortionsEntity.getExistingValidNinePortions().getSittings();
        unitOfMeasure.isNumberOfItemsSet(portions);
        unitOfMeasure.isTotalUnitOneSet((2));

        return new RecipeIngredientQuantityEntity(
                "existing_valid_imperialSpoon_id",
                TestDataRecipeEntity.getValidExisting().getId(),
                TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor().getId(),
                TestDataProductEntity.getExistingValid().getId(),
                5,
                MeasurementSubtype.IMPERIAL_SPOON.asInt(),
                Constants.getUserId().getValue(),
                40L,
                40L
        );
    }
}
