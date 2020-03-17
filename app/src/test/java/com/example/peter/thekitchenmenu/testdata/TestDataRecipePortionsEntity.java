package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;

public class TestDataRecipePortionsEntity {

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
                TestDataRecipeEntity.getNewInvalid().getId(),
                getMinServings(),
                getMinSittings(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidFourPortions() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getNewInvalid().getId(),
                2,
                2,
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidSixteenPortions() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getNewInvalid().getId(),
                4,
                4,
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsInvalidTooHighSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getNewInvalid().getId(),
                getMaxServings() + 1,
                getMaxSittings() + 1,
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getNewInvalid().getId(),
                getMaxServings() + 1,
                getMinSittings(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsInvalidTooHighSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getNewInvalid().getId(),
                getMinServings(),
                getMaxSittings() + 1,
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getNewInvalid().getId(),
                getMaxServings(),
                getMaxSittings(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingInvalidTooHighSittingsInvalidTooHighServings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeEntity.getValidExisting().getId(),
                getMaxServings() + 1,
                getMaxSittings() + 1,
                TestDataRecipeEntity.getValidExisting().getCreateDate(),
                TestDataRecipeEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidNinePortions() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeEntity.getValidExisting().getId(),
                3,
                3,
                TestDataRecipeEntity.getValidExisting().getCreateDate(),
                TestDataRecipeEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedServings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeEntity.getValidExisting().getId(),
                getNewValidServingsValidSittings().getServings(),
                getExistingValidNinePortions().getSittings(),
                TestDataRecipeEntity.getValidExisting().getCreateDate(),
                TestDataRecipeEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedSittings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeEntity.getValidExisting().getId(),
                getExistingValidNinePortions().getServings(),
                getNewValidServingsValidSittings().getSittings(),
                TestDataRecipeEntity.getValidExisting().getCreateDate(),
                TestDataRecipeEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidClone() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getValidNewCloned().getId(),
                getExistingValidNinePortions().getServings(),
                getExistingValidNinePortions().getSittings(),
                TestDataRecipeEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingClonedUpdatedSittingsServings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getValidNewCloned().getId(),
                getExistingValidUpdatedServings().getServings(),
                getExistingValidUpdatedSittings().getSittings(),
                TestDataRecipeEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getValidFromAnotherUser() {
        return new RecipePortionsEntity(
                TestDataRecipeEntity.getValidFromAnotherUser().getId(),
                TestDataRecipeEntity.getValidFromAnotherUser().getId(),
                5,
                5,
                TestDataRecipeEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getValidCloneFromAnotherUser() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeEntity.getValidFromAnotherUser().getId(),
                5,
                5,
                TestDataRecipeEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getValidNewCloned().getLastUpdate()
        );
    }
}
