package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public interface ProductDataSource {

    interface LoadProductsCallback {

        void onProductsLoaded(List<ProductEntity> products);

        void onDataNotAvailable();
    }

    interface GetProductCallback {

        void onProductLoaded(ProductEntity product);

        void onDataNotAvailable();
    }

    void getProducts(@NotNull LoadProductsCallback callback);

    void getProduct(@NonNull String productId, @NonNull GetProductCallback callback);

    void saveProduct(@NotNull ProductEntity product);

    void refreshProducts();

    void deleteAllProducts();

    void deleteProduct(@NonNull String productId);
}
