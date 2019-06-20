package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

public class FavoriteProductModel {

    private static final String TAG = "FavoriteProductModel";

    private FavoriteProductEntity favoriteProduct;
    private ProductEntity product;

    public FavoriteProductEntity getFavoriteProduct() {
        return favoriteProduct;
    }

    public void setFavoriteProduct(FavoriteProductEntity favoriteProduct) {
        this.favoriteProduct = favoriteProduct;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}
