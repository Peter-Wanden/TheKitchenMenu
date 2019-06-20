package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public interface FavoriteProductsDataSource {

    interface LoadFavoriteProductsCallback {

        void onFavoriteProductsLoaded(List<FavoriteProductEntity> favoriteProducts);

        void onDataNotAvailable();
    }

    interface GetFavoriteProductCallback {

        void onFavoriteProductLoaded(FavoriteProductEntity favoriteProduct);

        void onDataNotAvailable();
    }

    void getFavoriteProducts(@NotNull LoadFavoriteProductsCallback callback);

    void getFavoriteProduct(@NonNull String favoriteProductId,
                            @NonNull GetFavoriteProductCallback callback);

    void getFavoriteProductByProductId(@NonNull String productId,
                                       @NonNull GetFavoriteProductCallback callback);

    void saveFavoriteProduct(@NonNull FavoriteProductEntity favoriteProduct);

    void refreshFavoriteProducts();

    void deleteAllFavoriteProducts();

    void deleteFavoriteProduct(String favoriteProductId);
}
