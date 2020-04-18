package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;

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
        return null;
//        new RecipePortionsEntity(
//                "portionId",
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getMinServings(),
//                getMinSittings(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getNewValidFourPortions() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                2,
//                2,
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getNewValidSixteenPortions() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                4,
//                4,
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsInvalidTooHighSittings() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getMaxServings() + 1,
//                getMaxSittings() + 1,
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsValidSittings() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getMaxServings() + 1,
//                getMinSittings(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getNewValidServingsInvalidTooHighSittings() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getMinServings(),
//                getMaxSittings() + 1,
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getNewValidServingsValidSittings() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getMaxServings(),
//                getMaxSittings(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getExistingInvalidTooHighSittingsInvalidTooHighServings() {
        return null;
//        new RecipePortionsEntity(
//                "validExistingPortionId",
//                TestDataRecipeMetadataEntity.getValidExisting().getDataId(),
//                getMaxServings() + 1,
//                getMaxSittings() + 1,
//                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getExistingValidNinePortions() {
        return null;
//        new RecipePortionsEntity(
//                "validExistingPortionId",
//                TestDataRecipeMetadataEntity.getValidExisting().getDataId(),
//                3,
//                3,
//                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedServings() {
        return null;
//        new RecipePortionsEntity(
//                "validExistingPortionId",
//                TestDataRecipeMetadataEntity.getValidExisting().getDataId(),
//                getNewValidServingsValidSittings().getServings(),
//                getExistingValidNinePortions().getSittings(),
//                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedSittings() {
        return null;
//        new RecipePortionsEntity(
//                "validExistingPortionId",
//                TestDataRecipeMetadataEntity.getValidExisting().getDataId(),
//                getExistingValidNinePortions().getServings(),
//                getNewValidServingsValidSittings().getSittings(),
//                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getExistingValidClone() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getDataId(),
//                getExistingValidNinePortions().getServings(),
//                getExistingValidNinePortions().getSittings(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getExistingClonedUpdatedSittingsServings() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getDataId(),
//                getExistingValidUpdatedServings().getServings(),
//                getExistingValidUpdatedSittings().getSittings(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getValidFromAnotherUser() {
        return null;
//        new RecipePortionsEntity(
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getDataId(),
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getDataId(),
//                5,
//                5,
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getLastUpdate()
//        );
    }

    public static RecipePortionsEntity getValidCloneFromAnotherUser() {
        return null;
//        new RecipePortionsEntity(
//                getNewValidEmpty().getDataId(),
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getDataId(),
//                5,
//                5,
//                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate()
//        );
    }
}
