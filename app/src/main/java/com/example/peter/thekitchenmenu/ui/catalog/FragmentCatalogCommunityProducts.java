package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailProduct;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogCommunityProductList;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryProducts;

import java.util.List;

/**
 * This class is inherited from {@link FragmentCatalog} super class. This implementations ViewModel
 * lists all community products. In order for the application to function properly it requires the
 * members ID to be set (specifically for a boolean String comparison in
 * {@link AdapterCatalogProduct}. This is achieved by using {@link ViewModelFactoryProducts}.
 */
public class FragmentCatalogCommunityProducts
        extends
        FragmentCatalog {

    private static final String LOG_TAG = FragmentCatalogCommunityProducts.class.getSimpleName();

    /* The ViewModel that retrieves the entire catalog of products */
    public void setViewModel(String userId) {

        // Set the user ID to the adapter
        mCatalogAdapter.setUserId(userId);

        // Check to ensure the user ID is valid.
        if (!userId.equals(Constants.ANONYMOUS)) {

            ViewModelCatalogCommunityProductList catalogProductCommunityList =

                    // ViewModelFactoryProducts allows us to pass the user ID as an additional
                    // argument to the default constructor of ViewModelCatalogCommunityProductList.
                    ViewModelProviders.of(this, new ViewModelFactoryProducts(userId))
                            .get(ViewModelCatalogCommunityProductList.class);

            LiveData<List<Product>> productsLiveData =
                    catalogProductCommunityList.getProductsLiveData();

            productsLiveData.observe(this, products -> {
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
}
