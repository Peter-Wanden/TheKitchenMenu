package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeValidationStatus.*;

public class RecipeValidator implements RecipeValidation.RecipeValidatorModelSubmission {

    private static final String TAG = "tkm-RecipeValidator";

    enum RecipeValidationStatus {
        INVALID_MISSING_MODELS,
        INVALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_UNCHANGED,
        VALID_CHANGED
    }

    public enum ModelName {
        IDENTITY_MODEL,
        COURSES_MODEL,
        DURATION_MODEL
    }

    private RecipeValidation.RecipeEditor recipeEditor;
    private HashMap<ModelName, RecipeModelStatus> recipeModelStatusList = new LinkedHashMap<>();
    private final int numberOfModels = ModelName.values().length;

    void setRecipeEditor(RecipeValidation.RecipeEditor recipeEditor) {
        this.recipeEditor = recipeEditor;
    }

    @Override
    public void submitModelStatus(RecipeModelStatus modelStatus) {
        recipeModelStatusList.put(modelStatus.getModelName(), modelStatus);
        recipeEditor.setValidationStatus(getRecipeValidationStatus());
    }

    private RecipeValidationStatus getRecipeValidationStatus() {
        int numberOfModelsInList = recipeModelStatusList.size();

        if (numberOfModels == numberOfModelsInList) {
            boolean recipeHasChanged = false;
            boolean recipeIsValid = true;

            for (ModelName modelName : ModelName.values()) {
                RecipeModelStatus modelStatus = recipeModelStatusList.get(modelName);

                if (modelStatus.isChanged())
                    recipeHasChanged = true;

                if (!modelStatus.isValid())
                    recipeIsValid = false;
            }

            if(!recipeIsValid && !recipeHasChanged)
                return INVALID_UNCHANGED;

            if (!recipeIsValid && recipeHasChanged)
                return INVALID_CHANGED;

            if (recipeIsValid && !recipeHasChanged)
                return VALID_UNCHANGED;

            return VALID_CHANGED;

        } else
            return INVALID_MISSING_MODELS;
    }
}
