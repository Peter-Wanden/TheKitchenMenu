package com.example.peter.thekitchenmenu.ui.catalog;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogProductList;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogProductUsedList;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryProducts;

import java.util.List;
import java.util.Objects;

/**
 * Overrides FragmentCatalog to implement a version that shows all products
 */
public class FragmentCatalogProducts
        extends
        FragmentCatalog {

    private static final String LOG_TAG = FragmentCatalogProducts.class.getSimpleName();

    /* The ViewModel that retrieves the entire catalog of products */
    @Override
    public void onResume() {
        super.onResume();

        ViewModelCatalogProductList catalogProductList =
                ViewModelProviders.of(this).get(ViewModelCatalogProductList.class);

        LiveData<List<Product>> productsLiveData = catalogProductList.getProductsLiveData();

        productsLiveData.observe(this, products -> {
            if (products != null) {
                mCatalogAdapter.setProducts(products);
            }
        });
    }
}
