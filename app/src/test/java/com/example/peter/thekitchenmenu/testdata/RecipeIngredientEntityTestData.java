package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;

public class RecipeIngredientEntityTestData {

    public static RecipeIngredientEntity getNewInvalid() {
        return new RecipeIngredientEntity(
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

    public static RecipeIngredientEntity getNewValidMetric() {
        return new RecipeIngredientEntity(
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

    public static RecipeIngredientEntity getNewValidImperial() {
        return new RecipeIngredientEntity(
                getNewValidMetric().getId(),
                RecipeEntityTestData.getValidNew().getId(),
                IngredientEntityTestData.getNewValidName().getId(),
                "",
                144.5825679375, // 5.1lbs
                1,
                Constants.getUserId().getValue(),
                20L,
                20L
        );
    }

    public static RecipeIngredientEntity getExistingValidMetric() {
        return new RecipeIngredientEntity(
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

    public static RecipeIngredientEntity getExistingValidMetricMeasurementUpdated() {
        return new RecipeIngredientEntity(
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
}
