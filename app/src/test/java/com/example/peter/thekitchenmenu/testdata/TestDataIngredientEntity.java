package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;

import java.util.ArrayList;
import java.util.List;

public class TestDataIngredientEntity {

    public static IngredientEntity getNew() {
        return new IngredientEntity(
                "newId",
                "",
                "",
                1,
                Constants.getUserId().getValue(),
                10L,
                10L);
    }

    public static IngredientEntity getNewInvalidName() {
        return new IngredientEntity(
                getNew().getId(),
                "na",
                getNew().getDescription(),
                1,
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                20L
        );
    }

    public static IngredientEntity getNewValidName() {
        return new IngredientEntity(
                getNew().getId(),
                "name",
                getNew().getDescription(),
                1,
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                30L
        );
    }

    public static IngredientEntity getNewValidNameMaxConversionFactor() {
        return new IngredientEntity(
                getNew().getId(),
                getNewValidName().getName(),
                getNew().getDescription(),
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                getNew().getLastUpdate()
        );
    }

    public static IngredientEntity getNewValidNameInvalidDescription() {
        return new IngredientEntity(
                getNew().getId(),
                getNewValidName().getName(),
                "de",
                1,
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                40L
        );
    }

    public static IngredientEntity getNewInvalidNameValidDescription() {
        return new IngredientEntity(
                getNew().getId(),
                getNewInvalidName().getName(),
                "description",
                1,
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                50L
        );
    }

    public static IngredientEntity getNewValidNameValidDescription() {
        return new IngredientEntity(
                getNew().getId(),
                getNewValidName().getName(),
                getNewInvalidNameValidDescription().getDescription(),
                1,
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                60L
        );
    }

    public static IngredientEntity getNewValidNameValidDescriptionConversionFactorUpdated() {
        return new IngredientEntity(
                getNew().getId(),
                getNewValidName().getName(),
                getNewInvalidNameValidDescription().getDescription(),
                0.5,
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                70L
        );
    }

    public static IngredientEntity getExistingValidNameValidDescriptionNoConversionFactor() {
        return new IngredientEntity(
                "existingId",
                "existingName",
                "existingDescription",
                1,
                Constants.getUserId().getValue(),
                70L,
                80L

        );
    }

    public static IngredientEntity getExistingUpdatedWithInvalidName() {
        return new IngredientEntity(
                getExistingValidNameValidDescriptionNoConversionFactor().getId(),
                "na",
                getExistingValidNameValidDescriptionNoConversionFactor().getDescription(),
                1,
                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
                getExistingValidNameValidDescriptionNoConversionFactor().getLastUpdate()
        );
    }

    public static IngredientEntity getExistingUpdatedWithValidName() {
        return new IngredientEntity(
                getExistingValidNameValidDescriptionNoConversionFactor().getId(),
                "existingNameUpdated",
                getExistingValidNameValidDescriptionNoConversionFactor().getDescription(),
                1,
                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
                90L
        );
    }

    public static IngredientEntity getExistingUpdatedWithInvalidDescription() {
        return new IngredientEntity(
                getExistingValidNameValidDescriptionNoConversionFactor().getId(),
                getExistingValidNameValidDescriptionNoConversionFactor().getName(),
                "de",
                1,
                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
                90L
        );
    }

    public static IngredientEntity getExistingUpdatedWithValidDescription() {
        return new IngredientEntity(
                getExistingValidNameValidDescriptionNoConversionFactor().getId(),
                getExistingValidNameValidDescriptionNoConversionFactor().getName(),
                "existingDescriptionUpdated",
                1,
                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
                100L
        );
    }

    public static IngredientEntity getExistingValidNameValidDescriptionFromAnotherUser() {
        return new IngredientEntity(
                "existingIdFromAnotherUser",
                "existingNameFromAnotherUser",
                "existingDescriptionFromAnotherUser",
                1,
                "anotherUsersId",
                1000L,
                2000L

        );
    }

    public static IngredientEntity getExistingValidWithConversionFactor() {
        return new IngredientEntity(
                getExistingValidNameValidDescriptionNoConversionFactor().getId(),
                getExistingValidNameValidDescriptionNoConversionFactor().getName(),
                getExistingValidNameValidDescriptionNoConversionFactor().getDescription(),
                0.5,
                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
                110L
        );
    }

    public static List<IngredientEntity> getAllIngredients() {
        List<IngredientEntity> ingredients = new ArrayList<>();
        ingredients.add(getNew());
        ingredients.add(getNewInvalidName());
        ingredients.add(getNewValidName());
        ingredients.add(getNewValidNameInvalidDescription());
        ingredients.add(getNewInvalidNameValidDescription());
        ingredients.add(getNewValidNameValidDescription());
        ingredients.add(getExistingValidNameValidDescriptionNoConversionFactor());
        ingredients.add(getExistingUpdatedWithInvalidName());
        ingredients.add(getExistingUpdatedWithValidName());
        ingredients.add(getExistingUpdatedWithInvalidDescription());
        ingredients.add(getExistingUpdatedWithValidDescription());
        ingredients.add(getExistingValidNameValidDescriptionFromAnotherUser());
        ingredients.add(getExistingValidWithConversionFactor());
        return ingredients;
    }

    public static String getValidNameNoDuplicate() {
        return "nonDuplicateName";
    }

    public static String getValidNonDuplicatedDescription() {
        return "nonDuplicatedDescription";
    }
}
