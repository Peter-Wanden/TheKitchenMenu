package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;

import javax.annotation.Nonnull;

public class RecipeCatalogFavoritesFragment extends Fragment {

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

//        RecipeCatalogFavoritesFragmentBinding binding = DataBindingUtil.inflate(
//                inflater,
//                R.layout.recipe_catalog_favorites_fragment,
//                container,
//                false);

//        return binding.getRoot();
        return null;
    }
}
