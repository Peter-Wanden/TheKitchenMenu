package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

public interface AddEditProductNavigator {

    void reviewEditedProduct(ProductEntity productEntity);

    void reviewNewProduct(ProductEntity productEntity);

    void cancelEditing();
}
