package com.example.peter.thekitchenmenu.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogListView;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogView;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeListItemView;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeListItemViewImpl;

import javax.annotation.Nonnull;

public class ViewFactory {

    private final LayoutInflater inflater;

    public ViewFactory(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public RecipeCatalogView getRecipeCatalogView(@Nonnull ViewGroup parent) {
        return new RecipeCatalogView(inflater, parent);
    }

    public RecipeCatalogListView getRecipeCatalogListView(@Nonnull ViewGroup parent) {
        return new RecipeCatalogListView(inflater, parent);
    }

    public RecipeListItemView getRecipeListItemView(@Nonnull ViewGroup parent) {
        return new RecipeListItemViewImpl(inflater, parent);
    }

}
