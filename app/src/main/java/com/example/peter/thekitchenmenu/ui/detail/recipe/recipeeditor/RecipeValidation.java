package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

public interface RecipeValidation {

    interface RecipeValidatorModelSubmission {
        void submitRecipeComponentStatus(RecipeComponentStatus modelStatus);
    }

    interface RecipeEditor {
        void setValidationStatus(RecipeValidator.RecipeValidationStatus validationStatus);
    }
}
