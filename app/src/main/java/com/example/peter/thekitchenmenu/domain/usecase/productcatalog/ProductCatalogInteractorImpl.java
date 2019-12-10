package com.example.peter.thekitchenmenu.domain.usecase.productcatalog;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryFavoriteProduct;
import com.example.peter.thekitchenmenu.data.repository.RepositoryProduct;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductCatalogInteractorImpl implements ProductCatalogInteractor {

    private static final String TAG = "tkm-" + ProductCatalogInteractorImpl.class.getSimpleName() + " ";

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
            @NonNull RepositoryProduct repositoryProduct,
            @NonNull RepositoryFavoriteProduct repositoryFavoriteProduct) {

        this.productEntityDataSource = repositoryProduct;
        this.favoriteProductEntityDataSource = repositoryFavoriteProduct;
    }

    public static ProductCatalogInteractorImpl getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ProductCatalogInteractorImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProductCatalogInteractorImpl(
                            DatabaseInjection.provideProductsDataSource(
                                    application.getApplicationContext()),
                            DatabaseInjection.provideFavoritesProductsDataSource(
                                    application.getApplicationContext()
                            )
                    );
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getProductModelList(@NonNull GetAllCallback productListCallback) {
        this.productListCallback = productListCallback;
        productsMap.clear();
        productEntitiesLoading = true;
        productEntityDataSource.getAll(
                new DataSource.GetAllCallback<ProductEntity>() {
                    @Override
                    public void onAllLoaded(List<ProductEntity> productEntityList) {

                        for (ProductEntity productEntity : productEntityList) {
                            productsMap.put(productEntity.getId(), productEntity);
                        }

                        productEntitiesLoading = false;
                        sortDataWhenLoaded();
                    }

                    @Override
                    public void onDataNotAvailable() {
                        productEntitiesLoading = false;
                        productListCallback.onDataNotAvailable();
                        sortDataWhenLoaded();
                    }
                });
    }

    @Override
    public void getFavoriteProductModelList(@NonNull GetAllCallback favoriteProductListCallback) {
        this.favoriteProductListCallback = favoriteProductListCallback;
        favoriteProductsMap.clear();
        favoriteProductEntitiesLoading = true;
        favoriteProductEntityDataSource.getAll(
                new DataSource.GetAllCallback<FavoriteProductEntity>() {
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
                    public void onDataNotAvailable() {
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

            if (favoriteProductsMap.containsKey(productEntity.getValue().getId())) {
                productModel.setFavorite(true);
                productModel.setFavoriteProductEntity(
                        favoriteProductsMap.get(productEntity.getValue().getId()));
                favoriteProductModels.add(productModel);
            } else
                productModel.setFavorite(false);

            productModels.add(productModel);
        }

        int favoriteCount = 0;
        for (ProductModel pm : productModels)
            if (pm.isFavorite())
                favoriteCount++;

        Log.d(TAG, "sortAndReturnData: favorite count in product models=" + favoriteCount);
        Log.d(TAG, "sortAndReturnData: Number of favorite product models=" + favoriteProductModels.size());

        favoriteProductListCallback.onAllLoaded(favoriteProductModels);
        productListCallback.onAllLoaded(productModels);

    }

    @Override
    public void removeFavoriteProduct(String favoriteProductId) {
        favoriteProductEntityDataSource.deleteById(favoriteProductId);
    }
}