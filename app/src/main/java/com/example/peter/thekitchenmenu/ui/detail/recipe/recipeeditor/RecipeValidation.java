package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

public interface RecipeValidation {

    interface RecipeValidatorModelSubmission {
        void submitRecipeComponentStatus(RecipeComponentStateModel modelStatus);
    }

    interface RecipeEditor {
        void setValidationStatus(RecipeValidator.RecipeStatus validationStatus);
    }
}
