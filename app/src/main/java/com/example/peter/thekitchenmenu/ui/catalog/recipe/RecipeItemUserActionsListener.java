package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

/**
 * Listener used with data binding to process user actions on a recipe in a catalog list
 */
public interface RecipeItemUserActionsListener {

    void onRecipeClicked(RecipeEntity recipeEntity);

    void onAddToFavoritesClicked(RecipeEntity recipeEntity);

    void onRemoveFromFavoritesClicked(RecipeEntity recipeEntity);
}
