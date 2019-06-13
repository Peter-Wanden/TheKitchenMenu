package com.example.peter.thekitchenmenu.ui.catalog;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.model.UsedProductDataModel;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.data.repository.UsedProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.UsedProductRepository;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.ProductViewerActivity;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CatalogProductsViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-CatalogProductsVM";

    private ProductRepository repositoryProduct;
    private UsedProductRepository repositoryUsedProduct;
    private ProductNavigator navigator;
    private ObservableBoolean dataLoading = new ObservableBoolean(false);
    private ObservableBoolean isDataLoadingError = new ObservableBoolean(false);

    private MutableLiveData<List<ProductEntity>> products = new MutableLiveData<>();
    private MutableLiveData<List<UsedProductDataModel>> usedProducts = new MutableLiveData<>();

    private SingleLiveEvent<String> openProductEvent = new SingleLiveEvent<>();

    private MutableLiveData<String> userId;
    private MutableLiveData<Boolean> isCreator = new MutableLiveData<>();

    private UsedProductDataModel usedProductDataModel;
    private List<UsedProductDataModel> usedProductDataModels = new ArrayList<>();

    // The item selected in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<ProductEntity> selectedProduct = new MutableLiveData<>();

    public CatalogProductsViewModel(Application application,
                                    UsedProductRepository repositoryUsedProduct,
                                    ProductRepository repositoryProduct) {
        super(application);
        setupObservables();
        this.repositoryUsedProduct = repositoryUsedProduct;
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

    void loadAllProducts() {
        loadAllProducts(false);
    }

    private void loadAllProducts(boolean forceUpdate) {
        loadAllProducts(forceUpdate, true);
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link ProductDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadAllProducts(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) dataLoading.set(true);
        if (forceUpdate) repositoryProduct.refreshProducts();

        repositoryProduct.getProducts(new ProductDataSource.LoadProductsCallback() {

            @Override
            public void onProductsLoaded(List<ProductEntity> productsToShow) {
                if (showLoadingUI) dataLoading.set(false);
                isDataLoadingError.set(false);
                products.setValue(productsToShow);
            }

            @Override
            public void onDataNotAvailable() {
                isDataLoadingError.set(true);
                Log.d(TAG, "loadAllProducts: onDataNotAvailable: ");
            }
        });
    }

    public MutableLiveData<List<ProductEntity>> getProducts() {
        return products;
    }

    void loadUsedProducts() {
        loadUsedProducts(false);
    }

    private void loadUsedProducts(boolean forceUpdate) {
        loadUsedProducts(forceUpdate, false);
    }

    private void loadUsedProducts(boolean forceUpdate, boolean showLoadingUi) {
        usedProductDataModels.clear();
        if (showLoadingUi) dataLoading.set(true);

        if (forceUpdate) {
            repositoryProduct.refreshProducts();
            repositoryUsedProduct.refreshUsedProducts();
        }

        repositoryUsedProduct.getUsedProducts(new UsedProductDataSource.LoadUsedProductsCallback() {
            @Override
            public void onUsedProductsLoaded(List<UsedProductEntity> usedProducts) {
                int count = 0;
                int listSize = usedProducts.size();

                for (UsedProductEntity usedProduct : usedProducts) {
                    count++;
                    int finalCount = count;
                    // Add the associated ProductEntity to the UsedProductEntity
                    repositoryProduct.getProduct(usedProduct.getProductId(),
                            new ProductDataSource.GetProductCallback() {

                                @Override
                                public void onProductLoaded(ProductEntity product) {

                                    UsedProductDataModel model = new UsedProductDataModel(
                                            usedProduct,
                                            product);

                                    CatalogProductsViewModel.this.
                                            usedProductDataModels.add(model);

                                    if (finalCount == listSize) {
                                        CatalogProductsViewModel.this.usedProducts.setValue(
                                                CatalogProductsViewModel.this.
                                                        usedProductDataModels);
                                    }
                                }

                                @Override
                                public void onDataNotAvailable() {
                                    // There is a used product with no associated product, this is
                                    // not allowed, so delete the used product
                                    repositoryUsedProduct.deleteUsedProduct(usedProduct.getId());
                                }
                            });
                }
            }

            @Override
            public void onDataNotAvailable() {
                // TODO - Show empty screen
                Log.d(TAG, "load used products: onDataNotAvailable: no used products available!");
            }
        });
    }

    MutableLiveData<List<UsedProductDataModel>> getUsedProducts() {
        return usedProducts;
    }

    // Pushes changes to the user ID to observers.
    public MutableLiveData<String> getUserId() {
        return userId;
    }

    // Boolean that tells us if this user created the product_viewer_identity being used.
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

    void handleActivityResult(int requestCode, int resultCode) {
        if (ProductViewerActivity.REQUEST_CODE == requestCode) {
            switch (resultCode) {
                case ProductViewerActivity.DELETE_RESULT_OK:
                    loadUsedProducts();
                    Log.d(TAG, "handleActivityResult: force update on used products");
            }
        }
    }
}