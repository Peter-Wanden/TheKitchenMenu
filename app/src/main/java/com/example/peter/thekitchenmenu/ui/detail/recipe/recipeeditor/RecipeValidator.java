package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeStatus.*;

public class RecipeValidator implements RecipeValidation.RecipeValidatorModelSubmission {

    private static final String TAG = "tkm-" + RecipeValidator.class.getSimpleName() + ": ";

    enum RecipeStatus {
        INVALID_MISSING_MODELS,
        INVALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_UNCHANGED,
        VALID_CHANGED
    }

    private RecipeValidation.RecipeEditor recipeEditor;
    private HashMap<RecipeState.ComponentName, RecipeComponentStateModel> recipeModelStatusList = new LinkedHashMap<>();
    private final int numberOfModels = RecipeState.ComponentName.values().length;

    void setRecipeEditor(RecipeValidation.RecipeEditor recipeEditor) {
        this.recipeEditor = recipeEditor;
    }

    @Override
    public void submitRecipeComponentStatus(RecipeComponentStateModel componentStatus) {
        System.out.println(TAG + componentStatus);
        recipeModelStatusList.put(componentStatus.getComponentName(), componentStatus);
        recipeEditor.setValidationStatus(getRecipeValidationStatus());
    }

    private RecipeStatus getRecipeValidationStatus() {
        int numberOfModelsInList = recipeModelStatusList.size();

        if (numberOfModels == numberOfModelsInList) {
            boolean recipeHasChanged = false;
            boolean recipeIsValid = true;

            for (RecipeState.ComponentName componentName : RecipeState.ComponentName.values()) {
                RecipeComponentStateModel modelStatus = recipeModelStatusList.get(componentName);

//                if (modelStatus.isChanged())
//                    recipeHasChanged = true;
//
//                if (!modelStatus.isValid())
//                    recipeIsValid = false;
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
