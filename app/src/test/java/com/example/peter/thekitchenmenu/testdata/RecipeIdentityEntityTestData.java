package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;

public class RecipeIdentityEntityTestData {

    public static RecipeIdentityEntity getInvalidNewEmpty() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                "",
                "",
                RecipeDurationTestData.getValidNewEmpty().getPrepTime(),
                RecipeDurationTestData.getValidNewEmpty().getCookTime(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleUpdatedWithInvalidValue() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                "ti",
                getInvalidNewEmpty().getDescription(),
                RecipeDurationTestData.getValidNewEmpty().getPrepTime(),
                RecipeDurationTestData.getValidNewEmpty().getCookTime(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleInvalidDescriptionValid() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                getInvalidNewTitleUpdatedWithInvalidValue().getTitle(),
                getValidNewComplete().getDescription(),
                RecipeDurationTestData.getValidNewEmpty().getPrepTime(),
                RecipeDurationTestData.getValidNewEmpty().getCookTime(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewTitleUpdatedWithValidValue() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getValidNew().getId(),
                "newValidTitle",
                "",
                RecipeDurationTestData.getValidNewEmpty().getPrepTime(),
                RecipeDurationTestData.getValidNewEmpty().getCookTime(),
                RecipeEntityTestData.getValidNew().getCreateDate(),
                RecipeEntityTestData.getValidNew().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewComplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getValidNew().getId(),
                getValidNewTitleUpdatedWithValidValue().getTitle(),
                "validNewDescription",
                RecipeDurationTestData.getValidNewComplete().getPrepTime(),
                RecipeDurationTestData.getValidNewComplete().getCookTime(),
                RecipeEntityTestData.getValidNew().getCreateDate(),
                RecipeEntityTestData.getValidNew().getLastUpdate()
                );
    }

    public static RecipeIdentityEntity getInvalidExistingIncomplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidExisting().getId(),
                "ti",
                "",
                RecipeDurationTestData.getInvalidExisting().getPrepTime(),
                RecipeDurationTestData.getInvalidExisting().getCookTime(),
                RecipeEntityTestData.getInvalidExisting().getCreateDate(),
                RecipeEntityTestData.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidExistingComplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getValidExisting().getId(),
                "validExistingTitle",
                "validExistingDescription",
                RecipeDurationTestData.getValidExistingComplete().getPrepTime(),
                RecipeDurationTestData.getValidExistingComplete().getCookTime(),
                RecipeEntityTestData.getValidExisting().getCreateDate(),
                RecipeEntityTestData.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidCompleteFromAnotherUser() {
        return new RecipeIdentityEntity (
                RecipeEntityTestData.getValidFromAnotherUser().getId(),
                "validTitleFromAnotherUsersRecipe",
                "validDescriptionFromAnotherUsersRecipe",
                RecipeDurationTestData.getValidCompleteFromAnotherUser().getPrepTime(),
                RecipeDurationTestData.getValidCompleteFromAnotherUser().getCookTime(),
                RecipeEntityTestData.getValidFromAnotherUser().getCreateDate(),
                RecipeEntityTestData.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidCompleteFromAnotherUser() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidFromAnotherUser().getId(),
                "ti",
                "de",
                RecipeDurationTestData.getInvalidCompleteFromAnotherUser().getPrepTime(),
                RecipeDurationTestData.getInvalidCompleteFromAnotherUser().getPrepTime(),
                RecipeEntityTestData.getInvalidFromAnotherUser().getCreateDate(),
                RecipeEntityTestData.getInvalidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewCloned() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getValidNew().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                getValidCompleteFromAnotherUser().getDescription(),
                RecipeDurationTestData.getValidNewCloned().getPrepTime(),
                RecipeDurationTestData.getValidNewCloned().getCookTime(),
                RecipeEntityTestData.getValidNew().getCreateDate(),
                RecipeEntityTestData.getValidNew().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewCloned() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidNewCloned().getId(),
                getInvalidCompleteFromAnotherUser().getTitle(),
                getInvalidCompleteFromAnotherUser().getDescription(),
                RecipeDurationTestData.getInvalidNewCloned().getPrepTime(),
                RecipeDurationTestData.getInvalidNewCloned().getCookTime(),
                RecipeEntityTestData.getInvalidNewCloned().getCreateDate(),
                RecipeEntityTestData.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewClonedDescriptionUpdatedComplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                "validUpdatedDescription",
                RecipeDurationTestData.getValidNewCloned().getPrepTime(),
                RecipeDurationTestData.getValidNewCloned().getCookTime(),
                RecipeEntityTestData.getValidNewCloned().getLastUpdate(),
                RecipeEntityTestData.getValidNewCloned().getCreateDate()
        );
    }
}
