package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

public class RecipeEntityTestData {

    // Create new recipe, or used when cloning
    public static RecipeEntity getNewInvalid() {
        return new RecipeEntity(
                "newId",
                "newId",
                Constants.getUserId().getValue(),
                10L,
                10L,
                true
        );
    }

    // Built from new, or cloned / copied to current user
    public static RecipeEntity getNewValid() {
        return new RecipeEntity(
                getNewInvalid().getId(),
                getNewInvalid().getId(),
                getNewInvalid().getCreatedBy(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate(),
                true
        );
    }

    // Existing valid recipe loaded, current user is creator
    public static RecipeEntity getValidExisting() {
        return new RecipeEntity(
                "validExistingRecipeId",
                "validExistingRecipeId",
                Constants.getUserId().getValue(),
                20L,
                20L,
                false
        );
    }

    // Existing invalid recipe, current user is creator, one or more recipe data models are invalid
    public static RecipeEntity getInvalidExisting() {
        return new RecipeEntity(
                "invalidExistingId",
                "invalidExistingId",
                Constants.getUserId().getValue(),
                70L,
                70L,
                true
        );
    }

    // Existing valid recipe, another user is creator
    public static RecipeEntity getValidFromAnotherUser() {
        return new RecipeEntity(
                "idFromAnotherUser",
                "idFromAnotherUser",
                "anotherUser",
                30L,
                40L,
                false
        );
    }

    public static RecipeEntity getInvalidFromAnotherUser() {
        return new RecipeEntity(
                getValidFromAnotherUser().getId(),
                getValidFromAnotherUser().getId(),
                getValidFromAnotherUser().getCreatedBy(),
                50L,
                60L,
                true
        );
    }

    // Existing valid recipe, expected output when cloned from another user
    public static RecipeEntity getValidNewCloned() {
        return new RecipeEntity(
                getNewInvalid().getId(),
                getValidFromAnotherUser().getId(),
                Constants.getUserId().getValue(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate(),
                false
        );
    }

    // Existing invalid recipe, expected output when invalid recipe cloned, or made invalid after
    // editing clone
    public static RecipeEntity getInvalidNewCloned() {
        return new RecipeEntity(
                getNewInvalid().getId(),
                getInvalidFromAnotherUser().getId(),
                Constants.getUserId().getValue(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate(),
                true
        );
    }
}
























