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
import com.example.peter.thekitchenmenu.databinding.RecipeCatalogAllFragmentBinding;

import javax.annotation.Nonnull;

public class RecipeCatalogAllFragment extends Fragment {

    private static final String TAG = "tkm-" + RecipeCatalogAllFragment.class.getSimpleName() + " ";
    private RecipeCatalogViewModel viewModel;
    private RecipeCatalogAllRecyclerAdapter adapter;

    public RecipeCatalogAllFragment() {
    }

    public static RecipeCatalogAllFragment newInstance() {
        return new RecipeCatalogAllFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getRecipeListLiveData().observe(requireActivity(), recipeListItemModels -> {
            if (recipeListItemModels != null) {
                adapter.setRecipeModels(recipeListItemModels);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        RecipeCatalogAllFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.recipe_catalog_all_fragment,
                container,
                false);

        viewModel = RecipeCatalogActivity.obtainViewModel(requireActivity());
        binding.setViewModel(viewModel);

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager((requireActivity())
                    .getApplicationContext(), 2);

            binding.recipeCatalogAllFragmentRecyclerView.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new LinearLayoutManager(requireActivity()
                    .getApplicationContext(), RecyclerView.VERTICAL, false);

            binding.recipeCatalogAllFragmentRecyclerView.setLayoutManager(linearManager);
        }

        binding.recipeCatalogAllFragmentRecyclerView.setHasFixedSize(true);
        adapter = new RecipeCatalogAllRecyclerAdapter(viewModel);
        binding.recipeCatalogAllFragmentRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}
