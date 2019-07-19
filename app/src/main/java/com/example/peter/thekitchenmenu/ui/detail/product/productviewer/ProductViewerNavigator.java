package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

/**
 * Defines the actions that can be called from the ProductViewer details screen
 */
public interface ProductViewerNavigator {

    void editProduct(ProductEntity productEntity);

    void deleteProduct(String productId);

    void doneWithProduct(String productId);
}
