package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.List;
import java.util.Objects;

/**
 * Houses the recycler views that list 'all products' and 'my products' within
 * ActivityCatalogProducts
 */
public class FragmentCatalog
        extends
        Fragment {

    private static final String LOG_TAG = FragmentCatalog.class.getSimpleName();

    /* Adapter for the product list view */
    public AdapterCatalogProduct mCatalogAdapter;

    /* Binding class for this fragment */
    FragmentCatalogProductsBinding mFragmentCatalogProductsBinding;

    /* Layout Managers */
    GridLayoutManager mGridManager;
    LinearLayoutManager mLinearManager;

    /* Enables the current layout manager to save state in configuration change. */
    Parcelable mLayoutManagerState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mFragmentCatalogProductsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalog_products, container, false);

        View rootView = mFragmentCatalogProductsBinding.getRoot();

        /* Creates RecyclerViews with dynamic widths depending on the display type. */
        // Portrait for phone.
        // Landscape for phone.
        // Portrait for tablet.
        // Landscape for tablet.
        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            mGridManager = new GridLayoutManager(
                    Objects.requireNonNull(getActivity())
                            .getApplicationContext(), columnCalculator());

            mFragmentCatalogProductsBinding.
                    fragmentCatalogProductsRv.
                    setLayoutManager(mGridManager);

        } else {
            mLinearManager = new
                    LinearLayoutManager(Objects.requireNonNull(getActivity())
                    .getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);

            mFragmentCatalogProductsBinding.
                    fragmentCatalogProductsRv.
                    setLayoutManager(mLinearManager);
        }

        mFragmentCatalogProductsBinding.
                fragmentCatalogProductsRv.setHasFixedSize(true);

        /* Create the adapter and pass in this classes context and the listener which is also
        this class, as this class implements the click handler. */
        mCatalogAdapter = new AdapterCatalogProduct(getActivity());

        mFragmentCatalogProductsBinding.
                fragmentCatalogProductsRv.setAdapter(mCatalogAdapter);

        return rootView;
    }

    /* Screen width column calculator */
    private int columnCalculator() {

        DisplayMetrics metrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity())
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);

        // Width of smallest tablet
        int divider = 600;
        int width = metrics.widthPixels;
        int columns = width / divider;
        if (columns < 2) return 2;

        return columns;
    }
}
