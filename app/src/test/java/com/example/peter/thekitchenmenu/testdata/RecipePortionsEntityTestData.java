package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;

public class RecipePortionsEntityTestData {

    public static int getMinServings() {
        return 1;
    }
    public static int getMaxServings() {
        return 10;
    }
    public static int getMinSittings() {
        return 1;
    }
    public static int getMaxSittings() {
        return 10;
    }

    public static RecipePortionsEntity getNewEmpty() {
        return new RecipePortionsEntity(
                "portionId",
                RecipeEntityTestData.getInvalidNew().getId(),
                1,
                1,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidServingsInvalidSittings() {
        return new RecipePortionsEntity(
                getNewEmpty().getId(),
                RecipeEntityTestData.getInvalidNew().getId(),
                11,
                11,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewEmpty().getId(),
                RecipeEntityTestData.getInvalidNew().getId(),
                11,
                1,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsInvalidSittings() {
        return new RecipePortionsEntity(
                getNewEmpty().getId(),
                RecipeEntityTestData.getInvalidNew().getId(),
                1,
                11,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewEmpty().getId(),
                RecipeEntityTestData.getInvalidNew().getId(),
                10,
                10,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValid() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                RecipeEntityTestData.getValidExisting().getId(),
                3,
                3,
                RecipeEntityTestData.getValidExisting().getCreateDate(),
                RecipeEntityTestData.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidClone() {
        return new RecipePortionsEntity(
                getNewEmpty().getId(),
                RecipeEntityTestData.getValidNewCloned().getId(),
                5,
                5,
                RecipeEntityTestData.getValidNewCloned().getCreateDate(),
                RecipeEntityTestData.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getValidCloneFromAnotherUser() {
        return new RecipePortionsEntity(
                getNewEmpty().getId(),
                RecipeEntityTestData.getValidFromAnotherUser().getId(),
                5,
                5,
                RecipeEntityTestData.getValidFromAnotherUser().getCreateDate(),
                RecipeEntityTestData.getValidFromAnotherUser().getLastUpdate()
        );
    }
}
