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

import com.example.peter.thekitchenmenu.data.model.DmProdComm;
import com.example.peter.thekitchenmenu.data.model.VmProd;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelMyCommProd;

import java.util.List;
import java.util.Objects;

public class FragmentCatVmProdMy
        extends Fragment
        implements OnClickVmProd {

    private static final String LOG_TAG = FragmentCatVmProdMy.class.getSimpleName();

    private AdapterCatProdComm mAdapterCatProd;
    private FragmentCatalogProductsBinding mCatProdBinding;
    private Parcelable mLayoutManagerState;
    private ViewModelMyCommProd mViewModelProdCommMy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapterCatProd = new AdapterCatProdComm(getActivity(), this);

        mViewModelProdCommMy = ViewModelProviders.of(getActivity()).get(ViewModelMyCommProd.class);

        // Observes changes to view model ProdMy list and passes them to the adaptor.
        final Observer<List<VmProd>> viewModelProd = vmProds
                -> mAdapterCatProd.setProducts(vmProds);
        mViewModelProdCommMy.getAllVmProdMy().observe(this, viewModelProd);

        // Observes changes to the user ID state and passes them to the adaptor.
        final Observer<String> userIdObserver = newUserId
                -> {
            if (newUserId !=null && !newUserId.equals(ANONYMOUS)) {
                // Turns remote data sync on
                mViewModelProdCommMy.setRemoteSyncEnabled(true);
                mAdapterCatProd.setUserId(newUserId);
            }
        };
        mViewModelProdCommMy.getUserId().observe(this, userIdObserver);
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

            GridLayoutManager gridManager = new
                    GridLayoutManager(getActivity().
                    getApplicationContext(), columnCalculator());

            mCatProdBinding.fragmentCatalogProductsRv.
                    setLayoutManager(gridManager);
        } else {
            LinearLayoutManager linearManager = new
                    LinearLayoutManager(getActivity().getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);

            mCatProdBinding.
                    fragmentCatalogProductsRv.
                    setLayoutManager(linearManager);
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

        mViewModelProdCommMy.setRemoteSyncEnabled(false);

        mLayoutManagerState = mCatProdBinding.fragmentCatalogProductsRv.
                getLayoutManager().
                onSaveInstanceState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("layoutManagerState", mLayoutManagerState);
    }

    @Override
    public void onClick(VmProd vmProd, boolean isCreator) {
        // TODO - update ViewModel to take these values
        mViewModelProdCommMy.selectedItem(vmProd, isCreator);
    }
}
