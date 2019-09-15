package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;

public class RecipeDurationTestData {

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

    public static RecipeDurationEntity getNewPrepTimeUpdatedInvalid() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                6001,
                0,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getNewCookTimeUpdatedInvalid() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                0,
                6001,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getValidNewComplete() {
        return new RecipeDurationEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                6000,
                6000,
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeDurationEntity getInvalidExisting() {
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

    // getInvalidFromAnotherUser
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
                getInvalidExisting().getPrepTime(),
                getInvalidExisting().getCookTime(),
                RecipeEntityTestData.getInvalidNewCloned().getCreateDate(),
                RecipeEntityTestData.getInvalidNewCloned().getLastUpdate()
        );
    }


}











































