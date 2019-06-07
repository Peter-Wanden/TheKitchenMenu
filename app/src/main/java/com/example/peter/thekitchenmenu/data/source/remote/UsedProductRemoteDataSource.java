package com.example.peter.thekitchenmenu.data.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.repository.UsedProductDataSource;

public class UsedProductRemoteDataSource implements UsedProductDataSource {

    private static UsedProductRemoteDataSource INSTANCE;

    public static UsedProductRemoteDataSource getInstance() {
        if (INSTANCE == null) INSTANCE = new UsedProductRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getUsedProducts(LoadUsedProductsCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getUsedProduct(@NonNull String usedProductId, @NonNull GetUsedProductCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void saveUsedProduct(@NonNull UsedProductEntity usedProduct) {

    }

    @Override
    public void deleteAllUsedProducts() {

    }

    @Override
    public void deleteUsedProduct(String usedProductId) {

    }
}
