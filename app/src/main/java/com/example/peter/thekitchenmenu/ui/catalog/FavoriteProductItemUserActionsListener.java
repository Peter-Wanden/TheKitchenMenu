package com.example.peter.thekitchenmenu.ui.catalog;

import com.example.peter.thekitchenmenu.data.model.FavoriteProductModel;

/**
 * Listener used with data binding to process user actions on a favorite product list item in a
 * catalog
 */
public interface FavoriteProductItemUserActionsListener {

    void onFavoriteProductClicked(FavoriteProductModel favoriteProduct);

    void onRemoveFromFavoritesClicked(FavoriteProductModel favoriteProductModel);
}
