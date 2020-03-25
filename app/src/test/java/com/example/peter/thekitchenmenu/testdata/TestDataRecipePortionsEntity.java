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
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getMinServings(),
                getMinSittings(),
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidFourPortions() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                2,
                2,
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidSixteenPortions() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                4,
                4,
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsInvalidTooHighSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getMaxServings() + 1,
                getMaxSittings() + 1,
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getMaxServings() + 1,
                getMinSittings(),
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsInvalidTooHighSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getMinServings(),
                getMaxSittings() + 1,
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getNewValidServingsValidSittings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getMaxServings(),
                getMaxSittings(),
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingInvalidTooHighSittingsInvalidTooHighServings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeMetadataEntity.getValidExisting().getId(),
                getMaxServings() + 1,
                getMaxSittings() + 1,
                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidNinePortions() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeMetadataEntity.getValidExisting().getId(),
                3,
                3,
                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedServings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeMetadataEntity.getValidExisting().getId(),
                getNewValidServingsValidSittings().getServings(),
                getExistingValidNinePortions().getSittings(),
                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedSittings() {
        return new RecipePortionsEntity(
                "validExistingPortionId",
                TestDataRecipeMetadataEntity.getValidExisting().getId(),
                getExistingValidNinePortions().getServings(),
                getNewValidServingsValidSittings().getSittings(),
                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingValidClone() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getId(),
                getExistingValidNinePortions().getServings(),
                getExistingValidNinePortions().getSittings(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getExistingClonedUpdatedSittingsServings() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getId(),
                getExistingValidUpdatedServings().getServings(),
                getExistingValidUpdatedSittings().getSittings(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getValidFromAnotherUser() {
        return new RecipePortionsEntity(
                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getId(),
                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getId(),
                5,
                5,
                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipePortionsEntity getValidCloneFromAnotherUser() {
        return new RecipePortionsEntity(
                getNewValidEmpty().getId(),
                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getId(),
                5,
                5,
                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate()
        );
    }
}
