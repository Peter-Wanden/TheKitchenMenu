package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;



import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.annotation.Nonnull;

public class ProductCatalogAllFragment extends Fragment {

    // TODO - make a super class, for AllProducts and MyProducts to inherit.
    private static final String TAG = "tkm-" + ProductCatalogAllFragment.class.getSimpleName() +
            ":";

    private ProductCatalogViewModel viewModel;
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
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        ProductCatalogAllFragmentBinding binding = DataBindingUtil.inflate(
//                inflater,
//                R.layout.product_catalog_all_fragment,
//                container,
//                false);

        viewModel = ProductCatalogActivity.obtainViewModel(requireActivity());
//        binding.setViewModel(viewModel);

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager((requireActivity())
                    .getApplicationContext(), 2);

//            binding.productCatalogAllFragmentRecyclerView.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new LinearLayoutManager(requireActivity()
                    .getApplicationContext(), RecyclerView.VERTICAL, false);

//            binding.productCatalogAllFragmentRecyclerView.setLayoutManager(linearManager);
        }

//        binding.productCatalogAllFragmentRecyclerView.setHasFixedSize(true);
        adapter = new ProductCatalogAllRecyclerAdapter(viewModel);
//        binding.productCatalogAllFragmentRecyclerView.setAdapter(adapter);

//        return binding.getRoot();
        return null;
    }

}
