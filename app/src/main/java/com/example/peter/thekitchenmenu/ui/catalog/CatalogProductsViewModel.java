package com.example.peter.thekitchenmenu.ui.catalog;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.FavoriteProductModel;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsDataSource;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsRepository;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.ProductViewerActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CatalogProductsViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-CatalogProductsVM";

    private ProductRepository repositoryProduct;
    private FavoriteProductsRepository repositoryFavoriteProducts;
    private ProductNavigator navigator;

    private boolean productEntitiesLoading;
    private boolean favoriteProductEntitiesLoading;
    private ObservableBoolean dataLoading = new ObservableBoolean(false);
    private ObservableBoolean isDataLoadingError = new ObservableBoolean(false);

    private LinkedHashMap<String, ProductEntity> productMap = new LinkedHashMap<>();
    private LinkedHashMap<String, FavoriteProductEntity> favoriteProductMap = new LinkedHashMap<>();
    private MutableLiveData<List<ProductModel>> productModels = new MutableLiveData<>();
    private MutableLiveData<List<FavoriteProductModel>> favoriteProducts = new MutableLiveData<>();

    private SingleLiveEvent<String> openProductEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> addToFavoritesEvent = new SingleLiveEvent<>();

    private MutableLiveData<String> userId;
    private MutableLiveData<Boolean> isCreator = new MutableLiveData<>();

    private List<FavoriteProductModel> favoriteProductModels = new ArrayList<>();

    // The item selected in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<ProductEntity> selectedProduct = new MutableLiveData<>();

    public CatalogProductsViewModel(Application application,
                                    FavoriteProductsRepository repositoryFavoriteProducts,
                                    ProductRepository repositoryProduct) {
        super(application);
        setupObservables();
        this.repositoryFavoriteProducts = repositoryFavoriteProducts;
        this.repositoryProduct = repositoryProduct;
    }

    private void setupObservables() {
        // Retrieve constants
        userId = Constants.getUserId();
    }

    void setNavigator(ProductNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks
        navigator = null;
    }

    void addNewProduct() {
        if (navigator != null) navigator.addNewProduct();
    }

    void prepareData() {
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
     * @param forceUpdate   Pass in true to refresh the data in the {@link ProductDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadProductEntities(boolean forceUpdate, final boolean showLoadingUI) {
        productEntitiesLoading = true;
        if (showLoadingUI) dataLoading.set(true);
        if (forceUpdate) repositoryProduct.refreshProducts();

        repositoryProduct.getProducts(new ProductDataSource.LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<ProductEntity> productEntities) {

                if (showLoadingUI) dataLoading.set(false);
                isDataLoadingError.set(false);
                productEntitiesLoading = false;

                if (productMap == null) productMap = new LinkedHashMap<>();
                for (ProductEntity productEntity : productEntities) {
                    productMap.put(productEntity.getId(), productEntity);
                }
                prepareModels();
            }

            @Override
            public void onDataNotAvailable() {
                // TODO - Show empty screen
                isDataLoadingError.set(true);
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
        favoriteProductEntitiesLoading = true;
        if (showLoadingUi) dataLoading.set(true);

        if (forceUpdate) {
            repositoryProduct.refreshProducts();
            repositoryFavoriteProducts.refreshFavoriteProducts();
        }

        repositoryFavoriteProducts.getFavoriteProducts(
                new FavoriteProductsDataSource.LoadFavoriteProductsCallback() {
                    @Override
                    public void onFavoriteProductsLoaded(
                            List<FavoriteProductEntity> favoriteProductEntities) {

                        if (showLoadingUi) dataLoading.set(false);
                        isDataLoadingError.set(false);
                        favoriteProductEntitiesLoading = false;

                        if (favoriteProductMap == null) favoriteProductMap = new LinkedHashMap<>();
                        for (FavoriteProductEntity favoriteProductEntity : favoriteProductEntities) {
                            favoriteProductMap.put(
                                    favoriteProductEntity.getProductId(), favoriteProductEntity);
                        }
                        prepareModels();
                    }

                    @Override
                    public void onDataNotAvailable() {
                        // TODO - Show empty screen
                        isDataLoadingError.set(true);
                    }
                });
    }

    private void prepareModels() {
        if (!productEntitiesLoading && !favoriteProductEntitiesLoading) {
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
            } else productModel.setFavorite(false);

            productModelList.add(productModel);

        }
        productModels.setValue(productModelList);
    }

    MutableLiveData<List<ProductModel>> getProductModels() {
        return productModels;
    }

    private void prepareFavoriteProductModels() {
        Log.d(TAG, "prepareFavoriteProductModels: called");
        List<FavoriteProductModel> favoriteProductModelList = new ArrayList<>();

        for (Map.Entry<String, FavoriteProductEntity> favoriteProductEntityEntry :
                favoriteProductMap.entrySet()) {
            FavoriteProductModel favoriteProductModel = new FavoriteProductModel();

            if (productMap.containsKey(favoriteProductEntityEntry.getKey())) {
                favoriteProductModel.setFavoriteProduct(favoriteProductEntityEntry.getValue());
                favoriteProductModel.setProduct(productMap.get(
                        favoriteProductEntityEntry.getKey()));

            } else favoriteProductMap.remove(favoriteProductEntityEntry.getKey());
            favoriteProductModelList.add(favoriteProductModel);
        }
        favoriteProducts.setValue(favoriteProductModelList);
    }

    MutableLiveData<List<FavoriteProductModel>> getFavoriteProducts() {
        return favoriteProducts;
    }

    // Pushes changes to the user ID to observers.
    public MutableLiveData<String> getUserId() {
        return userId;
    }

    // Boolean that tells us if this user created the product.
    MutableLiveData<Boolean> getIsCreator() {
        return isCreator;
    }

    // Pushes the selected product to observers.
    LiveData<ProductEntity> getSelected() {
        return selectedProduct;
    }

    SingleLiveEvent<String> getOpenProductEvent() {
        return openProductEvent;
    }

    SingleLiveEvent<String> getAddToFavoritesEvent() {
        return addToFavoritesEvent;
    }

    void removeFromFavorites(String productId) {
        repositoryFavoriteProducts.getFavoriteProductByProductId(productId,
                new FavoriteProductsDataSource.GetFavoriteProductCallback() {
            @Override
            public void onFavoriteProductLoaded(FavoriteProductEntity favoriteProduct) {
                repositoryFavoriteProducts.deleteFavoriteProduct(favoriteProduct.getId());
                prepareData();
            }

            @Override
            public void onDataNotAvailable() {}});
    }

    void handleActivityResult(int requestCode, int resultCode) {
        if (ProductViewerActivity.REQUEST_CODE == requestCode) {

            switch (resultCode) {
                case ProductViewerActivity.DELETE_RESULT_OK:
                    prepareData();

                case FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK:
                    prepareData();
            }
        }
    }
}