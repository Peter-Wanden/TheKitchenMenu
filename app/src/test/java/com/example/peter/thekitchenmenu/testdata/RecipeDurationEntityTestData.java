package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;

public class RecipeDurationEntityTestData {

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    public static RecipeDurationEntity getValidNewEmpty() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                0,
                0,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewPrepTimeInvalid() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                6001,
                getValidNewEmpty().getCookTime(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewCookTimeInvalid() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                getValidNewEmpty().getPrepTime(),
                6001,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewPrepTimeValid() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                6000,
                getValidNewEmpty().getCookTime(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewCookTimeValid() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                getValidNewEmpty().getPrepTime(),
                6000,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewComplete() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                getValidNewPrepTimeValid().getPrepTime(),
                getValidNewPrepTimeValid().getCookTime(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidExistingComplete() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidExisting().getId(),
                6001,
                6001,
                RecipeEntityTestData.getInvalidExisting().getCreateDate(),
                RecipeEntityTestData.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidExistingComplete() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getValidExisting().getId(),
                6000,
                6000,
                RecipeEntityTestData.getValidExisting().getCreateDate(),
                RecipeEntityTestData.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidCompleteFromAnotherUser() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getValidFromAnotherUser().getId(),
                150,
                150,
                RecipeEntityTestData.getValidFromAnotherUser().getCreateDate(),
                RecipeEntityTestData.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidCompleteFromAnotherUser() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidFromAnotherUser().getId(),
                6001,
                6001,
                RecipeEntityTestData.getInvalidFromAnotherUser().getCreateDate(),
                RecipeEntityTestData.getInvalidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewCloned() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getPrepTime(),
                getValidCompleteFromAnotherUser().getCookTime(),
                RecipeEntityTestData.getValidNewCloned().getCreateDate(),
                RecipeEntityTestData.getValidNewCloned().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidNewCloned() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNewCloned().getId(),
                getInvalidExistingComplete().getPrepTime(),
                getInvalidExistingComplete().getCookTime(),
                RecipeEntityTestData.getInvalidNewCloned().getCreateDate(),
                RecipeEntityTestData.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewClonedPrepTimeUpdated() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNewCloned().getId(),
                3000,
                getValidCompleteFromAnotherUser().getCookTime(),
                RecipeEntityTestData.getInvalidNewCloned().getCreateDate(),
                RecipeEntityTestData.getInvalidNewCloned().getLastUpdate()
        );
    }

}











































