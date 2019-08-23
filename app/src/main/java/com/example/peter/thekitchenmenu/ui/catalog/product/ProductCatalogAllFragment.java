package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;

import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.databinding.ProductCatalogAllFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductCatalogAllFragment extends Fragment {

    // TODO - make a super class, for AllProducts and MyProducts to inherit.
    private static final String TAG = "tkm-ProductCatAllFrag";

    private ProductCatalogViewModel viewModel;
    private ProductCatalogAllFragmentBinding binding;
    private ProductCatalogAllRecyclerAdapter adapter;

    public ProductCatalogAllFragment() {
    }

    public static ProductCatalogAllFragment newInstance() {
        return new ProductCatalogAllFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getProductModelList().observe(requireActivity(), productModelList -> {
            if (productModelList != null) {
                adapter.setProductModels(productModelList);
            }
        });

        viewModel.getSearchQueryEvent().observe(requireActivity(), searchQuery ->
                adapter.getFilter().filter(searchQuery));
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

        viewModel = ProductCatalogActivity.obtainViewModel(requireActivity());
        binding.setViewModel(viewModel);

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager((requireActivity())
                    .getApplicationContext(), 2);

            binding.productCatalogAllFragmentRecyclerView.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new LinearLayoutManager(requireActivity()
                    .getApplicationContext(), RecyclerView.VERTICAL, false);

            binding.productCatalogAllFragmentRecyclerView.setLayoutManager(linearManager);
        }

        binding.productCatalogAllFragmentRecyclerView.setHasFixedSize(true);
        adapter = new ProductCatalogAllRecyclerAdapter(viewModel);
        binding.productCatalogAllFragmentRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}
