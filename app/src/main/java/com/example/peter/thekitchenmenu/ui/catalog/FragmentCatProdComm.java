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
import static com.example.peter.thekitchenmenu.app.Constants.ANONYMOUS;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelProdComm;

import java.util.List;
import java.util.Objects;

/**
 * Displays the {@link ProductCommunity} list.
 */
public class FragmentCatProdComm extends Fragment {

    // TODO - make a super class, for FragmentCommunityProducts and FragmentMyProducts to inherit.
    private static final String LOG_TAG = FragmentCatProdComm.class.getSimpleName();

    private AdapterCatProdComm mAdapterCatProd;
    private Parcelable mLayoutManagerState;
    private ViewModelProdComm mViewModelProdComm;
    private FragmentCatalogProductsBinding mCatProdBinding;

    // TODO - add a click interface

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapterCatProd = new AdapterCatProdComm(getActivity());

        mViewModelProdComm = ViewModelProviders.of(this).get(ViewModelProdComm.class);

        // Observes changes to the ProductCommunity data and passes them to the adaptor
        final Observer<List<ProductCommunity>> prodCommObserver = prodComms
                -> {
            mAdapterCatProd.setProducts(prodComms);
        };
        mViewModelProdComm.getAllProdComms().observe(this, prodCommObserver);

        // Observes changes to the UserId state and passes them to the adaptor.
        final Observer<String> userIdObserver = newUserId
                -> {
            if (newUserId !=null && !newUserId.equals(ANONYMOUS)) {
                // Turns remote data sync on
                mViewModelProdComm.setRemoteSyncEnabled(true);
                mAdapterCatProd.setUserId(newUserId);
            }
        };
        mViewModelProdComm.getUserId().observe(this, userIdObserver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mCatProdBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalog_products, container, false);

        View rootView = mCatProdBinding.getRoot();

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager((getActivity())
                            .getApplicationContext(), columnCalculator());

            mCatProdBinding.fragmentCatalogProductsRv.
                    setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new LinearLayoutManager(getActivity()
                    .getApplicationContext(),LinearLayoutManager.VERTICAL, false);

            mCatProdBinding.fragmentCatalogProductsRv.setLayoutManager(linearManager);
        }

        mCatProdBinding.fragmentCatalogProductsRv.setHasFixedSize(true);
        mCatProdBinding.fragmentCatalogProductsRv.setAdapter(mAdapterCatProd);

        // Post configuration change, restores the state of the previous layout manager to the new
        // layout manager, which could be either a grid or linear layout.
        if (savedInstanceState != null && savedInstanceState.containsKey("layoutManagerState")) {
            mLayoutManagerState = savedInstanceState.getParcelable("layoutManagerState");
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

    @Override
    public void onPause() {
        super.onPause();

        mViewModelProdComm.setRemoteSyncEnabled(false);

        // Get the grid / linear layout manager's state.
        mLayoutManagerState = mCatProdBinding.fragmentCatalogProductsRv.
                getLayoutManager().
                onSaveInstanceState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the grid / linear layout manager's state.
        outState.putParcelable("layoutManagerState", mLayoutManagerState);
    }
}
