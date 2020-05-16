package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeListItemView.RecipeListItemUserActionsListener;
import com.example.peter.thekitchenmenu.ui.common.ViewFactory;
import com.example.peter.thekitchenmenu.ui.common.views.BaseObservableViewMvc;

import javax.annotation.Nullable;

public class RecipeCatalogListView
        extends BaseObservableViewMvc<RecipeListItemUserActionsListener>
        implements RecipeListItemUserActionsListener {

    public RecipeCatalogListView(LayoutInflater inflater,
                                 @Nullable ViewGroup parent,
                                 ViewFactory viewFactory) {
        setRootView(inflater.inflate(
                R.layout.recipe_catalog_all_fragment,
                parent,
                false)
        );

        setupRecyclerView(viewFactory);
    }

    private void setupRecyclerView(ViewFactory viewFactory) {
        RecyclerView recyclerView = findViewById(R.id.recipe_catalog_all_fragment_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        RecipeCatalogRecyclerAdapter adapter = new RecipeCatalogRecyclerAdapter(
                this, viewFactory);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRecipeClicked(String recipeDomainId) {
        getListeners().forEach((listener) -> listener.onRecipeClicked(recipeDomainId));
    }

    @Override
    public void onAddToFavoritesClicked(String recipeDomainId) {
        getListeners().forEach((listener) -> listener.onAddToFavoritesClicked(recipeDomainId));
    }

    @Override
    public void onRemoveFromFavoritesClicked(String recipeDomainId) {
        getListeners().forEach((listener) -> listener.onRemoveFromFavoritesClicked(recipeDomainId));
    }
}
