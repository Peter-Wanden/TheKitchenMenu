package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeMetaDataEntity {

    // Create new recipe, or used when cloning
    public static RecipeMetadataEntity getNewInvalid() {
        return new RecipeMetadataEntity(
                "newId",
                "newId",
                Constants.getUserId(),
                10L,
                10L
        );
    }

    // Built from new, or cloned / copied to current user
    public static RecipeMetadataEntity getNewValid() {
        return new RecipeMetadataEntity(
                getNewInvalid().getId(),
                getNewInvalid().getId(),
                getNewInvalid().getCreatedBy(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    // Existing valid recipe loaded, current user is creator
    public static RecipeMetadataEntity getValidExisting() {
        return new RecipeMetadataEntity(
                "validExistingRecipeId",
                "validExistingRecipeId",
                Constants.getUserId(),
                20L,
                20L
        );
    }

    // Existing invalid recipe, current user is creator, one or more recipe data models are invalid
    public static RecipeMetadataEntity getInvalidExisting() {
        return new RecipeMetadataEntity(
                "invalidExistingId",
                "invalidExistingId",
                Constants.getUserId(),
                70L,
                70L
        );
    }

    // Existing valid recipe, another user is creator
    public static RecipeMetadataEntity getValidFromAnotherUser() {
        return new RecipeMetadataEntity(
                "idFromAnotherUser",
                "idFromAnotherUser",
                "anotherUser",
                30L,
                40L
        );
    }

    public static RecipeMetadataEntity getInvalidFromAnotherUser() {
        return new RecipeMetadataEntity(
                getValidFromAnotherUser().getId(),
                getValidFromAnotherUser().getId(),
                getValidFromAnotherUser().getCreatedBy(),
                50L,
                60L
        );
    }

    // Existing valid recipe, expected output when cloned from another user
    public static RecipeMetadataEntity getValidNewCloned() {
        return new RecipeMetadataEntity(
                getNewInvalid().getId(),
                getValidFromAnotherUser().getId(),
                Constants.getUserId(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    // Existing invalid recipe, expected output when invalid recipe cloned, or made invalid after
    // editing copy
    public static RecipeMetadataEntity getInvalidNewCloned() {
        return new RecipeMetadataEntity(
                getNewInvalid().getId(),
                getInvalidFromAnotherUser().getId(),
                Constants.getUserId(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    public static List<RecipeMetadataEntity> getAllRecipeEntities() {
        List<RecipeMetadataEntity> recipes = new ArrayList<>();
        recipes.add(getNewValid());
        recipes.add(getValidExisting());
        recipes.add(getValidFromAnotherUser());
        recipes.add(getValidNewCloned());
        return recipes;
    }
}
























