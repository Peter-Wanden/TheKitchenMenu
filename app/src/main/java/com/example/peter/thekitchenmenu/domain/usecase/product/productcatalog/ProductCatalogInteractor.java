package com.example.peter.thekitchenmenu.domain.usecase.product.productcatalog;

import com.example.peter.thekitchenmenu.data.model.ProductModel;

import java.util.List;

import javax.annotation.Nonnull;

public interface ProductCatalogInteractor {

    interface GetAllCallback {

        void onAllLoaded(List<ProductModel> entities);

        void onDataNotAvailable();
    }

    interface GetProductModelCallback {

        void onModelLoaded(ProductModel productModel);

        void onDataNotAvailable();
    }

    void getProductModelList(@Nonnull GetAllCallback callback);

    void getFavoriteProductModelList(@Nonnull GetAllCallback callback);

    void removeFavoriteProduct(String favoriteProductId);
}
