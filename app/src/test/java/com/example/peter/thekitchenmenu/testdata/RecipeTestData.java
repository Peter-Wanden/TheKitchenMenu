package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeIdentityModelMetaData;

public class RecipeTestData {

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    // A valid recipe entered by the current user
    public static RecipeEntity getValidExistingRecipeEntity() {
        return new RecipeEntity(
                "id",
                "id",
                "title",
                "description",
                90,
                90,
                Constants.getUserId().getValue(),
                10L,
                10L
        );
    }
    // A valid recipe identity model with meta data
    public static RecipeIdentityModelMetaData getValidExistingRecipeIdentityModelMetadataData() {
        return new RecipeIdentityModelMetaData(
                getValidExistingRecipeIdentityModelData(),
                false,
                true
        );
    }

    // A valid recipe identity model
    public static RecipeIdentityModel getValidExistingRecipeIdentityModelData() {
        return new RecipeIdentityModel(
                getValidExistingRecipeEntity().getTitle(),
                getValidExistingRecipeEntity().getDescription(),
                getValidExistingRecipeEntity().getId(),
                getValidExistingRecipeEntity().getPreparationTime(),
                getValidExistingRecipeEntity().getCookingTime()
        );
    }

    // The applications invalid empty entity
    public static RecipeEntity getEmptyRecipeEntity() {
        return new RecipeEntity(
                "",
                "",
                "",
                "",
                0,
                0,
                "",
                0L,
                0L);
    }

    // An invalid empty recipe identity model with meta data
    public static RecipeIdentityModelMetaData getEmptyRecipeIdentityModelMetadata() {
        return new RecipeIdentityModelMetaData(
                getEmptyRecipeIdentityModel(), false, false
        );
    }

    // An invalid empty recipe identity model
    public static RecipeIdentityModel getEmptyRecipeIdentityModel() {
        return new RecipeIdentityModel(
                getEmptyRecipeEntity().getTitle(),
                getEmptyRecipeEntity().getDescription(),
                getValidExistingRecipeEntity().getId(),
                getEmptyRecipeEntity().getPreparationTime(),
                getEmptyRecipeEntity().getCookingTime()
        );
    }

    // An invalid recipe entity
    public static RecipeEntity getInvalidRecipeEntity() {
        return new RecipeEntity(
                "id",
                "id",
                "ti",
                "de",
                10000,
                10000,
                Constants.getUserId().getValue(),
                10L,
                10L);
    }

    public static RecipeIdentityModelMetaData getInvalidRecipeIdentityModelMetaData() {
        return new RecipeIdentityModelMetaData(
                getInvalidRecipeIdentityModel(),
                true,
                false
        );
    }

    public static RecipeIdentityModel getInvalidRecipeIdentityModel() {
        return new RecipeIdentityModel(
                getInvalidRecipeEntity().getTitle(),
                getEmptyRecipeEntity().getDescription(),
                getValidExistingRecipeEntity().getId(),
                getInvalidRecipeEntity().getPreparationTime(),
                getInvalidRecipeEntity().getCookingTime()
        );
    }

    public static RecipeEntity getValidRecipeEntityUpdatedByOwner() {
        return new RecipeEntity(
                getValidExistingRecipeEntity().getId(),
                getValidExistingRecipeEntity().getId(),
                getValidExistingRecipeEntity().getTitle(),
                "updated description",
                120,
                120,
                Constants.getUserId().getValue(),
                10L,
                15L);

    }

    public static RecipeIdentityModelMetaData getValidRecipeIdentityModelMetadataUpdatedData() {
        return new RecipeIdentityModelMetaData(
                getValidRecipeIdentityModelUpdatedData(),
                true,
                true
        );
    }

    public static RecipeIdentityModel getValidRecipeIdentityModelUpdatedData() {
        return new RecipeIdentityModel(
                getValidExistingRecipeEntity().getTitle(),
                getValidRecipeEntityUpdatedByOwner().getDescription(),
                getValidExistingRecipeEntity().getId(),
                getValidRecipeEntityUpdatedByOwner().getPreparationTime(),
                getValidRecipeEntityUpdatedByOwner().getCookingTime()
        );
    }

    public static RecipeEntity getValidRecipeEntityFromAnotherUser() {
        return new RecipeEntity(
                "id1",
                "1d1",
                "title1",
                "description1",
                150,
                150,
                "anotherUser",
                5L,
                10L
        );
    }

    public static RecipeIdentityModelMetaData getValidRecipeIdentityModelMetaDataFromAnotherUser() {
        return new RecipeIdentityModelMetaData(
                getValidRecipeIdentityModelFromAnotherUser(),
                false,
                true
        );
    }

    public static RecipeIdentityModel getValidRecipeIdentityModelFromAnotherUser() {
        return new RecipeIdentityModel(
                getValidRecipeEntityFromAnotherUser().getTitle(),
                getValidRecipeEntityFromAnotherUser().getDescription(),
                getValidRecipeEntityFromAnotherUser().getId(),
                getValidRecipeEntityFromAnotherUser().getPreparationTime(),
                getValidRecipeEntityFromAnotherUser().getCookingTime()
        );
    }

    public static RecipeEntity getValidClonedRecipeEntity() {
        return new RecipeEntity(
                "id2",
                getValidRecipeEntityFromAnotherUser().getId(),
                getValidRecipeEntityFromAnotherUser().getTitle(),
                "description2",
                45,
                45,
                Constants.getUserId().getValue(),
                20L,
                20L
        );
    }

    public static RecipeIdentityEntity getValidRecipeIdentityEntity() {
        return new RecipeIdentityEntity(
                getValidExistingRecipeEntity().getId(),
                getValidExistingRecipeEntity().getParentId(),
                getValidExistingRecipeEntity().getTitle(),
                getValidExistingRecipeEntity().getDescription(),
                getValidExistingRecipeEntity().getPreparationTime(),
                getValidExistingRecipeEntity().getCookingTime(),
                getValidExistingRecipeEntity().getCreateDate(),
                getValidExistingRecipeEntity().getLastUpdate()
        );
    }

    public static RecipeIdentityModel getValidClonedRecipeIdentityModel() {
        return new RecipeIdentityModel(
                getValidRecipeEntityFromAnotherUser().getTitle(),
                getValidClonedRecipeEntity().getDescription(),
                getValidExistingRecipeEntity().getId(),
                getValidClonedRecipeEntity().getPreparationTime(),
                getValidClonedRecipeEntity().getCookingTime()
        );
    }
}
