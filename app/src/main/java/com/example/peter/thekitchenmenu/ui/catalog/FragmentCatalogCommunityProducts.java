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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCommunityProducts;

import java.util.List;
import java.util.Objects;

/**
 * Displays the {@link ProductCommunity} list.
 */
public class FragmentCatalogCommunityProducts extends Fragment {

    // TODO - make a super class, for FragmentCommunityProducts and FragmentMyProducts to inherit.
    private static final String LOG_TAG = FragmentCatalogCommunityProducts.class.getSimpleName();
    private AdapterCatalogProductCommunity mCatalogAdapter;
    private FragmentCatalogProductsBinding mCatalogProductsBinding;
    /* Enables the current layout manager to save state in configuration change. */
    private Parcelable mLayoutManagerState;
    /* ViewModel for this class */
    private ViewModelCommunityProducts mModelCommunityProducts;

    // TODO - add a click interface
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associates the ViewModel with this UI controller.
        mModelCommunityProducts = ViewModelProviders.of(this)
                .get(ViewModelCommunityProducts.class);

        // Observes the data delivered in the ViewModel
        final Observer<List<ProductCommunity>> communityProductsObserver = productCommunities
                -> mCatalogAdapter.setProducts(productCommunities);

        // Returns a list of CommunityProducts
        mModelCommunityProducts.listAllCommunityProducts().
                observe(this, communityProductsObserver);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mCatalogProductsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalog_products, container, false);

        View rootView = mCatalogProductsBinding.getRoot();

        /* Creates RecyclerViews with dynamic widths depending on the display type. */
        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            /* Layout Managers */
            GridLayoutManager gridManager = new GridLayoutManager(
                    Objects.requireNonNull(getActivity())
                            .getApplicationContext(), columnCalculator());

            mCatalogProductsBinding.
                    fragmentCatalogProductsRv.
                    setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new
                    LinearLayoutManager(Objects.requireNonNull(getActivity())
                    .getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);

            mCatalogProductsBinding.
                    fragmentCatalogProductsRv.
                    setLayoutManager(linearManager);
        }

        mCatalogProductsBinding.
                fragmentCatalogProductsRv.setHasFixedSize(true);

        mCatalogAdapter = new AdapterCatalogProductCommunity(getActivity());

        mCatalogProductsBinding.
                fragmentCatalogProductsRv.setAdapter(mCatalogAdapter);

        // Post configuration change, restores the state of the previous layout manager to the new
        // layout manager, which could be either a grid or linear layout.
        if (savedInstanceState != null && savedInstanceState.containsKey("layoutManagerState")) {
            mLayoutManagerState = savedInstanceState.getParcelable("layoutManagerState");
        }

        // Restores the user ID to the adapter
        if (!mModelCommunityProducts.getUserUid().equals(Constants.ANONYMOUS)) {
            mCatalogAdapter.setUserId(mModelCommunityProducts.getUserUid());
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

    /* Informs the fragment that the user is signed in to the remote database */
    public void setUserId(String userUid) {

        Log.e(LOG_TAG, "User is logged in!");

        mModelCommunityProducts.setUserId(userUid);
        mCatalogAdapter.setUserId(userUid);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Get the grid / linear layout manager's state.
        mLayoutManagerState = mCatalogProductsBinding.
                fragmentCatalogProductsRv
                .getLayoutManager()
                .onSaveInstanceState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the grid / linear layout manager's state.
        outState.putParcelable("layoutManagerState", mLayoutManagerState);
    }
}
