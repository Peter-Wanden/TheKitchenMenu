package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;

public interface FavoriteProductsDataSource extends DataSource<FavoriteProductEntity> {

    void getByProductId(@NonNull String productId,
                        @NonNull GetEntityCallback<FavoriteProductEntity> callback);
}
