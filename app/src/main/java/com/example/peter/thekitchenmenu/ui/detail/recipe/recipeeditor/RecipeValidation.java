package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

public interface RecipeValidation {

    interface RecipeValidatorModelSubmission {
        void submitModelStatus(RecipeModelStatus modelStatus);
    }

    interface RecipeEditor {
        void setRecipeValidationStatus(RecipeValidator.RecipeValidationStatus validationStatus);
    }
}
