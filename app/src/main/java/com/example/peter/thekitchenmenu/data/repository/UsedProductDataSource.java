package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public interface UsedProductDataSource {

    interface LoadUsedProductsCallback {

        void onUsedProductsLoaded(List<UsedProductEntity> usedProducts);

        void onDataNotAvailable();
    }

    interface GetUsedProductCallback {

        void onUsedProductLoaded(UsedProductEntity usedProduct);

        void onDataNotAvailable();
    }

    void getUsedProducts(@NotNull LoadUsedProductsCallback callback);

    void getUsedProduct(@NonNull String usedProductId, @NonNull GetUsedProductCallback callback);

    void getUsedProductByProductId(@NonNull String productId,
                                   @NonNull GetUsedProductCallback callback);

    void saveUsedProduct(@NonNull UsedProductEntity usedProduct);

    void deleteAllUsedProducts();

    void deleteUsedProduct(String usedProductId);
}
