package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
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

    private static final String TAG = "tkm-ProductCatalogVM";

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
        productNavigator = null;
        itemNavigator = null;
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

                if (showLoadingUI)
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
    public void addNewProduct() {
        if (productNavigator != null)
            productNavigator.addNewProduct();
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

        if (requestCode == ProductViewerActivity.REQUEST_VIEW_PRODUCT &&
                resultCode == ProductViewerActivity.RESULT_DATA_HAS_CHANGED)
            start();

        if (requestCode == FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT &&
                resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK)
            start();

        if (ProductEditorActivity.REQUEST_ADD_EDIT_PRODUCT == requestCode &&
                resultCode == ProductViewerActivity.RESULT_DATA_HAS_CHANGED)
            start();
    }

    void searchQuery(String query) {
        searchQueryEvent.setValue(query);
    }

    MutableLiveData<String> getSearchQueryEvent() {
        return searchQueryEvent;
    }
}


















