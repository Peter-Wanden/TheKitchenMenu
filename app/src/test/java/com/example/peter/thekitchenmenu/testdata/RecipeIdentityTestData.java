package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;

import static com.example.peter.thekitchenmenu.testdata.RecipeTestData.*;

public class RecipeIdentityTestData {

    public static RecipeIdentityEntity getValidRecipeIdentityEntity() {
        return new RecipeIdentityEntity(
                "id",
                getValidExistingRecipeEntity().getId(),
                "title",
                "description",
                90,
                90,
                10L,
                10L
        );
    }

    public static RecipeIdentityEntity getValidRecipeIdentityEntityFromAnotherUser() {
        return new RecipeIdentityEntity (
                "id1",
                RecipeTestData.getValidRecipeEntityFromAnotherUser().getId(),
                "titleFromAnotherRecipe",
                "descriptionFromAnotherRecipe",
                150,
                150,
                100L,
                100L
        );
    }

    public static RecipeIdentityEntity getValidClonedRecipeIdentityEntity() {
        return new RecipeIdentityEntity(
                "clonedIdentityEntityNewId",
                getValidRecipeIdentityEntityFromAnotherUser().getRecipeId(),
                getValidRecipeIdentityEntityFromAnotherUser().getTitle(),
                getValidRecipeIdentityEntityFromAnotherUser().getDescription(),
                getValidRecipeIdentityEntityFromAnotherUser().getPrepTime(),
                getValidRecipeIdentityEntityFromAnotherUser().getCookTime(),
                200L,
                200L
        );
    }

    public static RecipeIdentityEntity getValidIdentityEntityWithOnlyTitleUpdated() {
        return new RecipeIdentityEntity(
                getValidRecipeIdentityEntity().getId(),
                RecipeTestData.getValidExistingRecipeEntity().getId(),
                getValidRecipeIdentityEntity().getTitle(),
                "",
                0,
                0,
                getValidRecipeIdentityEntity().getCreateDate(),
                getValidRecipeIdentityEntity().getCreateDate()
        );
    }

    public static RecipeIdentityEntity getClonedIdentityEntityWithDescriptionUpdated() {
        return new RecipeIdentityEntity(
                getValidClonedRecipeIdentityEntity().getId(),
                getValidClonedRecipeIdentityEntity().getRecipeId(),
                getValidRecipeIdentityEntityFromAnotherUser().getTitle(),
                "updated description",
                getValidRecipeIdentityEntityFromAnotherUser().getPrepTime(),
                getValidRecipeIdentityEntityFromAnotherUser().getCookTime(),
                getValidClonedRecipeIdentityEntity().getLastUpdate(),
                getValidClonedRecipeIdentityEntity().getCreateDate()
        );
    }

    public static RecipeIdentityEntity getEmptyClonedIdentityEntityWithNoUpdates() {
        return new RecipeIdentityEntity(
                getValidClonedRecipeIdentityEntity().getId(),
                getValidClonedRecipeIdentityEntity().getRecipeId(),
                "",
                "",
                0,
                0,
                getValidClonedRecipeIdentityEntity().getLastUpdate(),
                getValidClonedRecipeIdentityEntity().getCreateDate()
        );
    }
}
