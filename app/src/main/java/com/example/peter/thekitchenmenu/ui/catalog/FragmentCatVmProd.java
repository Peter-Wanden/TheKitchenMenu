package com.example.peter.thekitchenmenu.ui.catalog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import static com.example.peter.thekitchenmenu.app.Constants.ANONYMOUS;

import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.model.VmProd;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatProd;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Displays the {@link DmProdComm} list.
 */
public class FragmentCatVmProd
        extends Fragment
        implements OnClickVmProd {

    // TODO - make a super class, for FragmentCommunityProducts and FragmentMyProducts to inherit.
    private static final String TAG = "FragmentCatVmProd";

    private AdapterCatProdComm mAdapterCatProd;
    private ViewModelCatProd mViewModelProdCommMy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapterCatProd = new AdapterCatProdComm(getActivity(), this);

        mViewModelProdCommMy = ViewModelProviders.of(getActivity()).get(ViewModelCatProd.class);
        // Observes changes to the DmProdComm data and passes them to the adapter
        mViewModelProdCommMy.getMatchVmProds().observe(
                this, vmListProd -> mAdapterCatProd.setProducts(vmListProd));

        // Observes changes to the UserId state and passes them to the adaptor.
        final Observer<String> userIdObserver = userId
                -> {
            if (userId !=null && !userId.equals(ANONYMOUS)) {
                mAdapterCatProd.setUserId(userId);
            }
        };
        mViewModelProdCommMy.getUserId().observe(this, userIdObserver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentCatalogProductsBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalog_products, container, false);


        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager((getActivity())
                            .getApplicationContext(), columnCalculator());

            binding.fragmentCatalogProductsRv.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new LinearLayoutManager(getActivity()
                    .getApplicationContext(), RecyclerView.VERTICAL, false);

            binding.fragmentCatalogProductsRv.setLayoutManager(linearManager);
        }

        binding.fragmentCatalogProductsRv.setHasFixedSize(true);
        binding.fragmentCatalogProductsRv.setAdapter(mAdapterCatProd);

        return binding.getRoot();
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
    public void onClick(VmProd clickedVmProd, boolean isCreator) {
        mViewModelProdCommMy.selectedItem(clickedVmProd, isCreator);
    }
}
