package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeMetadataEntity {

    // Create new recipe, or used when cloning
    public static RecipeMetadataParentEntity getNewInvalid() {
        return new RecipeMetadataParentEntity(
                "newId",
                "newId",
                Constants.getUserId(),
                10L,
                10L
        );
    }

    // Built from new, or cloned / copied to current user
    public static RecipeMetadataParentEntity getNewValid() {
        return new RecipeMetadataParentEntity(
                getNewInvalid().getDataId(),
                getNewInvalid().getDataId(),
                getNewInvalid().getCreatedBy(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    // Existing valid recipe loaded, current user is creator
    public static RecipeMetadataParentEntity getValidExisting() {
        return new RecipeMetadataParentEntity(
                "validExistingRecipeId",
                "validExistingRecipeId",
                Constants.getUserId(),
                20L,
                20L
        );
    }

    // Existing invalid recipe, current user is creator, one or more recipe data models are invalid
    public static RecipeMetadataParentEntity getInvalidExisting() {
        return new RecipeMetadataParentEntity(
                "invalidExistingId",
                "invalidExistingId",
                Constants.getUserId(),
                70L,
                70L
        );
    }

    // Existing valid recipe, another user is creator
    public static RecipeMetadataParentEntity getValidFromAnotherUser() {
        return new RecipeMetadataParentEntity(
                "idFromAnotherUser",
                "idFromAnotherUser",
                "anotherUser",
                30L,
                40L
        );
    }

    public static RecipeMetadataParentEntity getInvalidFromAnotherUser() {
        return new RecipeMetadataParentEntity(
                getValidFromAnotherUser().getDataId(),
                getValidFromAnotherUser().getDataId(),
                getValidFromAnotherUser().getCreatedBy(),
                50L,
                60L
        );
    }

    // Existing valid recipe, expected output when cloned from another user
    public static RecipeMetadataParentEntity getValidNewCloned() {
        return new RecipeMetadataParentEntity(
                getNewInvalid().getDataId(),
                getValidFromAnotherUser().getDataId(),
                Constants.getUserId(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    // Existing invalid recipe, expected output when invalid recipe cloned, or made invalid after
    // editing copy
    public static RecipeMetadataParentEntity getInvalidNewCloned() {
        return new RecipeMetadataParentEntity(
                getNewInvalid().getDataId(),
                getInvalidFromAnotherUser().getDataId(),
                Constants.getUserId(),
                getNewInvalid().getCreateDate(),
                getNewInvalid().getLastUpdate()
        );
    }

    public static List<RecipeMetadataParentEntity> getAllRecipeEntities() {
        List<RecipeMetadataParentEntity> recipes = new ArrayList<>();
        recipes.add(getNewValid());
        recipes.add(getValidExisting());
        recipes.add(getValidFromAnotherUser());
        recipes.add(getValidNewCloned());
        return recipes;
    }
}
























