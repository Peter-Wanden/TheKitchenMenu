package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.*;

public class TestDataRecipeIngredientQuantityEntity {

    public static RecipeIngredientEntity getNewInvalid() {
        return new RecipeIngredientEntity(
                "newId",
                TestDataRecipeMetadataEntity.getNewInvalid().getDataId(),
                TestDataIngredientEntity.getNew().getDataId(),
                TestDataProductEntity.getNewInvalid().getDataId(),
                0,
                MeasurementSubtype.METRIC_MASS.asInt(),
                Constants.getUserId(),
                10L,
                10L
        );
    }

    public static RecipeIngredientEntity getNewValidMetric() {
        return new RecipeIngredientEntity(
                "new_valid_measurement_one_id",
                TestDataRecipeMetadataEntity.getNewValid().getDataId(),
                TestDataIngredientEntity.getNewValidName().getDataId(),
                "",
                150, // per portion value
                MeasurementSubtype.METRIC_MASS.asInt(),
                Constants.getUserId(),
                10L,
                10L
        );
    }

    public static RecipeIngredientEntity getNewValidImperial() {
        return new RecipeIngredientEntity(
                getNewValidMetric().getDataId(),
                TestDataRecipeMetadataEntity.getNewValid().getDataId(),
                TestDataIngredientEntity.getNewValidName().getDataId(),
                "",
                36.145641984375, // 5.1lbs / 4 portions
                MeasurementSubtype.IMPERIAL_MASS.asInt(),
                Constants.getUserId(),
                20L,
                20L
        );
    }

    public static RecipeIngredientEntity getNewValidMetricMaxMassDivFourPortions() {
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.fromInt(
                getNewValidMetric().getMeasurementSubtype()).getMeasurementClass();

        unitOfMeasure.isTotalBaseUnitsSet(MAX_MASS);

        int portions = TestDataRecipePortionsEntity.getNewValidFourPortions().getSittings() *
                TestDataRecipePortionsEntity.getNewValidFourPortions().getServings();
        unitOfMeasure.isNumberOfItemsSet(portions);

        return new RecipeIngredientEntity(
                getNewValidMetric().getDataId(),
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

    public static RecipeIngredientEntity getNewValidImperialOneTeaspoonFourPortionsNoConversionFactor() {
        return new RecipeIngredientEntity(
                getNewInvalid().getDataId(),
                getNewInvalid().getRecipeId(),
                getNewInvalid().getIngredientId(),
                "",
                1.25,
                MeasurementSubtype.IMPERIAL_SPOON.asInt(),
                Constants.getUserId(),
                40L,
                40L
        );
    }

    public static RecipeIngredientEntity getNewValidImperialOneTeaspoonConversionFactorApplied() {
        return new RecipeIngredientEntity(
                getNewInvalid().getDataId(),
                getNewInvalid().getRecipeId(),
                getNewInvalid().getIngredientId(),
                "",
                getItemBaseUnitsWithConversionFactorApplied(),
                MeasurementSubtype.IMPERIAL_SPOON.asInt(),
                Constants.getUserId(),
                40L,
                40L
        );
    }

    public static RecipeIngredientEntity getNewValidImperialSpoonMaxConversionFactor() {
        return new RecipeIngredientEntity(
                getNewValidMetric().getDataId(),
                getNewInvalid().getRecipeId(),
                getNewInvalid().getIngredientId(),
                "",
                 7.5,
                getNewValidImperialOneTeaspoonConversionFactorApplied().getMeasurementSubtype(),
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

    public static RecipeIngredientEntity getExistingValidMetric() {
        return new RecipeIngredientEntity(
                "existing_valid_id",
                TestDataRecipeMetadataEntity.getValidExisting().getDataId(),
                TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor().getDataId(),
                TestDataProductEntity.getExistingValid().getDataId(),
                250,
                MeasurementSubtype.METRIC_VOLUME.asInt(),
                Constants.getUserId(),
                30L,
                30L
        );
    }

    public static RecipeIngredientEntity getExistingValidMetricMeasurementUpdated() {
        return new RecipeIngredientEntity(
                getExistingValidMetric().getDataId(),
                getExistingValidMetric().getRecipeId(),
                getExistingValidMetric().getIngredientId(),
                getExistingValidMetric().getProductId(),
                getExistingValidMetric().getItemBaseUnits() + 250,
                getExistingValidMetric().getMeasurementSubtype(),
                getExistingValidMetric().getCreatedBy(),
                getExistingValidMetric().getCreateDate(),
                getExistingValidMetric().getLastUpdate() + 10
        );
    }

    public static RecipeIngredientEntity getExistingValidImperialTwoSpoons() {

        UnitOfMeasure unitOfMeasure = MeasurementSubtype.IMPERIAL_SPOON.getMeasurementClass();
        int portions = TestDataRecipePortionsEntity.getExistingValidNinePortions().getServings() *
                TestDataRecipePortionsEntity.getExistingValidNinePortions().getSittings();
        unitOfMeasure.isNumberOfItemsSet(portions);
        unitOfMeasure.isTotalUnitOneSet((2));

        return new RecipeIngredientEntity(
                "existing_valid_imperialSpoon_id",
                TestDataRecipeMetadataEntity.getValidExisting().getDataId(),
                TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor().getDataId(),
                TestDataProductEntity.getExistingValid().getDataId(),
                5,
                MeasurementSubtype.IMPERIAL_SPOON.asInt(),
                Constants.getUserId(),
                40L,
                40L
        );
    }
}
