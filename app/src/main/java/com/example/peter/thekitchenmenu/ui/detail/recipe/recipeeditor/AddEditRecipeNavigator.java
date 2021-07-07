package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

public interface AddEditRecipeNavigator {

    void reviewNewRecipe(String recipeId);

    void reviewEditedRecipe(String recipeId);

    void reviewClonedRecipe(String recipeId);

    void addIngredients(String recipeId);

    void editIngredients(String recipeId);

    void reviewIngredients(String recipeId);

    void cancelEditing();

    void refreshOptionsMenu();

    void setActivityTitle(int activityTitleResourceId);

    void showUnsavedChangedDialog();
}
