package com.example.peter.thekitchenmenu.ui.catalog;

import android.app.Application;

import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CatalogProductsViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-VM-CatalogProducts";

    private ProductRepository repositoryProduct;
    private ProductNavigator navigator;
    private ObservableBoolean dataLoading = new ObservableBoolean(false);
    private ObservableBoolean isDataLoadingError = new ObservableBoolean(false);

    private MutableLiveData<List<ProductEntity>> products = new MutableLiveData<>();
    private MutableLiveData<List<UsedProductEntity>> usedProducts = new MutableLiveData<>();

    private SingleLiveEvent<String> openProductEvent = new SingleLiveEvent<>();

    private MutableLiveData<String> userId;
    private MutableLiveData<Boolean> isCreator = new MutableLiveData<>();

    // The item selected in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<ProductEntity> selectedProduct = new MutableLiveData<>();

    public CatalogProductsViewModel(Application application, ProductRepository repositoryProduct) {
        super(application);
        setupObservables();
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
        if(navigator != null) navigator.addNewProduct();
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
            }
        });
    }

    void loadUsedProducts() {

    }

    public MutableLiveData<List<ProductEntity>> getProducts() {
        return products;
    }

    public MutableLiveData<List<UsedProductEntity>> getUsedProducts() {
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
}