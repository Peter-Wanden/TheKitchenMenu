package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogMyProducts;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryProducts;

import java.util.List;
import java.util.Objects;

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
    public void setViewModel() {

        // Retrieve the user ID.
        String userId = PreferenceManager.
                getDefaultSharedPreferences(Objects.requireNonNull(
                        getActivity()).
                        getApplicationContext()).
                getString(Constants.USER_ID_KEY, Constants.ANONYMOUS);

        // Check to ensure the user ID has been updated and the fragment is attached to the
        // activity.
        if (!userId.equals(Constants.ANONYMOUS) && getActivity() != null) {

            ViewModelCatalogMyProducts catalogProductMyList =

                    // ViewModelFactoryProducts allows us to pass the user ID as an additional
                    // argument to the default constructor of ViewModelCatalogMyProducts.
                    ViewModelProviders.of(getActivity(), new ViewModelFactoryProducts(
                            userId)).get(ViewModelCatalogMyProducts.class);

            mCatalogAdapter.setUserId(userId);

            LiveData<List<Product>> productsLiveData =
                    catalogProductMyList.getProductsLiveData();

            // Observes the users MyProduct list for changes.
            productsLiveData.observe(getActivity(), products -> {

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

    /* This method is called on configuration change, it ensures setViewModel() is called */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            // Call the ViewModel.
            setViewModel();
        }
    }
}
