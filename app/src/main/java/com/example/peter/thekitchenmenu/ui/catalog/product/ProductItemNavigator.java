package com.example.peter.thekitchenmenu.ui.catalog.product;

/**
 * Defines the navigation actions that can be called from a list item in the product list.
 */
public interface ProductItemNavigator {

    void viewProduct(String productId);

    void addToFavorites(String productId);

    void removeFromFavorites(String productId);
}
