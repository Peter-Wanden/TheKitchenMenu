package com.example.peter.thekitchenmenu.ui.catalog;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ViewModelCatalogProducts extends AndroidViewModel {

    private static final String TAG = "ViewModelCatalogProducts";

    private ProductNavigator navigator;
    private MediatorLiveData<List<ProductEntity>> products;
    private MutableLiveData<String> userId;
    private MutableLiveData<Boolean> isCreator = new MutableLiveData<>();

    // The item selected in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<ProductEntity> selectedProduct = new MutableLiveData<>();

    public ViewModelCatalogProducts(Application application) {
        super(application);

        setupObservables(application);
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

    private void setupObservables(Application application) {
        Repository repository = ((Singletons) application).getRepository();

        // Data models.
        products = new MediatorLiveData<>();
        products.setValue(null);

        // Request data
        products.addSource(repository.getAllProducts(), products::setValue);

        // Retrieve constants
        userId = Constants.getUserId();
    }
    // Converts data models into view models.

    public MediatorLiveData<List<ProductEntity>> getMergedProductAndUserData() {
        return products;
    }
    // Filters through only the ProductUserDataEntity data in the ProductModel view model (My Products).

    public LiveData<List<ProductEntity>> getProductData() {
        return products;
    }
    // Pushes changes to the user ID to observers.

    public MutableLiveData<String> getUserId() {
        return userId;
    }
    // Boolean that tells us if this user created the product_uneditable being used.

    public MutableLiveData<Boolean> getIsCreator() {
        return isCreator;
    }
    // Triggered by selecting an item in the Fragment's RecyclerView.

    public void selectedItem(ProductEntity product, boolean isCreator) {
        this.isCreator.setValue(isCreator);
        selectedProduct.setValue(product);
    }

    // Pushes the selected product to observers.
    public LiveData<ProductEntity> getSelected() {
        return selectedProduct;
    }
}