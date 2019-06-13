package com.example.peter.thekitchenmenu.ui.catalog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;

import com.example.peter.thekitchenmenu.data.model.UsedProductDataModel;
import com.example.peter.thekitchenmenu.databinding.ProductCatalogUsedFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogUsedFragment extends Fragment {

    private static final String TAG = "tkm-CatalogUsedFragment";

    private CatalogProductsViewModel viewModel;
    private ProductCatalogUsedFragmentBinding binding;
    private CatalogUsedRecyclerAdapter adapter;

    public CatalogUsedFragment(){}
    public CatalogUsedFragment newInstance() {return new CatalogUsedFragment();}

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");

        viewModel.getUsedProducts().observe(requireActivity(), usedProductsDataModels -> {
            if (usedProductsDataModels != null) {
                adapter.setUsedProductDataModels(usedProductsDataModels);

                for (UsedProductDataModel model : usedProductsDataModels) {
                    Log.d(TAG, "onResume: Looping through used products: " + model.getProduct().getDescription());
                }
            }
        });
        viewModel.loadUsedProducts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_catalog_used_fragment,
                container,
                false);

        viewModel = CatalogActivity.obtainViewModel(requireActivity());
        binding.setViewModel(viewModel);

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager(requireActivity().
                    getApplicationContext(), columnCalculator());

            binding.fragmentCatalogProductsRv.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new
                    LinearLayoutManager(requireActivity().getApplicationContext(),
                    RecyclerView.VERTICAL, false);

            binding.fragmentCatalogProductsRv.setLayoutManager(linearManager);
        }

        binding.fragmentCatalogProductsRv.setHasFixedSize(true);
        adapter = new CatalogUsedRecyclerAdapter(viewModel);
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
