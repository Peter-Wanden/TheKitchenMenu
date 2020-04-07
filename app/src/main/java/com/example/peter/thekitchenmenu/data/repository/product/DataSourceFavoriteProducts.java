package com.example.peter.thekitchenmenu.data.repository.product;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource;

import javax.annotation.Nonnull;

public interface DataSourceFavoriteProducts extends PrimitiveDataSource<FavoriteProductEntity> {

    void getByProductId(@Nonnull String productId,
                        @Nonnull GetPrimitiveCallback<FavoriteProductEntity> callback);
}
