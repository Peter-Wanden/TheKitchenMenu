package com.example.peter.thekitchenmenu.ui.catalog.product;

import com.example.peter.thekitchenmenu.data.model.ProductModel;

/**
 * Listener used with data binding to process user actions on a favorite product list item in a
 * catalog
 */
public interface FavoriteProductItemUserActionsListener {

    void onFavoriteProductClicked(ProductModel productModel);

    void onRemoveFromFavoritesClicked(ProductModel productModel);
}
