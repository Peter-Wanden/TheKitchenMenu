package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

public class ProductTestData {

    public static ProductEntity getProductEntity() {
        return new ProductEntity(
                "id",
                "description",
                "shoppingListItemName",
                1,
                2,
                2,
                1000,
                1,
                "createdBy",
                "webImageUrl",
                "remoteSmallImageUri",
                "remoteMediumImageUri",
                "remoteLargeImageUri",
                10000L,
                20000L);
    }
}
