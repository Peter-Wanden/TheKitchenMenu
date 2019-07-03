package com.example.peter.thekitchenmenu.data.repository;

import android.database.Cursor;

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

    void getProducts(@NonNull LoadProductsCallback callback);

    void getProduct(@NonNull String productId, @NonNull GetProductCallback callback);

    void saveProduct(@NonNull ProductEntity product);

    void refreshProducts();

    void deleteAllProducts();

    void deleteProduct(@NonNull String productId);

    Cursor getMatchingProducts(String searchQuery);
}
