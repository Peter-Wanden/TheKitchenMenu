package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

public interface RecipeIngredientListItemNavigator {

    void deleteRecipeIngredient(String recipeIngredientId);

    void editRecipeIngredient(String recipeIngredientId, String ingredientId);
}
