package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

public interface RecipeComponent {

    void start(String recipeId);

    void cloneComponent(String oldRecipeId, String newRecipeId);
}
