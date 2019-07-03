package com.example.peter.thekitchenmenu.ui.catalog.recipe;

public interface RecipeNavigator {

    void addRecipe();

    void editRecipe(String recipeId);

    void addRecipeToPlanner(String recipeId);
}
