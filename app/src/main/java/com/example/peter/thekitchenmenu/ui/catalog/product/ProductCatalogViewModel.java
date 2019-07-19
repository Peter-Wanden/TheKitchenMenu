package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.productviewer.ProductViewerActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProductCatalogViewModel extends AndroidViewModel
        implements ProductNavigator, ProductItemNavigator {

    private static final String TAG = "tkm-CatalogProductsVM";

    private ProductCatalogInteractor productInteractor;
    private ProductNavigator productNavigator;
    private ProductItemNavigator itemNavigator;

    public final ObservableBoolean productDataLoading = new ObservableBoolean(false);
    public final ObservableBoolean favoriteProductDataLoading = new ObservableBoolean(false);
    public final ObservableBoolean isDataLoadingError = new ObservableBoolean(false);

    private final MutableLiveData<List<ProductModel>> productModelList = new MutableLiveData<>();
    private final MutableLiveData<List<ProductModel>> favoriteProductModelList = new MutableLiveData<>();

    private final MutableLiveData<String> searchQueryEvent = new MutableLiveData<>();

    public ProductCatalogViewModel(
            Application application) {
        super(application);
        productInteractor = ProductCatalogInteractorImpl.getInstance(application);
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

    @Override
    public void addNewProduct() {
        if (productNavigator != null)
            productNavigator.addNewProduct();
    }

    void start() {
        loadProductModelList();
        loadFavoriteProductModelList();
    }

    private void loadProductModelList() {
        loadProductModelList(false);
    }

    private void loadProductModelList(boolean forceUpdate) {
        loadProductModelList(forceUpdate, true);
    }

    /**
     * @param forceUpdate   Pass in true to refreshData the data in the {@link }
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadProductModelList(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI)
            productDataLoading.set(true);

//        if (forceUpdate)
//            productEntityDataSource.refreshData();

        productInteractor.getProductModelList(new ProductCatalogInteractor.GetAllCallback() {
            @Override
            public void onAllLoaded(List<ProductModel> modelList) {
                isDataLoadingError.set(false);

                if(showLoadingUI)
                    productDataLoading.set(false);

                productModelList.postValue(modelList);
            }

            @Override
            public void onDataNotAvailable() {
                isDataLoadingError.set(true);
            }
        });
    }

    MutableLiveData<List<ProductModel>> getProductModelList() {
        return productModelList;
    }

    private void loadFavoriteProductModelList() {
        loadFavoriteProductModelList(false);
    }

    private void loadFavoriteProductModelList(boolean forceUpdate) {
        loadFavoriteProductModelList(forceUpdate, true);
    }

    private void loadFavoriteProductModelList(boolean forceUpdate, boolean showLoadingUi) {
        if (showLoadingUi)
            favoriteProductDataLoading.set(true);

//        if (forceUpdate)
//            favoriteProductEntityDataSource.refreshData();

        productInteractor.getFavoriteProductModelList(new ProductCatalogInteractor.GetAllCallback() {
            @Override
            public void onAllLoaded(List<ProductModel> modelList) {
                isDataLoadingError.set(false);

                if (showLoadingUi)
                    favoriteProductDataLoading.set(false);

                favoriteProductModelList.postValue(modelList);
            }

            @Override
            public void onDataNotAvailable() {
                isDataLoadingError.set(true);
            }
        });
    }

    MutableLiveData<List<ProductModel>> getFavoriteProductModelList() {
        return favoriteProductModelList;
    }

    @Override
    public void viewProduct(ProductModel productModel) {
        itemNavigator.viewProduct(productModel);
    }

    @Override
    public void addToFavorites(String productId) {
        itemNavigator.addToFavorites(productId);
    }

    @Override
    public void removeFromFavorites(String favoriteProductId) {
        productInteractor.removeFavoriteProduct(favoriteProductId);
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