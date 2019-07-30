package com.example.peter.thekitchenmenu.ui.catalog.product;

import com.example.peter.thekitchenmenu.data.model.ProductModel;

/**
 * Listener used with data binding to process user actions on a favorite product list item
 */
public interface FavoriteItemNavigator {

    void onFavoriteProductClicked(ProductModel productModel);

    void onRemoveFromFavoritesClicked(ProductModel productModel);
}
