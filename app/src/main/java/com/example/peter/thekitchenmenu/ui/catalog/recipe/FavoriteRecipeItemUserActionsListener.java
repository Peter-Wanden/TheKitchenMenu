package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import com.example.peter.thekitchenmenu.data.model.FavoriteRecipeModel;

/**
 * Listener used with data binding to process user actions on a favorite product list item in a
 * catalog
 */
public interface FavoriteRecipeItemUserActionsListener {

    void onFavoriteRecipeClicked(FavoriteRecipeModel favoriteRecipeModel);

    void onRemoveFromFavoritesClicked(FavoriteRecipeModel favoriteRecipeModel);
}
