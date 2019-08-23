package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeIdentityModelMetaData;

public class RecipeTestData {

    public static RecipeEntity getEmptyRecipeEntity() {
        return RecipeEditorViewModel.EMPTY_RECIPE;
    }

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    public static RecipeEntity getValidExistingRecipeEntity() {
        return new RecipeEntity(
                "id",
                "titleObservable",
                "description",
                90,
                90,
                Constants.getUserId().getValue(),
                10L,
                10L
        );
    }

    public static RecipeIdentityModelMetaData getValidExistingRecipeIdentityModelMetadataData() {
        return new RecipeIdentityModelMetaData(
                getValidExistingRecipeIdentityModelData(),
                false,
                true
        );
    }

    public static RecipeIdentityModel getValidExistingRecipeIdentityModelData() {
        return new RecipeIdentityModel(
                "titleObservable",
                "description",
                90,
                90
        );
    }

    public static RecipeIdentityModelMetaData getEmptyRecipeIdentityModelMetadata() {
        return new RecipeIdentityModelMetaData(
                getEmptyRecipeIdentityModel(), false, false
        );
    }

    public static RecipeIdentityModel getEmptyRecipeIdentityModel() {
        return new RecipeIdentityModel(
                "",
                "",
                0,
                0
        );
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
                "",
                "de",
                10000,
                10000
        );
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
                "titleObservable",
                "description",
                120,
                120
        );
    }

    public static RecipeEntity getRecipeEntityWithUpdatedData() {
        return new RecipeEntity(
                getValidExistingRecipeEntity().getId(),
                getValidRecipeIdentityModelUpdatedData().getTitle(),
                getValidRecipeIdentityModelUpdatedData().getDescription(),
                getValidRecipeIdentityModelUpdatedData().getPrepTime(),
                getValidRecipeIdentityModelUpdatedData().getCookTime(),
                getValidExistingRecipeEntity().getCreatedBy(),
                getValidExistingRecipeEntity().getCreateDate(),
                20L
        );
    }
}
