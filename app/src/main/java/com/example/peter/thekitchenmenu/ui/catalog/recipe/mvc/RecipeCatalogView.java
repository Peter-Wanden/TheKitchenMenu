package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;

public class RecipeCatalogView {

    private View rootView;

    public RecipeCatalogView(LayoutInflater inflater, ViewGroup parent) {
        rootView = inflater.inflate(
                R.layout.recipe_catalog_activity,
                parent,
                false
        );
    }

    private <T extends View> T findViewById(int id) {
        return getRootView().findViewById(id);
    }

    public View getRootView() {
        return rootView;
    }

    private Context getContext() {
        return getRootView().getContext();
    }
}
