package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;

import java.util.ArrayList;
import java.util.List;

public class IngredientEntityTestData {

    public static IngredientEntity getNew() {
        return new IngredientEntity(
                "newId",
                "",
                "",
                Constants.getUserId().getValue(),
                10L,
                10L);
    }

    public static IngredientEntity getNewInvalidName() {
        return new IngredientEntity(
                getNew().getId(),
                "na",
                getNew().getDescription(),
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
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                30L
        );
    }

    public static IngredientEntity getNewValidNameInvalidDescription() {
        return new IngredientEntity(
                getNew().getId(),
                getNewValidName().getName(),
                "de",
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
                getNew().getCreatedBy(),
                getNew().getCreateDate(),
                60L
        );
    }

    public static IngredientEntity getExistingValidNameValidDescription() {
        return new IngredientEntity(
                "existingId",
                "existingName",
                "existingDescription",
                Constants.getUserId().getValue(),
                70L,
                80L

        );
    }

    public static IngredientEntity getExistingUpdatedWithInvalidName() {
        return new IngredientEntity(
                getExistingValidNameValidDescription().getId(),
                "na",
                getExistingValidNameValidDescription().getDescription(),
                getExistingValidNameValidDescription().getCreatedBy(),
                getExistingValidNameValidDescription().getCreateDate(),
                getExistingValidNameValidDescription().getLastUpdate()
        );
    }

    public static IngredientEntity getExistingUpdatedWithValidName() {
        return new IngredientEntity(
                getExistingValidNameValidDescription().getId(),
                "existingNameUpdated",
                getExistingValidNameValidDescription().getDescription(),
                getExistingValidNameValidDescription().getCreatedBy(),
                getExistingValidNameValidDescription().getCreateDate(),
                90L
        );
    }

    public static IngredientEntity getExistingUpdatedWithInvalidDescription() {
        return new IngredientEntity(
                getExistingValidNameValidDescription().getId(),
                getExistingValidNameValidDescription().getName(),
                "de",
                getExistingValidNameValidDescription().getCreatedBy(),
                getExistingValidNameValidDescription().getCreateDate(),
                90L
        );
    }

    public static IngredientEntity getExistingUpdatedWithValidDescription() {
        return new IngredientEntity(
                getExistingValidNameValidDescription().getId(),
                getExistingValidNameValidDescription().getName(),
                "existingDescriptionUpdated",
                getExistingValidNameValidDescription().getCreatedBy(),
                getExistingValidNameValidDescription().getCreateDate(),
                100L
        );
    }

    public static IngredientEntity getExistingValidNameValidDescriptionFromAnotherUser() {
        return new IngredientEntity(
                "existingIdFromAnotherUser",
                "existingNameFromAnotherUser",
                "existingDescriptionFromAnotherUser",
                "anotherUsersId",
                1000L,
                2000L

        );
    }

    public static List<IngredientEntity> getAllIngredients() {
        List<IngredientEntity> entityList = new ArrayList<>();
        entityList.add(getNew());
        entityList.add(getNewInvalidName());
        entityList.add(getNewValidName());
        entityList.add(getNewValidNameInvalidDescription());
        entityList.add(getNewInvalidNameValidDescription());
        entityList.add(getNewValidNameValidDescription());
        entityList.add(getExistingValidNameValidDescription());
        entityList.add(getExistingUpdatedWithInvalidName());
        entityList.add(getExistingUpdatedWithValidName());
        entityList.add(getExistingUpdatedWithInvalidDescription());
        entityList.add(getExistingUpdatedWithValidDescription());
        entityList.add(getExistingValidNameValidDescriptionFromAnotherUser());
        return entityList;
    }

    public static String getValidNameNoDuplicate() {
        return "nonDuplicateName";
    }

    public static String getValidNonDuplicatedDescription() {
        return "nonDuplicatedDescription";
    }
}
