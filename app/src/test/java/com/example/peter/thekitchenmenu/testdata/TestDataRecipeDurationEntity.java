package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationTest.MAX_COOK_TIME;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationTest.MAX_PREP_TIME;

public class TestDataRecipeDurationEntity {

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    public static RecipeDurationEntity getValidNewEmpty() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                0,
                0,
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewPrepTimeInvalid() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getMaxPrepTime() + 1,
                getValidNewEmpty().getCookTime(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewCookTimeInvalid() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getValidNewEmpty().getPrepTime(),
                getMaxCookTime() + 1,
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewPrepTimeValid() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getMaxPrepTime(),
                getValidNewEmpty().getCookTime(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewCookTimeValid() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getValidNewEmpty().getPrepTime(),
                getMaxCookTime(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getValidNewPrepTimeValid().getPrepTime(),
                getValidNewPrepTimeValid().getCookTime(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidExistingComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getInvalidExisting().getId(),
                getMaxPrepTime() + 1,
                getMaxCookTime() + 1,
                TestDataRecipeEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidExistingComplete() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getValidExisting().getId(),
                getMaxPrepTime(),
                getMaxCookTime(),
                TestDataRecipeEntity.getValidExisting().getCreateDate(),
                TestDataRecipeEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidCompleteFromAnotherUser() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getValidFromAnotherUser().getId(),
                getMaxPrepTime(),
                getMaxCookTime(),
                TestDataRecipeEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidCompleteFromAnotherUser() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getInvalidFromAnotherUser().getId(),
                getMaxPrepTime() + 1,
                getMaxCookTime() + 1,
                TestDataRecipeEntity.getInvalidFromAnotherUser().getCreateDate(),
                TestDataRecipeEntity.getInvalidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewCloned() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getPrepTime(),
                getValidCompleteFromAnotherUser().getCookTime(),
                TestDataRecipeEntity.getValidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewCloned() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getInvalidNewCloned().getId(),
                getInvalidExistingComplete().getPrepTime(),
                getInvalidExistingComplete().getCookTime(),
                TestDataRecipeEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewClonedPrepTimeUpdated() {
        return new RecipeDurationEntity(
                TestDataRecipeEntity.getInvalidNewCloned().getId(),
                getMaxPrepTime() / 2,
                getValidCompleteFromAnotherUser().getCookTime(),
                TestDataRecipeEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

}











































