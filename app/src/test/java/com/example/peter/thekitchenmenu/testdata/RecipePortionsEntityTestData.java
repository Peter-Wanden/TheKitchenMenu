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

    public static RecipePortionsEntity getNewValidEmpty() {
        return new RecipePortionsEntity(
                "portionId",
                RecipeEntityTestData.getInvalidNew().getId(),
                getMinServings(),
                getMinSittings(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidServingsInvalidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                RecipeEntityTestData.getInvalidNew().getId(),
                getMaxServings() + 1,
                getMaxSittings() + 1,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                RecipeEntityTestData.getInvalidNew().getId(),
                getMaxServings() + 1,
                getMinSittings(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsInvalidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                RecipeEntityTestData.getInvalidNew().getId(),
                getMinServings(),
                getMaxSittings() + 1,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                RecipeEntityTestData.getInvalidNew().getId(),
                getMaxServings(),
                getMaxSittings(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingInvalid() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                RecipeEntityTestData.getValidExisting().getId(),
                getMaxServings() + 1,
                getMaxSittings() + 1,
                RecipeEntityTestData.getValidExisting().getCreateDate(),
                RecipeEntityTestData.getValidExisting().getLastUpdate()
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

    public static RecipePortionsEntity getExistingValidUpdatedServings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                RecipeEntityTestData.getValidExisting().getId(),
                getNewValidServingsValidSittings().getServings(),
                getExistingValid().getSittings(),
                RecipeEntityTestData.getValidExisting().getCreateDate(),
                RecipeEntityTestData.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedSittings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                RecipeEntityTestData.getValidExisting().getId(),
                getExistingValid().getServings(),
                getNewValidServingsValidSittings().getSittings(),
                RecipeEntityTestData.getValidExisting().getCreateDate(),
                RecipeEntityTestData.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidClone() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                RecipeEntityTestData.getValidNewCloned().getId(),
                getExistingValid().getServings(),
                getExistingValid().getSittings(),
                RecipeEntityTestData.getValidNewCloned().getCreateDate(),
                RecipeEntityTestData.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingClonedUpdatedSittingsServings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                RecipeEntityTestData.getValidNewCloned().getId(),
                getExistingValidUpdatedServings().getServings(),
                getExistingValidUpdatedSittings().getSittings(),
                RecipeEntityTestData.getValidNewCloned().getCreateDate(),
                RecipeEntityTestData.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getValidCloneFromAnotherUser() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                RecipeEntityTestData.getValidFromAnotherUser().getId(),
                5,
                5,
                RecipeEntityTestData.getValidFromAnotherUser().getCreateDate(),
                RecipeEntityTestData.getValidFromAnotherUser().getLastUpdate()
        );
    }
}
