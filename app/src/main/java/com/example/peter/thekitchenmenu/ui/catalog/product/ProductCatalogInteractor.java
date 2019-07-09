package com.example.peter.thekitchenmenu.ui.catalog.product;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductModel;

import java.util.List;

interface ProductCatalogInteractor {

    interface GetAllCallback {

        void onAllLoaded(List<ProductModel> entities);

        void onDataNotAvailable();
    }

    interface GetProductModelCallback {

        void onModelLoaded(ProductModel productModel);

        void onDataNotAvailable();
    }

    void getProductModelList(@NonNull GetAllCallback callback);

    void getFavoriteProductModelList(@NonNull GetAllCallback callback);

    void removeFavoriteProduct(String favoriteProductId);
}
