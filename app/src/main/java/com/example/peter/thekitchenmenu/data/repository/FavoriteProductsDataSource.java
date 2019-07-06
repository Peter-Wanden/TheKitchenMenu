package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;

public interface FavoriteProductsDataSource extends DataSource<FavoriteProductEntity> {

    void getFavoriteProductByProductId(@NonNull String productId,
                                       @NonNull GetEntityCallback<FavoriteProductEntity> callback);
}
