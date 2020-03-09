package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

public class RecipeMacroRequest extends RecipeRequestAbstract {

    public RecipeMacroRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
