package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;

import javax.annotation.Nonnull;

public interface DataSourceFavoriteProducts extends PrimitiveDataSource<FavoriteProductEntity> {

    void getByProductId(@Nonnull String productId,
                        @Nonnull GetEntityCallback<FavoriteProductEntity> callback);
}
