package com.example.peter.thekitchenmenu.ui.catalog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.databinding.ProductCatalogAllFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogAllFragment extends Fragment {

    // TODO - make a super class, for FragmentCommunityProducts and FragmentMyProducts to inherit.
    private static final String TAG = "tkm-CatalogAllFragment";

    private CatalogProductsViewModel viewModel;
    private ProductCatalogAllFragmentBinding binding;
    private CatalogRecyclerAdapter adapter;

    public CatalogAllFragment(){}
    public static CatalogAllFragment newInstance() {
        return new CatalogAllFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getProducts().observe(requireActivity(), products -> {
            if (products != null) {
                adapter.setProducts(products);

                for (ProductEntity product : products) {
                    Log.d(TAG, "onResumeAll: description=" + product.getDescription());
                }
            }
        });
        viewModel.loadAllProducts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_catalog_all_fragment,
                container,
                false);

        viewModel = CatalogActivity.obtainViewModel(requireActivity());
        binding.setViewModel(viewModel);


        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager((requireActivity())
                            .getApplicationContext(), columnCalculator());

            binding.fragmentCatalogProductsRv.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new LinearLayoutManager(requireActivity()
                    .getApplicationContext(), RecyclerView.VERTICAL, false);

            binding.fragmentCatalogProductsRv.setLayoutManager(linearManager);
        }

        binding.fragmentCatalogProductsRv.setHasFixedSize(true);
        adapter = new CatalogRecyclerAdapter(requireActivity(), viewModel);
        binding.fragmentCatalogProductsRv.setAdapter(adapter);

        return binding.getRoot();
    }

    /**
     * Screen width column calculator
     */
    private int columnCalculator() {

        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Width of smallest tablet
        int divider = 600;
        int width = metrics.widthPixels;
        int columns = width / divider;
        if (columns < 2) return 2;

        return columns;
    }
}
