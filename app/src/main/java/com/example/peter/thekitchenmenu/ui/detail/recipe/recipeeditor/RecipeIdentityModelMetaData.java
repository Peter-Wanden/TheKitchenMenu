package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;

public final class RecipeIdentityModelMetaData {

    private final RecipeIdentityModel identityModel;
    private final boolean modelChanged;
    private final boolean validModel;


    public RecipeIdentityModelMetaData(RecipeIdentityModel identityModel,
                                       boolean modelChanged,
                                       boolean validModel) {
        this.identityModel = identityModel;
        this.modelChanged = modelChanged;
        this.validModel = validModel;
    }

    public RecipeIdentityModel getIdentityModel() {
        return identityModel;
    }

    public boolean isModelChanged() {
        return modelChanged;
    }

    public boolean isValidModel() {
        return validModel;
    }
}
