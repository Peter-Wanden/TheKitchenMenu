package com.example.peter.thekitchenmenu.ui.catalog;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class CatalogProductsViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-VM-CatalogProducts";

    private ProductRepository repository;
    private ProductNavigator navigator;
    private ObservableBoolean dataLoading = new ObservableBoolean(false);
    private ObservableBoolean isDataLoadingError = new ObservableBoolean(false);
    private MediatorLiveData<List<ProductEntity>> products;
    private MediatorLiveData<List<ProductUserDataEntity>> usersProducts;
    private MutableLiveData<String> userId;
    private MutableLiveData<Boolean> isCreator = new MutableLiveData<>();

    // The item selected in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<ProductEntity> selectedProduct = new MutableLiveData<>();

    public CatalogProductsViewModel(Application application, ProductRepository repository) {
        super(application);
        setupObservables();
        this.repository = repository;
    }

    private void setupObservables() {
        // Data models.
        products = new MediatorLiveData<>();
        products.setValue(null);

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
        loadProducts(false);
    }

    void loadUsedProducts() {

    }

    private void loadProducts(boolean forceUpdate) {
        loadProducts(forceUpdate, true);
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link ProductDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadProducts (boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) dataLoading.set(true);
        if (forceUpdate) repository.refreshProducts();

        repository.getProducts(new ProductDataSource.LoadProductsCallback() {

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

    public LiveData<List<ProductEntity>> getProducts() {
        return products;
    }

    // Pushes changes to the user ID to observers.
    public MutableLiveData<String> getUserId() {
        return userId;
    }

    // Boolean that tells us if this user created the product_uneditable being used.
    MutableLiveData<Boolean> getIsCreator() {
        return isCreator;
    }

    // Triggered by selecting an item in the Fragment's RecyclerView.
    void selectedItem(ProductEntity product, boolean isCreator) {
        this.isCreator.setValue(isCreator);
        selectedProduct.setValue(product);
    }

    // Pushes the selected product to observers.
    LiveData<ProductEntity> getSelected() {
        return selectedProduct;
    }
}