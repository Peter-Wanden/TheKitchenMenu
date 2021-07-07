package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;

public class ProductModel {

    private ProductEntity productEntity;
    private FavoriteProductEntity favoriteProductEntity;
    private boolean isFavorite;

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public FavoriteProductEntity getFavoriteProductEntity() {
        return favoriteProductEntity;
    }

    public void setFavoriteProductEntity(FavoriteProductEntity favoriteProductEntity) {
        this.favoriteProductEntity = favoriteProductEntity;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
