package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource;

import com.example.peter.thekitchenmenu.data.repository.ingredient.TestDataIngredient;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataIngredientEntity {

    public static IngredientEntity getInvalidNewEmpty() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidNewEmpty()
        );
    }

    public static IngredientEntity getInvalidNewNameTooShort() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidNewNameTooShort()
        );
    }

    public static IngredientEntity getInvalidNewNameTooLong() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidNewNameTooLong()
        );
    }

    public static IngredientEntity getInvalidNewNameTooLongDescriptionTooLong() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidNewNameTooLongDescriptionTooLong()
        );
    }

    public static IngredientEntity getInvalidNewNameTooShortDescriptionValid() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidNewNameTooShortDescriptionValid()
        );
    }

    public static IngredientEntity getValidNewNameValid() {
        return convertModelToEntity(TestDataIngredient.
                getValidNewNameValid()
        );
    }

    public static IngredientEntity getValidNewNameValidDescriptionValid() {
        return convertModelToEntity(TestDataIngredient.
                getValidNewNameValidDescriptionValid()
        );
    }

    public static IngredientEntity getInvalidExistingNameTooShort() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidExistingNameTooShort()
        );
    }

    public static IngredientEntity getInvalidExistingNameTooLong() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidExistingNameTooLong()
        );
    }

    public static IngredientEntity getInvalidExistingNameValidDescriptionTooLong() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidExistingNameValidDescriptionTooLong()
        );
    }

    public static IngredientEntity getInvalidExistingNameTooShortDescriptionTooLong() {
        return convertModelToEntity(TestDataIngredient.
                getInvalidExistingNameTooShortDescriptionTooLong()
        );
    }

    public static IngredientEntity getValidExistingNameValidDescriptionValid() {
        return convertModelToEntity(TestDataIngredient.
                getValidExistingNameValidDescriptionValid()
        );
    }

    public static IngredientEntity getValidExistingNameValid() {
        return convertModelToEntity(TestDataIngredient.
                getValidExistingNameValid()
        );
    }

    // Test data conversion factor
    public static IngredientEntity getNewValidMaxConversionFactor() {
        return convertModelToEntity(TestDataIngredient.
                getNewValidMaxConversionFactor()
        );
    }

    public static IngredientEntity getNewValidConversionFactorUpdated() {
        return convertModelToEntity(TestDataIngredient.
                getNewValidConversionFactorUpdated()
        );
    }

    public static IngredientEntity getExistingValidDefaultConversionFactor() {
        return convertModelToEntity(TestDataIngredient.
                getExistingValidDefaultConversionFactor()
        );
    }

    public static IngredientEntity getExistingValidNameValidDescriptionValidFromAnotherUser() {
        return convertModelToEntity(TestDataIngredient.
                getExistingValidNameValidDescriptionValidFromAnotherUser()
        );
    }

    public static IngredientEntity getExistingValidMinimumConversionFactor() {
        return convertModelToEntity(TestDataIngredient.
                getExistingValidMinimumConversionFactor()
        );
    }

    public static List<IngredientEntity> getAll() {
        List<IngredientEntity> ingredients = new ArrayList<>();

        TestDataIngredient.getAll().forEach((ingredient) -> {
                    ingredients.add(convertModelToEntity(ingredient));
                }
        );
        return ingredients;
    }

    public static List<IngredientEntity> getAllByDomainId(String domainId) {
        List<IngredientEntity> ingredients = new ArrayList<>();

        for (IngredientPersistenceModel m : TestDataIngredient.getAllByDomainId(domainId)) {
            ingredients.add(convertModelToEntity(m));
        }
        return ingredients;
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
