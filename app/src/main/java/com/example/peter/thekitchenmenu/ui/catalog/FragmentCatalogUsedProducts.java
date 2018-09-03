package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogProductUsedList;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryProducts;

import java.util.List;

/**
 * This class is inherited from {@link FragmentCatalog} super class.
 * It depends on the user ID, as it is required as an element in the DatabaseReference
 * used by {@link ViewModelCatalogProductUsedList}.
 */
public class FragmentCatalogUsedProducts
        extends FragmentCatalog {

    private static final String LOG_TAG = FragmentCatalogUsedProducts.class.getSimpleName();

    private String mUserId = Constants.ANONYMOUS;

    /* ViewModel that retrieves the users used product list. */
    public void setViewModel(String userId) {

        mUserId = userId;

        // Check to ensure the user ID is valid.
        if (!mUserId.equals(Constants.ANONYMOUS)) {

            ViewModelCatalogProductUsedList catalogProductUsedList =

                    // ViewModelFactoryProducts allows us to pass the user ID as an additional
                    // argument to the default constructor of ViewModelCatalogProductUsedList.
                    ViewModelProviders.of(this, new ViewModelFactoryProducts(
                            mUserId)).get(ViewModelCatalogProductUsedList.class);

            LiveData<List<Product>> productsLiveData = catalogProductUsedList.getProductsLiveData();

            // Observes the users used product list for changes.
            productsLiveData.observe(this, products -> {
                if (products != null) {
                    mCatalogAdapter.setProducts(products);
                }
            });
        }
    }
}
