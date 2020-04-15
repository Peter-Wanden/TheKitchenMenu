package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.TestDataRecipeMetadataEntity;

public class TestDataRecipeDurationEntity {

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    public static RecipeDurationEntity getValidNewEmpty() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                0,
//                0,
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getInvalidNewPrepTimeInvalid() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getMaxPrepTime() + 1,
//                getValidNewEmpty().getCookTime(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getInvalidNewCookTimeInvalid() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getValidNewEmpty().getPrepTime(),
//                getMaxCookTime() + 1,
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getValidNewPrepTimeValid() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getMaxPrepTime(),
//                getValidNewEmpty().getCookTime(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getValidNewCookTimeValid() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getValidNewEmpty().getPrepTime(),
//                getMaxCookTime(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getValidNewComplete() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getValidNewPrepTimeValid().getPrepTime(),
//                getValidNewPrepTimeValid().getCookTime(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getInvalidExistingComplete() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getInvalidExisting().getDataId(),
//                getMaxPrepTime() + 1,
//                getMaxCookTime() + 1,
//                TestDataRecipeMetadataEntity.getInvalidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getValidExistingComplete() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getValidExisting().getDataId(),
//                getMaxPrepTime(),
//                getMaxCookTime(),
//                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getValidCompleteFromAnotherUser() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getDataId(),
//                getMaxPrepTime(),
//                getMaxCookTime(),
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getInvalidCompleteFromAnotherUser() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getDataId(),
//                getMaxPrepTime() + 1,
//                getMaxCookTime() + 1,
//                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getValidNewCloned() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getValidNewCloned().getDataId(),
//                getValidCompleteFromAnotherUser().getPrepTime(),
//                getValidCompleteFromAnotherUser().getCookTime(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getInvalidNewCloned() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getDataId(),
//                getInvalidExistingComplete().getPrepTime(),
//                getInvalidExistingComplete().getCookTime(),
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getLastUpdate()
//        );
    }

    public static RecipeDurationEntity getValidNewClonedPrepTimeUpdated() {
        return null;
//        new RecipeDurationEntity(
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getDataId(),
//                getMaxPrepTime() / 2,
//                getValidCompleteFromAnotherUser().getCookTime(),
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getLastUpdate()
//        );
    }

}











































