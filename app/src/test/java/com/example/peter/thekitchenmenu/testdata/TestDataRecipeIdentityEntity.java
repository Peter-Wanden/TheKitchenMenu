package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeIdentityEntity {

    public static RecipeIdentityEntity getInvalidNewEmpty() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                "",
                "",
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleUpdatedWithInvalidValue() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                "ti",
                getInvalidNewEmpty().getDescription(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleInvalidDescriptionValid() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getInvalidNewTitleUpdatedWithInvalidValue().getTitle(),
                getValidNewComplete().getDescription(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewTitleUpdatedWithValidValue() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewValid().getId(),
                "newValidTitle",
                "",
                TestDataRecipeEntity.getNewValid().getCreateDate(),
                TestDataRecipeEntity.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewComplete() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewValid().getId(),
                getValidNewTitleUpdatedWithValidValue().getTitle(),
                "validNewDescription",
                TestDataRecipeEntity.getNewValid().getCreateDate(),
                TestDataRecipeEntity.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingIncomplete() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidExisting().getId(),
                "ti",
                "",
                TestDataRecipeEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidExistingComplete() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getValidExisting().getId(),
                "validExistingTitle",
                "validExistingDescription",
                TestDataRecipeEntity.getValidExisting().getCreateDate(),
                TestDataRecipeEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidCompleteFromAnotherUser() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getValidFromAnotherUser().getId(),
                "validTitleFromAnotherUsersRecipe",
                "validDescriptionFromAnotherUsersRecipe",
                TestDataRecipeEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidCompleteFromAnotherUser() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidFromAnotherUser().getId(),
                "ti",
                "de",
                TestDataRecipeEntity.getInvalidFromAnotherUser().getCreateDate(),
                TestDataRecipeEntity.getInvalidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewCloned() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewValid().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                getValidCompleteFromAnotherUser().getDescription(),
                TestDataRecipeEntity.getNewValid().getCreateDate(),
                TestDataRecipeEntity.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewCloned() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidNewCloned().getId(),
                getInvalidCompleteFromAnotherUser().getTitle(),
                getInvalidCompleteFromAnotherUser().getDescription(),
                TestDataRecipeEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewClonedDescriptionUpdatedComplete() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                "validUpdatedDescription",
                TestDataRecipeEntity.getValidNewCloned().getLastUpdate(),
                TestDataRecipeEntity.getValidNewCloned().getCreateDate()
        );
    }

    public static RecipeIdentityEntity getRecipeIdentityEntityById(String recipeId) {
        List<RecipeIdentityEntity> entityList = new ArrayList<>();
        entityList.add(getInvalidNewEmpty());
        entityList.add(getInvalidNewTitleUpdatedWithInvalidValue());
        entityList.add(getInvalidNewTitleInvalidDescriptionValid());
        entityList.add(getValidNewTitleUpdatedWithValidValue());
        entityList.add(getValidNewComplete());
        entityList.add(getInvalidExistingIncomplete());
        entityList.add(getValidExistingComplete());
        entityList.add(getValidCompleteFromAnotherUser());
        entityList.add(getInvalidCompleteFromAnotherUser());
        entityList.add(getValidNewCloned());
        entityList.add(getInvalidNewCloned());
        entityList.add(getValidNewClonedDescriptionUpdatedComplete());

        for (RecipeIdentityEntity entity : entityList) {
            if (entity.getId().equals(recipeId)) {
                return entity;
            }
        }
        return null;
    }
}
