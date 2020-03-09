package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.recipemacroclone;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseAbstract;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;

public final class RecipeCloneResponse extends RecipeResponseAbstract {

    private RecipeMacro clonedRecipe;
    private String clonedFromId;

    public RecipeCloneResponse(String id, String clonedFromId, RecipeMacro clonedRecipe) {
        this.id = id;
        this.clonedFromId = clonedFromId;
        this.clonedRecipe = clonedRecipe;
    }

    public RecipeCloneResponse(String id) {
        this.id = id;
    }

    public RecipeMacro getClonedRecipe() {
        return clonedRecipe;
    }
}
