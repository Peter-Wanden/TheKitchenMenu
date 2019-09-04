package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;

public final class RecipeModelStatus {

    @NonNull
    private final RecipeValidator.ModelName modelName;
    private final boolean isChanged;
    private final boolean isValid;

    public RecipeModelStatus(@NonNull RecipeValidator.ModelName modelName,
                             boolean isChanged,
                             boolean isValid) {
        this.modelName = modelName;
        this.isChanged = isChanged;
        this.isValid = isValid;
    }

    @NonNull
    RecipeValidator.ModelName getModelName() {
        return modelName;
    }

    boolean isChanged() {
        return isChanged;
    }

    boolean isValid() {
        return isValid;
    }
}
