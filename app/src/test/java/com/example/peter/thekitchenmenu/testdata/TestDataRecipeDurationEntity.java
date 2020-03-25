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
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                0,
                0,
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewPrepTimeInvalid() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getMaxPrepTime() + 1,
                getValidNewEmpty().getCookTime(),
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewCookTimeInvalid() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getValidNewEmpty().getPrepTime(),
                getMaxCookTime() + 1,
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewPrepTimeValid() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getMaxPrepTime(),
                getValidNewEmpty().getCookTime(),
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewCookTimeValid() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getValidNewEmpty().getPrepTime(),
                getMaxCookTime(),
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getNewInvalid().getId(),
                getValidNewPrepTimeValid().getPrepTime(),
                getValidNewPrepTimeValid().getCookTime(),
                TestDataRecipeMetadataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetadataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidExistingComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getInvalidExisting().getId(),
                getMaxPrepTime() + 1,
                getMaxCookTime() + 1,
                TestDataRecipeMetadataEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeMetadataEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidExistingComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getValidExisting().getId(),
                getMaxPrepTime(),
                getMaxCookTime(),
                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidCompleteFromAnotherUser() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getId(),
                getMaxPrepTime(),
                getMaxCookTime(),
                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidCompleteFromAnotherUser() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getId(),
                getMaxPrepTime() + 1,
                getMaxCookTime() + 1,
                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getCreateDate(),
                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewCloned() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getPrepTime(),
                getValidCompleteFromAnotherUser().getCookTime(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewCloned() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getInvalidNewCloned().getId(),
                getInvalidExistingComplete().getPrepTime(),
                getInvalidExistingComplete().getCookTime(),
                TestDataRecipeMetadataEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeMetadataEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewClonedPrepTimeUpdated() {
        return new RecipeDurationEntity(
                TestDataRecipeMetadataEntity.getInvalidNewCloned().getId(),
                getMaxPrepTime() / 2,
                getValidCompleteFromAnotherUser().getCookTime(),
                TestDataRecipeMetadataEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeMetadataEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

}











































