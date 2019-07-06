package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.FavoriteProductModel;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsDataSource;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsRepository;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteeditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.editor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.ProductViewerActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProductCatalogViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-CatalogProductsVM";

    private ProductRepository repositoryProduct;
    private FavoriteProductsRepository repositoryFavoriteProducts;
    private ProductNavigator productNavigator;
    private ProductItemNavigator itemNavigator;

    private boolean productsLoading;
    private boolean favoriteProductsLoading;
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);
    public final ObservableBoolean isDataLoadingError = new ObservableBoolean(false);

    private LinkedHashMap<String, ProductEntity> productMap = new LinkedHashMap<>();
    private LinkedHashMap<String, FavoriteProductEntity> favoriteProductMap = new LinkedHashMap<>();
    private final MutableLiveData<List<ProductModel>> productModels = new MutableLiveData<>();
    private final MutableLiveData<List<FavoriteProductModel>> favoriteProducts = new MutableLiveData<>();

    private final SingleLiveEvent<String> openProductEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> addToFavoritesEvent = new SingleLiveEvent<>();
    private final MutableLiveData<String> searchQueryEvent = new MutableLiveData<>();

    public ProductCatalogViewModel(Application application,
                                   FavoriteProductsRepository repositoryFavoriteProducts,
                                   ProductRepository repositoryProduct) {
        super(application);
        this.repositoryFavoriteProducts = repositoryFavoriteProducts;
        this.repositoryProduct = repositoryProduct;
    }

    void setNavigators(ProductNavigator productNavigator, ProductItemNavigator itemNavigator) {
        this.productNavigator = productNavigator;
        this.itemNavigator = itemNavigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks
        productNavigator = null;
        itemNavigator = null;
    }

    void addProduct() {
        if (productNavigator != null)
            productNavigator.addNewProduct();
    }

    void start() {
        loadProductEntities();
        loadFavoriteProductEntities();
    }

    private void loadProductEntities() {
        loadProductEntities(false);
    }

    private void loadProductEntities(boolean forceUpdate) {
        loadProductEntities(forceUpdate, true);
    }

    /**
     * @param forceUpdate   Pass in true to refreshData the data in the {@link ProductDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadProductEntities(boolean forceUpdate, final boolean showLoadingUI) {
        productMap.clear();
        productsLoading = true;

        if (showLoadingUI)
            dataLoading.set(true);

        if (forceUpdate)
            repositoryProduct.refreshProducts();

        repositoryProduct.getProducts(new ProductDataSource.LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<ProductEntity> productEntities) {

                if (showLoadingUI)
                    dataLoading.set(false);

                isDataLoadingError.set(false);
                productsLoading = false;

                if (productMap == null)
                    productMap = new LinkedHashMap<>();
                else
                    productMap.clear();

                for (ProductEntity productEntity : productEntities) {
                    productMap.put(productEntity.getId(), productEntity);
                }
                prepareModels();
            }

            @Override
            public void onDataNotAvailable() {
                // TODO - Show empty screen
                isDataLoadingError.set(true);
                productsLoading = false;
                prepareModels();
            }
        });
    }

    private void loadFavoriteProductEntities() {
        loadFavoriteProductEntities(false);
    }

    private void loadFavoriteProductEntities(boolean forceUpdate) {
        loadFavoriteProductEntities(forceUpdate, false);
    }

    private void loadFavoriteProductEntities(boolean forceUpdate, boolean showLoadingUi) {
        favoriteProductMap.clear();
        favoriteProductsLoading = true;
        if (showLoadingUi)
            dataLoading.set(true);

        if (forceUpdate) {
            repositoryProduct.refreshProducts();
            repositoryFavoriteProducts.refreshData();
        }

        repositoryFavoriteProducts.getAll(
                new FavoriteProductsDataSource.LoadAllCallback<FavoriteProductEntity>() {
                    @Override
                    public void onAllLoaded(List<FavoriteProductEntity> favoriteProductEntities) {

                        if (showLoadingUi) dataLoading.set(false);
                        isDataLoadingError.set(false);
                        favoriteProductsLoading = false;

                        if (favoriteProductMap == null)
                            favoriteProductMap = new LinkedHashMap<>();

                        for (FavoriteProductEntity favoriteProductEntity :
                                favoriteProductEntities) {
                            favoriteProductMap.put(
                                    favoriteProductEntity.getProductId(),
                                    favoriteProductEntity);
                        }
                        prepareModels();
                    }

            @Override
                    public void onDataNotAvailable() {
                        // TODO - Show empty screen
                        isDataLoadingError.set(true);
                        favoriteProductsLoading = false;
                        prepareModels();
                    }
                });
    }

    private void prepareModels() {
        if (!productsLoading && !favoriteProductsLoading) {
            prepareProductModels();
            prepareFavoriteProductModels();
        }
    }

    private void prepareProductModels() {
        List<ProductModel> productModelList = new ArrayList<>();
        for (Map.Entry<String, ProductEntity> productEntityEntry : productMap.entrySet()) {
            ProductModel productModel = new ProductModel();
            productModel.setProduct(productEntityEntry.getValue());

            if (favoriteProductMap.containsKey(productEntityEntry.getValue().getId())) {
                productModel.setFavorite(true);
                productModel.setFavoriteProductId(
                        favoriteProductMap.get(productEntityEntry.getValue().getId()).getId());

            } else productModel.setFavorite(false);

            productModelList.add(productModel);

        }
        productModels.setValue(productModelList);
    }

    private void prepareFavoriteProductModels() {
        List<FavoriteProductModel> favoriteProductModelList = new ArrayList<>();

        for (Map.Entry<String, FavoriteProductEntity> favoriteProductEntityEntry :
                favoriteProductMap.entrySet()) {
            FavoriteProductModel favoriteProductModel = new FavoriteProductModel();

            if (productMap.containsKey(favoriteProductEntityEntry.getKey())) {
                favoriteProductModel.setFavoriteProduct(favoriteProductEntityEntry.getValue());
                favoriteProductModel.setProduct(productMap.get(
                        favoriteProductEntityEntry.getKey()));

            }
            favoriteProductModelList.add(favoriteProductModel);
        }
        favoriteProducts.setValue(favoriteProductModelList);
    }

    MutableLiveData<List<ProductModel>> getProductModels() {
        return productModels;
    }

    MutableLiveData<List<FavoriteProductModel>> getFavoriteProducts() {
        return favoriteProducts;
    }

    SingleLiveEvent<String> getOpenProductEvent() {
        return openProductEvent;
    }

    SingleLiveEvent<String> getAddToFavoritesEvent() {
        return addToFavoritesEvent;
    }

    void removeFromFavorites(String favoriteProductId) {
        repositoryFavoriteProducts.deleteById(favoriteProductId);
        start();
    }

    void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "handleActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);

        if (ProductViewerActivity.REQUEST_VIEW_PRODUCT == requestCode) {
            if (resultCode == ProductEditorActivity.RESULT_ADD_EDIT_PRODUCT_OK) {
                start();
            } else if (resultCode == ProductViewerActivity.RESULT_DELETE_PRODUCT_OK) {
                start();
            } else if (resultCode == ProductViewerActivity.RESULT_FAVORITE_ADDED_OK) {
                start();
            }
        }

        if (ProductViewerActivity.REQUEST_REVIEW_PRODUCT == requestCode) {

            if (ProductViewerActivity.RESULT_FAVORITE_NOT_ADDED == resultCode) {
                start();
            } else if (ProductViewerActivity.RESULT_FAVORITE_ADDED_OK == resultCode) {
                start();
            }
        }

        if (FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT == requestCode) {
            if (resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK) {
                start();
            }
        }
    }

    void searchQuery(String query) {
        searchQueryEvent.setValue(query);
    }

    MutableLiveData<String> getSearchQueryEvent() {
        return searchQueryEvent;
    }


}