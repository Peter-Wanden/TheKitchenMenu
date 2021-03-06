package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;

public class TestDataProductEntity {

    public static ProductEntity getNewInvalid() {
        return new ProductEntity(
                "newId",
                "",
                "",
                1,
                1,
                1,
                0,
                1,
                Constants.getUserId(),
                "",
                "",
                "",
                "",
                10L,
                10L
        );
    }

    public static ProductEntity getNewValid() {
        return new ProductEntity(
                getNewInvalid().getDataId(),
                "description",
                "shoppingListItemName",
                1,
                1,
                1,
                1000,
                1,
                "createdBy",
                "webImageUrl",
                "remoteSmallImageUri",
                "remoteMediumImageUri",
                "remoteLargeImageUri",
                10L,
                20L);
    }

    public static ProductEntity getExistingValid() {
        return new ProductEntity(
                "existing_valid_id",
                "description",
                "shoppingListItemName",
                1,
                1,
                1,
                5000,
                0,
                Constants.getUserId(),
                "webImageUrl",
                "remoteSmallImageUri",
                "remoteMediumImageUri",
                "remoteLargeImageUri",
                30L,
                40L
        );
    }
}
