package com.example.peter.thekitchenmenu.ui.catalog;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.data.model.Product;

import java.util.Objects;

/**
 * The UI controller that displays the recycler views that list 'Community Products' and
 * 'My Products' within {@link ActivityCatalogProduct} depending on its inherited implementation of
 * {@link FragmentCatalogCommunityProducts} or {@link FragmentCatalogMyProducts}.
 */
public abstract class FragmentCatalog
        extends
        Fragment
        implements
        AdapterCatalogProduct.AdapterCatalogProductClickHandler,
        AdapterFirebaseCatalogProducts.AdapterFirebaseCatalogProductsClickHandler {

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

    public FragmentCatalogOnClickHandler mClickHandler;

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
        mCatalogAdapter = new AdapterCatalogProduct(getActivity(), this);

        mFragmentCatalogProductsBinding.
                fragmentCatalogProductsRv.setAdapter(mCatalogAdapter);

        // Post configuration change, restores the state of the previous layout manager to the new
        // layout manager which could be either a grid or linear layout manager
        if (savedInstanceState != null && savedInstanceState.containsKey("layoutManagerState")) {
            mLayoutManagerState = savedInstanceState.getParcelable("layoutManagerState");
        }

        return rootView;
    }

    /**
     *  Screen width column calculator
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

    /*
    This abstract class is only ever inherited (see Javadoc at the beginning of this class for more
    info). The onClick method is overridden and implemented by its inheritors, therefore this method
    is declared abstract.
    */
    @Override
    public abstract void onClick(Product clickedProduct, boolean isCreator);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Make sure the host activity has implemented the FragmentCatalogOnClickHandler callback
        // interface
        try {
            mClickHandler = (FragmentCatalogOnClickHandler) context;

        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString()
                    + " must implement FragmentCatalogOnClickHandler");
        }
    }

    /* Click interface from fragment to host that implements the onClick() method in host*/
    public interface FragmentCatalogOnClickHandler {
        void onClick(Product clickedProduct, boolean isCreator);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Get the grid / linear layout manager's state.
        mLayoutManagerState = mFragmentCatalogProductsBinding.
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

    /*
    Concrete classes that inherit must implement a ViewModel to provide data to this super
    class
    */
    public abstract void setViewModel();
}
