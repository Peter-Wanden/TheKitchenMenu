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

public class ProductCatalogFavoritesFragment extends Fragment {

    private ProductCatalogViewModel viewModel;
    private ProductCatalogFavoritesRecyclerAdapter adapter;

    public ProductCatalogFavoritesFragment(){}

    public static ProductCatalogFavoritesFragment newInstance() {
        return new ProductCatalogFavoritesFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getFavoriteProductModelList().observe(requireActivity(), productModelList -> {
            if (productModelList != null) {
                adapter.setProductModelList(productModelList);
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

//        ProductCatalogFavoritesFragmentBinding binding = DataBindingUtil.inflate(
//                inflater,
//                R.layout.product_catalog_favorites_fragment,
//                container,
//                false);

        viewModel = ProductCatalogActivity.obtainViewModel(requireActivity());
//        binding.setViewModel(viewModel);

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager(requireActivity().
                    getApplicationContext(), 2);

//            binding.productCatalogFavoritesFragmentRecyclerView.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new
                    LinearLayoutManager(requireActivity().getApplicationContext(),
                    RecyclerView.VERTICAL, false);

//            binding.productCatalogFavoritesFragmentRecyclerView.setLayoutManager(linearManager);
        }

//        binding.productCatalogFavoritesFragmentRecyclerView.setHasFixedSize(true);
        adapter = new ProductCatalogFavoritesRecyclerAdapter(viewModel);
//        binding.productCatalogFavoritesFragmentRecyclerView.setAdapter(adapter);

//        return binding.getRoot();
        return null;
    }
}
