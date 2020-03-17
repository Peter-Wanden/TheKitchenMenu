package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeEntity {

    // Create new recipe, or used when cloning
    public static RecipeEntity getNewInvalid() {
        return new RecipeEntity(
                "newId",
                "newId",
                Constants.getUserId(),
                10L,
                10L
        );
    }

    // Built from new, or cloned / copied to current user
    public static RecipeEntity getNewValid() {
        return new RecipeEntity(
                getNewInvalid().getId(),
                getNewInvalid().getId(),
                getNewInvalid().getCreatedBy(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    // Existing valid recipe loaded, current user is creator
    public static RecipeEntity getValidExisting() {
        return new RecipeEntity(
                "validExistingRecipeId",
                "validExistingRecipeId",
                Constants.getUserId(),
                20L,
                20L
        );
    }

    // Existing invalid recipe, current user is creator, one or more recipe data models are invalid
    public static RecipeEntity getInvalidExisting() {
        return new RecipeEntity(
                "invalidExistingId",
                "invalidExistingId",
                Constants.getUserId(),
                70L,
                70L
        );
    }

    // Existing valid recipe, another user is creator
    public static RecipeEntity getValidFromAnotherUser() {
        return new RecipeEntity(
                "idFromAnotherUser",
                "idFromAnotherUser",
                "anotherUser",
                30L,
                40L
        );
    }

    public static RecipeEntity getInvalidFromAnotherUser() {
        return new RecipeEntity(
                getValidFromAnotherUser().getId(),
                getValidFromAnotherUser().getId(),
                getValidFromAnotherUser().getCreatedBy(),
                50L,
                60L
        );
    }

    // Existing valid recipe, expected output when cloned from another user
    public static RecipeEntity getValidNewCloned() {
        return new RecipeEntity(
                getNewInvalid().getId(),
                getValidFromAnotherUser().getId(),
                Constants.getUserId(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    // Existing invalid recipe, expected output when invalid recipe cloned, or made invalid after
    // editing copy
    public static RecipeEntity getInvalidNewCloned() {
        return new RecipeEntity(
                getNewInvalid().getId(),
                getInvalidFromAnotherUser().getId(),
                Constants.getUserId(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    public static List<RecipeEntity> getAllRecipeEntities() {
        List<RecipeEntity> recipes = new ArrayList<>();
        recipes.add(getNewValid());
        recipes.add(getValidExisting());
        recipes.add(getValidFromAnotherUser());
        recipes.add(getValidNewCloned());
        return recipes;
    }
}
























