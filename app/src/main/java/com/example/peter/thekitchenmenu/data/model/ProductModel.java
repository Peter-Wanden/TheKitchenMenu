package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

public class ProductModel {

    private ProductEntity product;
    private boolean isFavorite;
    private String favoriteProductId;

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getFavoriteProductId() {
        return favoriteProductId;
    }

    public void setFavoriteProductId(String favoriteProductId) {
        this.favoriteProductId = favoriteProductId;
    }
}
