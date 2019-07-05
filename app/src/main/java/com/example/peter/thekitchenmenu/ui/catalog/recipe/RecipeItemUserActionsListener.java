package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import com.example.peter.thekitchenmenu.data.model.RecipeModel;

/**
 * Listener used with data binding to process user actions on a recipe in a catalog list
 */
public interface RecipeItemUserActionsListener {

    void onRecipeClicked(RecipeModel recipeModel);

    void onAddToFavoritesClicked(RecipeModel recipeModel);

    void onRemoveFromFavoritesClicked(RecipeModel recipeModel);
}
