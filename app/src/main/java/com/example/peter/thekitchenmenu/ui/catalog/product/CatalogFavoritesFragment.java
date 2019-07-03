package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;

import com.example.peter.thekitchenmenu.databinding.ProductCatalogFavoritesFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogFavoritesFragment extends Fragment {

    private static final String TAG = "tkm-CatalogFavFrag";

    private ProductCatalogViewModel viewModel;
    private ProductCatalogFavoritesFragmentBinding binding;
    private CatalogFavoritesRecyclerAdapter adapter;

    public CatalogFavoritesFragment(){}
    public CatalogFavoritesFragment newInstance() {return new CatalogFavoritesFragment();}

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getFavoriteProducts().observe(requireActivity(), favoriteProducts -> {
            if (favoriteProducts != null) {
                adapter.setFavoriteProductModels(favoriteProducts);
            }
        });

        viewModel.getSearchQueryEvent().observe(requireActivity(), searchQuery -> {
            Log.d(TAG, "onResume: searchQuery=" + searchQuery);
            adapter.getFilter().filter(searchQuery);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_catalog_favorites_fragment,
                container,
                false);

        viewModel = ProductCatalogActivity.obtainViewModel(requireActivity());
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
        adapter = new CatalogFavoritesRecyclerAdapter(viewModel);
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
