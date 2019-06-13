package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;

public class UsedProductDataModel {

    private static final String TAG = "UsedProductDataModel";

    private UsedProductEntity usedProduct;
    private ProductEntity product;

    public UsedProductDataModel(UsedProductEntity usedProduct, ProductEntity product) {
        this.usedProduct = usedProduct;
        this.product = product;
    }

    public UsedProductEntity getUsedProduct() {
        return usedProduct;
    }

    public void setUsedProduct(UsedProductEntity usedProduct) {
        this.usedProduct = usedProduct;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}
