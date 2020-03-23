package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeDurationEntity;

public class TestDataRecipeDurationEntity {

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    public static RecipeDurationEntity getValidNewEmpty() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                0,
                0,
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewPrepTimeInvalid() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getMaxPrepTime() + 1,
                getValidNewEmpty().getCookTime(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewCookTimeInvalid() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getValidNewEmpty().getPrepTime(),
                getMaxCookTime() + 1,
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewPrepTimeValid() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getMaxPrepTime(),
                getValidNewEmpty().getCookTime(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewCookTimeValid() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getValidNewEmpty().getPrepTime(),
                getMaxCookTime(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getValidNewPrepTimeValid().getPrepTime(),
                getValidNewPrepTimeValid().getCookTime(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidExistingComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getInvalidExisting().getId(),
                getMaxPrepTime() + 1,
                getMaxCookTime() + 1,
                TestDataRecipeMetaDataEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidExistingComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getValidExisting().getId(),
                getMaxPrepTime(),
                getMaxCookTime(),
                TestDataRecipeMetaDataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidCompleteFromAnotherUser() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getId(),
                getMaxPrepTime(),
                getMaxCookTime(),
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidCompleteFromAnotherUser() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getInvalidFromAnotherUser().getId(),
                getMaxPrepTime() + 1,
                getMaxCookTime() + 1,
                TestDataRecipeMetaDataEntity.getInvalidFromAnotherUser().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewCloned() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getPrepTime(),
                getValidCompleteFromAnotherUser().getCookTime(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewCloned() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getId(),
                getInvalidExistingComplete().getPrepTime(),
                getInvalidExistingComplete().getCookTime(),
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewClonedPrepTimeUpdated() {
        return new RecipeDurationEntity(
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getId(),
                getMaxPrepTime() / 2,
                getValidCompleteFromAnotherUser().getCookTime(),
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

}











































