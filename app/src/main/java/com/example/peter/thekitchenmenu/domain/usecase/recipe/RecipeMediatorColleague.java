package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;

public interface RecipeMediatorColleague {

    void recipeComponentStatusChanged();

    RecipeState.ComponentState getState();

    void startColleague(String recipeId);

    String getRecipeId();
}
