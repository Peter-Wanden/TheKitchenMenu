package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientTest;

public class TestDataIngredient {

    public static final String NEW_INGREDIENT_DOMAIN_ID = "domainId-ingredient-0";
    public static final String EXISTING_INGREDIENT_DOMAIN_ID = "domainId-ingredient-10";
    public static final String INGREDIENT_DOMAIN_ID_FROM_ANOTHER_USER = "domainId-ingredient-20";

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
                setDataId("dataId-ingredient-id1").
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
                setDescription(getValidNewComplete().getDescription()).
                setCreateDate(10L).
                setLastUpdate(50L).
                build();
    }

    public static IngredientPersistenceModel getValidNewNameValid() {
        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-ingredient-id5").
                setDomainId(NEW_INGREDIENT_DOMAIN_ID).
                setName(getValidNewComplete().getName()).
                setCreateDate(10L).
                setLastUpdate(60L).
                build();
    }

    public static IngredientPersistenceModel getValidNewComplete()  {
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
                setDataId("dataId-ingredient-").
                setDomainId(EXISTING_INGREDIENT_DOMAIN_ID).
                setName(validName).
                setDescription(validDescription).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }
}
