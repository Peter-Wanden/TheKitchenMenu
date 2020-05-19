package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.common.views.ViewFactory;
import com.example.peter.thekitchenmenu.ui.common.toolbar.ToolbarView;
import com.example.peter.thekitchenmenu.ui.common.views.BaseObservableViewMvc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.annotation.Nullable;

public class RecipeCatalogViewImpl
        extends
        BaseObservableViewMvc<RecipeNavigator>
        implements
        RecipeCatalogView {

    private Toolbar toolbar;
    private ToolbarView toolbarView;

    public RecipeCatalogViewImpl(LayoutInflater inflater,
                                 @Nullable ViewGroup parent,
                                 ViewFactory viewFactory) {
        setRootView(inflater.inflate(
                R.layout.recipe_catalog_activity,
                parent,
                false)
        );

        setupFab();

        toolbar = findViewById(R.id.recipe_catalog_activity_toolbar);
        toolbarView = viewFactory.getToolbarView(parent);
        setUpToolbar();
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.recipe_catalog_activity_fab);
        fab.setOnClickListener(v -> getListeners().
                forEach((RecipeNavigator::onAddRecipeClicked)));
    }

    private void setUpToolbar() {
        toolbarView.setTitle(getString(R.string.activity_title_recipe_catalog));
        toolbar.addView(toolbarView.getRootView());

        toolbarView.enableUpButtonAndListen(() -> getListeners().
                forEach(RecipeNavigator::onNavigateUpClicked)
        );
    }
}
