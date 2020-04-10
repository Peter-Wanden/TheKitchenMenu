package com.example.peter.thekitchenmenu.domain.usecase.productcatalog;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.product.RepositoryFavoriteProduct;
import com.example.peter.thekitchenmenu.data.repository.product.RepositoryProduct;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class ProductCatalogInteractorImpl implements ProductCatalogInteractor {

    private static final String TAG = "tkm-" + ProductCatalogInteractorImpl.class.getSimpleName()
            + ":";

    private static volatile ProductCatalogInteractorImpl INSTANCE = null;
    private final RepositoryProduct productEntityDataSource;
    private final RepositoryFavoriteProduct favoriteProductEntityDataSource;

    private boolean productEntitiesLoading;
    private boolean favoriteProductEntitiesLoading;
    private ProductCatalogInteractor.GetAllCallback productListCallback;
    private ProductCatalogInteractor.GetAllCallback favoriteProductListCallback;
    private LinkedHashMap<String, ProductEntity> productsMap = new LinkedHashMap<>();
    private LinkedHashMap<String, FavoriteProductEntity> favoriteProductsMap = new LinkedHashMap<>();

    private ProductCatalogInteractorImpl(
            @Nonnull RepositoryProduct repositoryProduct,
            @Nonnull RepositoryFavoriteProduct repositoryFavoriteProduct) {

        this.productEntityDataSource = repositoryProduct;
        this.favoriteProductEntityDataSource = repositoryFavoriteProduct;
    }

    public static ProductCatalogInteractorImpl getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ProductCatalogInteractorImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProductCatalogInteractorImpl(
                            DatabaseInjection.provideProductDataSource(
                                    application.getApplicationContext()),
                            DatabaseInjection.provideFavoriteProductsDataSource(
                                    application.getApplicationContext()
                            )
                    );
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getProductModelList(@Nonnull GetAllCallback productListCallback) {
        this.productListCallback = productListCallback;
        productsMap.clear();
        productEntitiesLoading = true;
        productEntityDataSource.getAll(
                new PrimitiveDataSource.GetAllPrimitiveCallback<ProductEntity>() {
                    @Override
                    public void onAllLoaded(List<ProductEntity> productEntityList) {

                        for (ProductEntity productEntity : productEntityList) {
                            productsMap.put(productEntity.getDataId(), productEntity);
                        }

                        productEntitiesLoading = false;
                        sortDataWhenLoaded();
                    }

                    @Override
                    public void onDataUnavailable() {
                        productEntitiesLoading = false;
                        productListCallback.onDataNotAvailable();
                        sortDataWhenLoaded();
                    }
                });
    }

    @Override
    public void getFavoriteProductModelList(@Nonnull GetAllCallback favoriteProductListCallback) {
        this.favoriteProductListCallback = favoriteProductListCallback;
        favoriteProductsMap.clear();
        favoriteProductEntitiesLoading = true;
        favoriteProductEntityDataSource.getAll(
                new PrimitiveDataSource.GetAllPrimitiveCallback<FavoriteProductEntity>() {
                    @Override
                    public void onAllLoaded(List<FavoriteProductEntity> favoriteProductEntityList) {

                        for (FavoriteProductEntity favoriteProductEntity : favoriteProductEntityList) {
                            favoriteProductsMap.put(
                                    favoriteProductEntity.getProductId(), favoriteProductEntity);
                        }

                        favoriteProductEntitiesLoading = false;
                        sortDataWhenLoaded();
                    }

                    @Override
                    public void onDataUnavailable() {
                        favoriteProductEntitiesLoading = false;
                        favoriteProductListCallback.onDataNotAvailable();
                        sortDataWhenLoaded();
                    }
                });
    }

    private void sortDataWhenLoaded() {
        if (!productEntitiesLoading && !favoriteProductEntitiesLoading) {
            sortAndReturnData();
        }
    }

    private void sortAndReturnData() {
        List<ProductModel> productModels = new ArrayList<>();
        List<ProductModel> favoriteProductModels = new ArrayList<>();

        for (Map.Entry<String, ProductEntity> productEntity : productsMap.entrySet()) {
            ProductModel productModel = new ProductModel();
            productModel.setProductEntity(productEntity.getValue());

            if (favoriteProductsMap.containsKey(productEntity.getValue().getDataId())) {
                productModel.setFavorite(true);
                productModel.setFavoriteProductEntity(
                        favoriteProductsMap.get(productEntity.getValue().getDataId()));
                favoriteProductModels.add(productModel);
            } else
                productModel.setFavorite(false);

            productModels.add(productModel);
        }

        favoriteProductListCallback.onAllLoaded(favoriteProductModels);
        productListCallback.onAllLoaded(productModels);

    }

    @Override
    public void removeFavoriteProduct(String favoriteProductId) {
        favoriteProductEntityDataSource.deleteByDataId(favoriteProductId);
    }
}