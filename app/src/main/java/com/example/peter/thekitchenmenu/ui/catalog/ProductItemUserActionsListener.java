package com.example.peter.thekitchenmenu.ui.catalog;


import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

/**
 * Listener used with data binding to process user actions on a product in a catalog list
 */
public interface ProductItemUserActionsListener {

    void onProductClicked(ProductEntity product);
}
