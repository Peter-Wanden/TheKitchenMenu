package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.lifecycle.ViewModel;

public class RecipePortionsViewModel extends ViewModel implements RecipeModelComposite.RecipeModelActions {

    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    @Override
    public void start(String recipeId) {

    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {

    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }
}
