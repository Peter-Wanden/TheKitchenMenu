package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;

public class RecipeIdentityEntityTestData {

    public static RecipeIdentityEntity getInvalidNewEmpty() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getNewInvalid().getId(),
                "",
                "",
                RecipeEntityTestData.getNewInvalid().getCreateDate(),
                RecipeEntityTestData.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleUpdatedWithInvalidValue() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getNewInvalid().getId(),
                "ti",
                getInvalidNewEmpty().getDescription(),
                RecipeEntityTestData.getNewInvalid().getCreateDate(),
                RecipeEntityTestData.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleInvalidDescriptionValid() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getNewInvalid().getId(),
                getInvalidNewTitleUpdatedWithInvalidValue().getTitle(),
                getValidNewComplete().getDescription(),
                RecipeEntityTestData.getNewInvalid().getCreateDate(),
                RecipeEntityTestData.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewTitleUpdatedWithValidValue() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getNewValid().getId(),
                "newValidTitle",
                "",
                RecipeEntityTestData.getNewValid().getCreateDate(),
                RecipeEntityTestData.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewComplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getNewValid().getId(),
                getValidNewTitleUpdatedWithValidValue().getTitle(),
                "validNewDescription",
                RecipeEntityTestData.getNewValid().getCreateDate(),
                RecipeEntityTestData.getNewValid().getLastUpdate()
                );
    }

    public static RecipeIdentityEntity getInvalidExistingIncomplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidExisting().getId(),
                "ti",
                "",
                RecipeEntityTestData.getInvalidExisting().getCreateDate(),
                RecipeEntityTestData.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidExistingComplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getValidExisting().getId(),
                "validExistingTitle",
                "validExistingDescription",
                RecipeEntityTestData.getValidExisting().getCreateDate(),
                RecipeEntityTestData.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidCompleteFromAnotherUser() {
        return new RecipeIdentityEntity (
                RecipeEntityTestData.getValidFromAnotherUser().getId(),
                "validTitleFromAnotherUsersRecipe",
                "validDescriptionFromAnotherUsersRecipe",
                RecipeEntityTestData.getValidFromAnotherUser().getCreateDate(),
                RecipeEntityTestData.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidCompleteFromAnotherUser() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidFromAnotherUser().getId(),
                "ti",
                "de",
                RecipeEntityTestData.getInvalidFromAnotherUser().getCreateDate(),
                RecipeEntityTestData.getInvalidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewCloned() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getNewValid().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                getValidCompleteFromAnotherUser().getDescription(),
                RecipeEntityTestData.getNewValid().getCreateDate(),
                RecipeEntityTestData.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewCloned() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getInvalidNewCloned().getId(),
                getInvalidCompleteFromAnotherUser().getTitle(),
                getInvalidCompleteFromAnotherUser().getDescription(),
                RecipeEntityTestData.getInvalidNewCloned().getCreateDate(),
                RecipeEntityTestData.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewClonedDescriptionUpdatedComplete() {
        return new RecipeIdentityEntity(
                RecipeEntityTestData.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                "validUpdatedDescription",
                RecipeEntityTestData.getValidNewCloned().getLastUpdate(),
                RecipeEntityTestData.getValidNewCloned().getCreateDate()
        );
    }
}
