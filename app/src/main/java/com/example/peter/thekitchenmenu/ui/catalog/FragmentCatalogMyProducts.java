package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelMyProducts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentCatalogMyProducts
        extends
        Fragment {

    private static final String LOG_TAG = FragmentCatalogMyProducts.class.getSimpleName();

    private AdapterCatalogProductCommunity mAdapterCatalogProduct;
    private FragmentCatalogProductsBinding mCatalogProductsBinding;
    private Parcelable mLayoutManagerState;
    private ViewModelMyProducts mViewModelMyProducts;
    private List<ProductCommunity> mProductCommunityList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModelMyProducts = ViewModelProviders.of(this).get(ViewModelMyProducts.class);

        final Observer<List<ProductCommunity>> observer = uCProducts
                -> mAdapterCatalogProduct.setProducts(uCProducts);

        mViewModelMyProducts.getMyCommProducts().observe(this, observer);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mCatalogProductsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalog_products, container, false);

        View rootView = mCatalogProductsBinding.getRoot();

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridLayoutManager = new
                    GridLayoutManager(getActivity().
                    getApplicationContext(), columnCalculator());

            mCatalogProductsBinding.
                    fragmentCatalogProductsRv.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new
                    LinearLayoutManager(getActivity().getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);

            mCatalogProductsBinding.
                    fragmentCatalogProductsRv.
                    setLayoutManager(linearLayoutManager);
        }

        mCatalogProductsBinding.fragmentCatalogProductsRv.setHasFixedSize(true);

        mAdapterCatalogProduct = new AdapterCatalogProductCommunity(getActivity());
        mAdapterCatalogProduct.setProducts(mProductCommunityList);
        mAdapterCatalogProduct.setUserId(mViewModelMyProducts.getUserId());
        mCatalogProductsBinding.fragmentCatalogProductsRv.setAdapter(mAdapterCatalogProduct);

        // Post configuration change, restores the state of the previous layout manager to the new
        // layout manager, which could be either a grid or linear layout.
        if (savedInstanceState != null && savedInstanceState.containsKey("layoutManagerState")) {
            mLayoutManagerState = savedInstanceState.getParcelable("layoutManagerState");
        }

        if (!mViewModelMyProducts.getUserId().equals(Constants.ANONYMOUS)) {
            mAdapterCatalogProduct.setUserId(mViewModelMyProducts.getUserId());
        }

        return rootView;
    }

    /**
     * Screen width column calculator
     */
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

    /**
     * Sets the users ID once the user has signed in.
     * TODO - Set the user Id by using a callback interface
     */
    public void setUserId(String userId) {
        mViewModelMyProducts.setUserId(userId);
    }

    @Override
    public void onPause() {
        super.onPause();

        mLayoutManagerState = mCatalogProductsBinding.fragmentCatalogProductsRv.
                getLayoutManager().
                onSaveInstanceState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("layoutManagerState", mLayoutManagerState);
    }
}
