package com.example.peter.thekitchenmenu.ui.catalog.product;

import com.example.peter.thekitchenmenu.data.model.ProductModel;

/**
 * Listener used with data binding to process user actions on a product in a catalog list
 */
public interface ProductItemUserActionsListener {

    void onProductClicked(ProductModel productModel);

    void onAddToFavoritesClicked(ProductModel productModel);

    void onRemoveFromFavoritesClicked(ProductModel productModel);
}
