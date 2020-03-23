package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;

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
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getMinServings(),
                getMinSittings(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidFourPortions() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                2,
                2,
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidSixteenPortions() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                4,
                4,
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsInvalidTooHighSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getMaxServings() + 1,
                getMaxSittings() + 1,
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getMaxServings() + 1,
                getMinSittings(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsInvalidTooHighSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getMinServings(),
                getMaxSittings() + 1,
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getMaxServings(),
                getMaxSittings(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingInvalidTooHighSittingsInvalidTooHighServings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeMetaDataEntity.getValidExisting().getId(),
                getMaxServings() + 1,
                getMaxSittings() + 1,
                TestDataRecipeMetaDataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidNinePortions() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeMetaDataEntity.getValidExisting().getId(),
                3,
                3,
                TestDataRecipeMetaDataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedServings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeMetaDataEntity.getValidExisting().getId(),
                getNewValidServingsValidSittings().getServings(),
                getExistingValidNinePortions().getSittings(),
                TestDataRecipeMetaDataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedSittings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeMetaDataEntity.getValidExisting().getId(),
                getExistingValidNinePortions().getServings(),
                getNewValidServingsValidSittings().getSittings(),
                TestDataRecipeMetaDataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidClone() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getId(),
                getExistingValidNinePortions().getServings(),
                getExistingValidNinePortions().getSittings(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingClonedUpdatedSittingsServings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getId(),
                getExistingValidUpdatedServings().getServings(),
                getExistingValidUpdatedSittings().getSittings(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getValidFromAnotherUser() {
        return new RecipePortionsEntity(
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getId(),
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getId(),
                5,
                5,
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getValidCloneFromAnotherUser() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getId(),
                5,
                5,
                TestDataRecipeMetaDataEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getLastUpdate()
        );
    }
}
