package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogMyProducts;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryProducts;

import java.util.List;

/**
 * This class is inherited from {@link FragmentCatalog} super class. This implementations ViewModel
 * shows a list of the current members products. In order to do so it is dependant on receiving the
 * members user ID, as it is required as an element in the DatabaseReference used by
 * {@link ViewModelCatalogMyProducts}. This is achieved by using
 * {@link ViewModelFactoryProducts}
 */
public class FragmentCatalogMyProducts
        extends
        FragmentCatalog {

    private static final String LOG_TAG = FragmentCatalogMyProducts.class.getSimpleName();

    /* ViewModel that retrieves the users used product list. */
    public void setViewModel(String userId) {

        // Set the user ID to the adapter
        mCatalogAdapter.setUserId(userId);

        // Check to ensure the user ID is valid.
        if (!userId.equals(Constants.ANONYMOUS)) {

            ViewModelCatalogMyProducts catalogProductMyList =

                    // ViewModelFactoryProducts allows us to pass the user ID as an additional
                    // argument to the default constructor of ViewModelCatalogMyProducts.
                    ViewModelProviders.of(this, new ViewModelFactoryProducts(
                            userId)).get(ViewModelCatalogMyProducts.class);

            LiveData<List<Product>> productsLiveData =
                    catalogProductMyList.getProductsLiveData();

            // Observes the users used product list for changes.
            productsLiveData.observe(this, products -> {
                if (products != null) {
                    mCatalogAdapter.setProducts(products);
                }
            });
        }
    }

    @Override
    public void onClick(Product clickedProduct, boolean isCreator) {
        mClickHandler.onClick(clickedProduct, isCreator);
    }
}
