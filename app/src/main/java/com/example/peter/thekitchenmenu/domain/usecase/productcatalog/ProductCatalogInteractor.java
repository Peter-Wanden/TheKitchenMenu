package com.example.peter.thekitchenmenu.domain.usecase.productcatalog;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.model.ProductModel;

import java.util.List;

public interface ProductCatalogInteractor {

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
