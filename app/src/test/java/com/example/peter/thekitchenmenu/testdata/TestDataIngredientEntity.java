package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.repository.ingredient.TestDataIngredient;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataIngredientEntity {

    public static IngredientEntity getInvalidNewEmpty() {
        return convertModelToEntity(TestDataIngredient.getInvalidNewEmpty()
        );
    }

    public static IngredientEntity getInvalidNewNameTooShort() {
        return convertModelToEntity(TestDataIngredient.getInvalidNewNameTooShort()
        );
    }

    public static IngredientEntity getNewValidName() {
        return null;
//        new IngredientEntity(
//                getNew().getDataId(),
//                "name",
//                getNew().getDescription(),
//                1,
//                getNew().getCreatedBy(),
//                getNew().getCreateDate(),
//                30L
//        );
    }

    public static IngredientEntity getNewValidNameMaxConversionFactor() {
        return null;
//        new IngredientEntity(
//                getNew().getDataId(),
//                getNewValidName().getName(),
//                getNew().getDescription(),
//                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
//                getNew().getCreatedBy(),
//                getNew().getCreateDate(),
//                getNew().getLastUpdate()
//        );
    }

    public static IngredientEntity getNewValidNameInvalidDescription() {
        return null;
//        new IngredientEntity(
//                "5",
//                getNewValidName().getName(),
//                "de",
//                1,
//                getNew().getCreatedBy(),
//                getNew().getCreateDate(),
//                40L
//        );
    }

    public static IngredientEntity getNewInvalidNameValidDescription() {
        return null;
//        new IngredientEntity(
//                getNew().getDataId(),
//                getNewInvalidName().getName(),
//                "description",
//                1,
//                getNew().getCreatedBy(),
//                getNew().getCreateDate(),
//                50L
//        );
    }

    public static IngredientEntity getNewValidNameValidDescription() {
        return null;
//        new IngredientEntity(
//                getNew().getDataId(),
//                getNewValidName().getName(),
//                getNewInvalidNameValidDescription().getDescription(),
//                1,
//                getNew().getCreatedBy(),
//                getNew().getCreateDate(),
//                60L
//        );
    }

    public static IngredientEntity getNewValidNameValidDescriptionConversionFactorUpdated() {
        return null;
//        new IngredientEntity(
//                getNewValidName().getDataId(),
//                getNewValidName().getName(),
//                getNewInvalidNameValidDescription().getDescription(),
//                0.5,
//                getNew().getCreatedBy(),
//                getNew().getCreateDate(),
//                70L
//        );
    }

    public static IngredientEntity getExistingValidNameValidDescriptionNoConversionFactor() {
        return null;
//        new IngredientEntity(
//                "9",
//                "existingName",
//                "existingDescription",
//                1,
//                Constants.getUserId(),
//                70L,
//                80L
//
//        );
    }

    public static IngredientEntity getExistingUpdatedWithInvalidName() {
        return null;
//        new IngredientEntity(
//                "10",
//                "na",
//                getExistingValidNameValidDescriptionNoConversionFactor().getDescription(),
//                1,
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
//                getExistingValidNameValidDescriptionNoConversionFactor().getLastUpdate()
//        );
    }

    public static IngredientEntity getExistingUpdatedWithValidName() {
        return null;
//        new IngredientEntity(
//                getExistingValidNameValidDescriptionNoConversionFactor().getDataId(),
//                "existingNameUpdated",
//                getExistingValidNameValidDescriptionNoConversionFactor().getDescription(),
//                1,
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
//                90L
//        );
    }

    public static IngredientEntity getExistingUpdatedWithInvalidDescription() {
        return null;
//        new IngredientEntity(
//                "12",
//                getExistingValidNameValidDescriptionNoConversionFactor().getName(),
//                "de",
//                1,
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
//                90L
//        );
    }

    public static IngredientEntity getExistingUpdatedWithValidDescription() {
        return null;
//        new IngredientEntity(
//                getExistingValidNameValidDescriptionNoConversionFactor().getDataId(),
//                getExistingValidNameValidDescriptionNoConversionFactor().getName(),
//                "existingDescriptionUpdated",
//                1,
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
//                100L
//        );
    }

    public static IngredientEntity getExistingValidNameValidDescriptionFromAnotherUser() {
        return null;
//        new IngredientEntity(
//                "14",
//                "existingNameFromAnotherUser",
//                "existingDescriptionFromAnotherUser",
//                1,
//                "anotherUsersId",
//                1000L,
//                2000L
//
//        );
    }

    public static IngredientEntity getExistingValidWithConversionFactor() {
        return null;
//        new IngredientEntity(
//                "15",
//                getExistingValidNameValidDescriptionNoConversionFactor().getName(),
//                getExistingValidNameValidDescriptionNoConversionFactor().getDescription(),
//                0.5,
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreatedBy(),
//                getExistingValidNameValidDescriptionNoConversionFactor().getCreateDate(),
//                110L
//        );
    }

    public static List<IngredientEntity> getAllIngredients() {
        List<IngredientEntity> ingredients = new ArrayList<>();
        ingredients.add(getInvalidNewEmpty());
        ingredients.add(getInvalidNewNameTooShort());
        ingredients.add(getNewValidName());
        ingredients.add(getNewValidNameMaxConversionFactor());
        ingredients.add(getNewValidNameInvalidDescription());
        ingredients.add(getNewInvalidNameValidDescription());
        ingredients.add(getNewValidNameValidDescription());
        ingredients.add(getNewValidNameValidDescriptionConversionFactorUpdated());
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

    private static IngredientEntity convertModelToEntity(IngredientPersistenceModel m) {
        return new IngredientEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getName(),
                m.getDescription(),
                m.getConversionFactor(),
                m.getCreatedBy(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }
}
