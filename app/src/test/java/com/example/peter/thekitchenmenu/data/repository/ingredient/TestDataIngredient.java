package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientTest;

import java.util.Arrays;
import java.util.List;

public class TestDataIngredient {

    public static final String NEW_INGREDIENT_DOMAIN_ID = "domainId-ingredient-0";
    public static final String EXISTING_INGREDIENT_DOMAIN_ID = "domainId-ingredient-10";
    private static final String INGREDIENT_DOMAIN_ID_FROM_ANOTHER_USER = "domainId-ingredient-20";

    // Test data new, text fields
    public static IngredientPersistenceModel getInvalidNewEmpty() {
        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id0").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static IngredientPersistenceModel getInvalidNewNameTooShort() {
        String nameTooShort = new StringMaker().
                makeStringOfExactLength(IngredientTest.NAME_MIN_LENGTH).
                includeStringAtStart("newNameTooShort").
                thenRemoveOneCharacter().
                build();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id1").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(nameTooShort).
                setCreateDate(10L).
                setLastUpdate(20L).
                build();
    }

    public static IngredientPersistenceModel getInvalidNewNameTooLong() {
        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id2").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(getInvalidNewNameTooLongDescriptionTooLong().getName()).
                setCreateDate(10L).
                setLastUpdate(30L).
                build();
    }

    public static IngredientPersistenceModel getInvalidNewNameTooLongDescriptionTooLong() {
        String nameTooLong = new StringMaker().
                makeStringOfExactLength(IngredientTest.NAME_MAX_LENGTH).
                includeStringAtStart("newNameTooLong").
                thenAddOneCharacter().
                build();

        String descriptionTooLong = new StringMaker().
                makeStringOfExactLength(IngredientTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("newDescriptionTooLong").
                thenAddOneCharacter().
                build();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id3").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(nameTooLong).
                setDescription(descriptionTooLong).
                setCreateDate(10L).
                setLastUpdate(40L).
                build();
    }

    public static IngredientPersistenceModel getInvalidNewNameTooShortDescriptionValid() {
        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id4").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(getInvalidNewNameTooShort().getName()).
                setDescription(getValidNewNameValidDescriptionValid().getDescription()).
                setCreateDate(10L).
                setLastUpdate(50L).
                build();
    }

    public static IngredientPersistenceModel getValidNewNameValid() {
        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id5").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(getValidNewNameValidDescriptionValid().getName()).
                setCreateDate(10L).
                setLastUpdate(60L).
                build();
    }

    public static IngredientPersistenceModel getValidNewNameValidDescriptionValid() {
        String validName = new StringMaker().
                makeStringOfExactLength(IngredientTest.NAME_MAX_LENGTH).
                includeStringAtStart("newValidName").
                build();

        String validDescription = new StringMaker().
                makeStringOfExactLength(IngredientTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("newValidDescription").
                build();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id6").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(validName).
                setDescription(validDescription).
                setCreateDate(10L).
                setLastUpdate(70L).
                build();
    }

    // Test data existing, text fields
    public static IngredientPersistenceModel getValidExistingNameValidDescriptionValid() {
        String validName = new StringMaker().
                makeStringOfExactLength(IngredientTest.NAME_MAX_LENGTH).
                includeStringAtStart("existingValidName").
                build();

        String validDescription = new StringMaker().
                makeStringOfExactLength(IngredientTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("existingValidDescription").
                build();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id10").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(validName).
                setDescription(validDescription).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static IngredientPersistenceModel getInvalidExistingNameTooShort() {
        String nameTooShort = new StringMaker().
                makeStringOfExactLength(IngredientTest.NAME_MIN_LENGTH).
                includeStringAtStart("existingNmeTooShort").
                thenRemoveOneCharacter().
                build();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id11").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(nameTooShort).
                setCreateDate(20L).
                setLastUpdate(30L).
                build();
    }

    public static IngredientPersistenceModel getInvalidExistingNameTooLong() {
        String nameTooLong = new StringMaker().
                makeStringOfExactLength(IngredientTest.NAME_MAX_LENGTH).
                includeStringAtStart("nameTooLong").
                thenAddOneCharacter().
                build();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id12").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(nameTooLong).
                setCreateDate(20L).
                setLastUpdate(40L).
                build();
    }

    public static IngredientPersistenceModel getInvalidExistingNameValidDescriptionTooLong() {
        String descriptionTooLong = new StringMaker().
                makeStringOfExactLength(IngredientTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("descriptionTooLong").
                thenAddOneCharacter().
                build();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id13").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(getValidExistingNameValidDescriptionValid().getName()).
                setDescription(descriptionTooLong).
                setCreateDate(20L).
                setLastUpdate(50L).
                build();
    }

    public static IngredientPersistenceModel getInvalidExistingNameTooShortDescriptionTooLong() {
        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id14").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(getInvalidExistingNameTooShort().getName()).
                setDescription(getInvalidExistingNameValidDescriptionTooLong().getDescription()).
                setCreateDate(20L).
                setLastUpdate(60L).
                build();
    }

    public static IngredientPersistenceModel getValidExistingNameValid() {
        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id15").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(getValidExistingNameValidDescriptionValid().getName()).
                setCreateDate(20L).
                setLastUpdate(70L).
                build();
    }

    // Test data from another user
    public static IngredientPersistenceModel getExistingValidNameValidDescriptionValidFromAnotherUser() {
        String validNameFromAnotherUser = new StringMaker().
                makeStringOfExactLength(IngredientTest.NAME_MAX_LENGTH).
                includeStringAtStart("validNameFromAnotherUser ").
                build();

        String validDescriptionFromAnotherUser = new StringMaker().
                makeStringOfExactLength(IngredientTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validDescriptionFromAnotherUser").
                build();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id16").
                setDomainId(INGREDIENT_DOMAIN_ID_FROM_ANOTHER_USER).
                setName(validNameFromAnotherUser).
                setDescription(validDescriptionFromAnotherUser).
                setCreateDate(40L).
                setLastUpdate(10L).
                build();
    }

    // Test data conversion factor
    public static IngredientPersistenceModel getNewValidMaxConversionFactor() {
        return new IngredientPersistenceModel.Builder().
                setDataId("dataId-ingredient-id7").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(getValidNewNameValidDescriptionValid().getName()).
                setDescription(getValidNewNameValidDescriptionValid().getDescription()).
                setConversionFactor(UnitOfMeasureConstants.MAX_CONVERSION_FACTOR).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(10L).
                setLastUpdate(80L).
                build();
    }

    public static IngredientPersistenceModel getNewValidConversionFactorUpdated() {
        return new IngredientPersistenceModel.Builder().
                setDataId("dataId-ingredient-id8").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(getValidNewNameValidDescriptionValid().getName()).
                setDescription(getValidNewNameValidDescriptionValid().getDescription()).
                setConversionFactor(0.5).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(10L).
                setLastUpdate(90L).
                build();
    }

    public static IngredientPersistenceModel getExistingValidDefaultConversionFactor() {
        return new IngredientPersistenceModel.Builder().
                setDataId("dataId-ingredient-id17").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(getValidNewNameValidDescriptionValid().getName()).
                setDescription(getValidNewNameValidDescriptionValid().getDescription()).
                setConversionFactor(UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(30L).
                setLastUpdate(80L).
                build();
    }

    public static IngredientPersistenceModel getExistingValidMinimumConversionFactor() {
        return new IngredientPersistenceModel.Builder().
                setDataId("dataId-ingredient-id18").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(getValidNewNameValidDescriptionValid().getName()).
                setDescription(getValidNewNameValidDescriptionValid().getDescription()).
                setConversionFactor(UnitOfMeasureConstants.MIN_CONVERSION_FACTOR).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(30L).
                setLastUpdate(80L).
                build();
    }

    public static List<IngredientPersistenceModel> getAllIngredients() {
        return Arrays.asList(
                getInvalidNewEmpty(),
                getInvalidNewNameTooShort(),
                getInvalidNewNameTooLong(),
                getInvalidNewNameTooLongDescriptionTooLong(),
                getInvalidNewNameTooShortDescriptionValid(),
                getValidNewNameValid(),
                getValidNewNameValidDescriptionValid(),
                getValidExistingNameValidDescriptionValid(),
                getInvalidExistingNameTooShort(),
                getInvalidExistingNameTooLong(),
                getInvalidExistingNameValidDescriptionTooLong(),
                getInvalidExistingNameTooShortDescriptionTooLong(),
                getValidExistingNameValid(),
                getExistingValidNameValidDescriptionValidFromAnotherUser(),
                getNewValidMaxConversionFactor(),
                getNewValidConversionFactorUpdated(),
                getExistingValidDefaultConversionFactor(),
                getExistingValidMinimumConversionFactor()
                );
    }

    // Duplicate checker test data
    public static String getValidNonDuplicatedName() {
        return "nameNotInTestDataModels";
    }

    public static String getValidNonDuplicatedDescription() {
        return "descriptionNotInTestDataModels";
    }
}
