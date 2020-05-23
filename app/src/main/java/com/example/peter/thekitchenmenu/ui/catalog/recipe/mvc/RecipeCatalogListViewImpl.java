package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView;
import com.example.peter.thekitchenmenu.ui.common.views.ViewFactory;
import com.example.peter.thekitchenmenu.ui.common.views.BaseObservableViewMvc;

import java.util.List;

import javax.annotation.Nullable;

public class RecipeCatalogListViewImpl
        extends
        BaseObservableViewMvc<RecipeListItemView.RecipeListItemUserActions>
        implements
        RecipeCatalogListView {

    private RecipeCatalogRecyclerAdapter adapter;

    public RecipeCatalogListViewImpl(LayoutInflater inflater,
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
        adapter = new RecipeCatalogRecyclerAdapter(this, viewFactory);
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

    @Override
    public void bindRecipes(List<Recipe> recipes) {
        adapter.bindRecipes(recipes);
    }
}
