package com.example.peter.thekitchenmenu.ui.catalog.recipe;

public interface RecipeNavigator {

    void viewRecipe(String recipeId);

    void addRecipe();

    void editRecipe(String recipeId);

    void addRecipeToPlanner(String recipeId);
}
