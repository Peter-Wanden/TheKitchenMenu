package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.ui.common.controllers.BaseFragment;

import javax.annotation.Nonnull;

public class RecipeCatalogFragment
        extends
        BaseFragment {

    public static RecipeCatalogFragment newInstance() {
        return new RecipeCatalogFragment();
    }

    private RecipeCatalogListController controller;

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {

        RecipeCatalogListView view = getCompositionRoot().
                getViewFactory().
                getRecipeCatalogListView(parent);

        controller = getCompositionRoot().getRecipeCatalogListController();
        controller.bindView(view);

        view.registerListener(controller);

        return view.getRootView();
    }

    @Override
    public void onStart() {
        super.onStart();
        controller.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.onStop();
    }
}
