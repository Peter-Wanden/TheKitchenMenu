package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogCommunityProductList;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryProducts;

import java.util.List;
import java.util.Objects;

/**
 * This class is inherited from {@link FragmentCatalog} super class. This implementations ViewModel
 * lists all community products. In order for the application to function properly it requires the
 * members ID to be set (specifically for a boolean String comparison in
 * {@link AdapterCatalogProduct}.
 */
public class FragmentCatalogCommunityProducts
        extends
        FragmentCatalog {

    private static final String LOG_TAG = FragmentCatalogCommunityProducts.class.getSimpleName();

    /* The ViewModel that retrieves the entire catalog of products */
    public void setViewModel() {

        // Retrieve the user ID.
        String userId = PreferenceManager.
                getDefaultSharedPreferences(Objects.requireNonNull(
                getActivity()).
                getApplicationContext()).
                getString(Constants.USER_ID_KEY, Constants.ANONYMOUS);

        // Check to ensure the user ID is valid (logged in).
        if (!userId.equals(Constants.ANONYMOUS) && getActivity() != null) {

            ViewModelCatalogCommunityProductList communityProductList =
                    ViewModelProviders.of(getActivity()).
                            get(ViewModelCatalogCommunityProductList.class);

            // Set the user ID to the adapter
            mCatalogAdapter.setUserId(userId);

            LiveData<List<Product>> productsLiveData =
                    communityProductList.getProductsLiveData();

            productsLiveData.observe(getActivity(), products -> {
                if (products != null) {
                    mCatalogAdapter.setProducts(products);
                }
            });
        }
    }

    /* Invokes the onClick method in the host activity */
    @Override
    public void onClick(Product clickedProduct, boolean isCreator) {
        mClickHandler.onClick(clickedProduct, isCreator);
    }

    /* This method is called on configuration change, it ensures setViewModel() is called */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState !=null && getActivity() != null) {

            // Call the ViewModel.
            setViewModel();
        }
    }
}
