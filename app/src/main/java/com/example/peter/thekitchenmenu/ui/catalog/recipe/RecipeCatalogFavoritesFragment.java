package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.RecipeCatalogFavoritesFragmentBinding;

import javax.annotation.Nonnull;

public class RecipeCatalogFavoritesFragment extends Fragment {

    private RecipeCatalogViewModel viewModel;
    private RecipeCatalogFavoritesRecyclerAdapter adapter;

    public RecipeCatalogFavoritesFragment(){}

    public static RecipeCatalogFavoritesFragment newInstance() {
        return new RecipeCatalogFavoritesFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        // todo - set observers
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        RecipeCatalogFavoritesFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_catalog_favorites_fragment,
                container,
                false);

        viewModel = RecipeCatalogActivity.obtainViewModel(requireActivity());
        binding.setViewModel(viewModel);

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager(requireActivity().
                    getApplicationContext(), 2);

            binding.recipeCatalogFavoritesFragmentRecyclerView.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new
                    LinearLayoutManager(requireActivity().getApplicationContext(),
                    RecyclerView.VERTICAL, false);

            binding.recipeCatalogFavoritesFragmentRecyclerView.setLayoutManager(linearManager);
        }

        binding.recipeCatalogFavoritesFragmentRecyclerView.setHasFixedSize(true);
        adapter = new RecipeCatalogFavoritesRecyclerAdapter(viewModel);
        binding.recipeCatalogFavoritesFragmentRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}
