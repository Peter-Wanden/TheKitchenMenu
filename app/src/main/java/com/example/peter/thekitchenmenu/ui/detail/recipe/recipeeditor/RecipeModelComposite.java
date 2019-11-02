package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

// Allows RecipeEditorVM to push commands to registered RecipeViewModels
class RecipeModelComposite {

    public interface RecipeModelActions {
        void start(String recipeId);
        void startByCloningModel(String oldRecipeId, String newRecipeId);
    }

    private List<RecipeModelActions> recipeModelCompositeList = new ArrayList<>();

    void registerModel(RecipeModelActions recipeModelActions) {
        recipeModelCompositeList.add(recipeModelActions);
    }

    void start(@NonNull String recipeId) {
        for (RecipeModelActions recipeModelActions : recipeModelCompositeList)
            recipeModelActions.start(recipeId);
    }

    void startWithClonedModel(@NonNull String oldRecipeId, @NonNull String newRecipeId) {
        for (RecipeModelActions recipeModelActions : recipeModelCompositeList)
            recipeModelActions.startByCloningModel(oldRecipeId, newRecipeId);
    }
}
