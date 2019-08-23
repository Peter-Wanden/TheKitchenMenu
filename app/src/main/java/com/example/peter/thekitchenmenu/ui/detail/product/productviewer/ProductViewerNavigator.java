package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

/**
 * Defines the product related actions that can be called from the ProductViewer screen
 */
public interface ProductViewerNavigator {

    void editProduct(ProductEntity productEntity);

    void discardProductEdits();

    void deleteProduct(String productId);

    void doneWithProduct(String productId);

    void postProduct();

    void showUnsavedChangesDialog();
}
