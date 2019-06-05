package com.example.peter.thekitchenmenu.ui.catalog;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

/**
 * Defines the navigation actions that can be called from a list item in the product list.
 */
public interface ProductItemNavigator {

    void openProductDetails(ProductEntity product, boolean isCreator);
}
