package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

/**
 * Defines the actions that can be called from the ProductViewer details screen
 */
public interface ProductViewerNavigator {

    void editProduct(String productId);

    void deleteProduct(String productId);

    void deleteFavoriteProduct();

    void addNewFavoriteProduct();

    void editFavoriteProduct();
}
