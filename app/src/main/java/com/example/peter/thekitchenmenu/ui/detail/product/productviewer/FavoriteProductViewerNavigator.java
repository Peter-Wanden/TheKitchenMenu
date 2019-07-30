package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

/**
 * Defines favorite product actions that can be called from the ProductViewer screen
 */
public interface FavoriteProductViewerNavigator {

    void addFavoriteProduct(String productId);

    void editFavoriteProduct(String productId);
}
