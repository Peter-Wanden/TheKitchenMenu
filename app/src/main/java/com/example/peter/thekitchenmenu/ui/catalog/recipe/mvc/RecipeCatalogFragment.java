package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.ui.common.controllers.BaseFragment;

import javax.annotation.Nonnull;

public class RecipeCatalogFragment extends BaseFragment {

    private static final String TAG = "tkm-" + RecipeCatalogFragment.class.getSimpleName() + " ";

    public static RecipeCatalogFragment newInstance() {
        return new RecipeCatalogFragment();
    }

    private RecipeCatalogRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {

        RecipeCatalogListView viewMvc = getCompositionRoot().
                getViewMvcFactory().
                getRecipeCatalogListView(parent);
        viewMvc.registerListener(this);

        return viewMvc.getRootView();
    }
}
