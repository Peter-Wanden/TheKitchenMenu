package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeValidationStatus.*;

public class RecipeValidator implements RecipeValidation.RecipeValidatorModelSubmission {

    enum RecipeValidationStatus {
        INVALID_MISSING_MODELS,
        INVALID_NO_CHANGES,
        INVALID_HAS_CHANGES,
        VALID_NO_CHANGES,
        VALID_HAS_CHANGES
    }

    public enum ModelName {
        IDENTITY_MODEL,
        COURSES_MODEL
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
        recipeEditor.setRecipeValidationStatus(getRecipeValidationStatus());
    }

    RecipeValidationStatus getRecipeValidationStatus(RecipeModelStatus modelStatus) {
        recipeModelStatusList.put(modelStatus.getModelName(), modelStatus);
        return getRecipeValidationStatus();
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
                return INVALID_NO_CHANGES;

            if (!recipeIsValid && recipeHasChanged)
                return INVALID_HAS_CHANGES;

            if (recipeIsValid && !recipeHasChanged)
                return VALID_NO_CHANGES;

            return VALID_HAS_CHANGES;

        } else
            return INVALID_MISSING_MODELS;
    }
}
