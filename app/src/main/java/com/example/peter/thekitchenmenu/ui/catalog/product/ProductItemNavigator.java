package com.example.peter.thekitchenmenu.ui.catalog.product;

import com.example.peter.thekitchenmenu.data.model.ProductModel;

/**
 * Defines the navigation actions that can be called from a list item in the product list.
 */
public interface ProductItemNavigator {

    void viewProduct(ProductModel productId);

    void addToFavorites(String productId);

    void removeFromFavorites(String productId);
}
