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

    public static RecipeIdentityEntity getInvalidNewTitleUpdated() {
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

    public static RecipeIdentityEntity getValidNewTitleUpdated() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                "newTitle",
                getInvalidNewEmpty().getDescription(),
                RecipeDurationTestData.getValidNewEmpty().getPrepTime(),
                RecipeDurationTestData.getValidNewEmpty().getCookTime(),
                RecipeEntityTestData.getInvalidNew().getCreateDate(),
                RecipeEntityTestData.getInvalidNew().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewComplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidNew().getId(),
                "newTitle",
                "newDescription",
                RecipeDurationTestData.getValidNewComplete().getPrepTime(),
                RecipeDurationTestData.getValidNewComplete().getCookTime(),
                getInvalidNewEmpty().getCreateDate(),
                getInvalidNewEmpty().getLastUpdate()
                );
    }

    public static RecipeIdentityEntity getInvalidExistingIncomplete() {
        return new RecipeIdentityEntity(
                getValidExistingComplete().getId(),
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
                "title",
                "description",
                RecipeDurationTestData.getValidExistingComplete().getPrepTime(),
                RecipeDurationTestData.getValidExistingComplete().getCookTime(),
                RecipeEntityTestData.getValidExisting().getCreateDate(),
                RecipeEntityTestData.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidCompleteFromAnotherUser() {
        return new RecipeIdentityEntity (
                RecipeEntityTestData.getValidFromAnotherUser().getId(),
                "titleFromAnotherUsersRecipe",
                "descriptionFromAnotherUsersRecipe",
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
                getInvalidNewEmpty().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                getValidCompleteFromAnotherUser().getDescription(),
                RecipeDurationTestData.getValidNewCloned().getPrepTime(),
                RecipeDurationTestData.getValidNewCloned().getCookTime(),
                getInvalidNewEmpty().getCreateDate(),
                getInvalidNewEmpty().getLastUpdate()
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
                "updated description",
                RecipeDurationTestData.getValidNewCloned().getPrepTime(),
                RecipeDurationTestData.getValidNewCloned().getCookTime(),
                RecipeEntityTestData.getValidNewCloned().getLastUpdate(),
                RecipeEntityTestData.getValidNewCloned().getCreateDate()
        );
    }
}
