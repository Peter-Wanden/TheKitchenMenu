package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import com.example.peter.thekitchenmenu.ui.common.ScreensNavigator;

public class RecipeCatalogController
        implements RecipeNavigator {

    private RecipeCatalogView view;
    private ScreensNavigator navigator;

    public RecipeCatalogController(ScreensNavigator navigator) {
        this.navigator = navigator;
    }

    public void bindView(RecipeCatalogView view) {
        this.view = view;
    }

    public void onStart() {
        view.registerListener(this);
    }

    public void onStop() {
        view.unregisterListener(this);
    }

    @Override
    public void onAddRecipeClicked() {
        navigator.toRecipeEditor();
    }

    @Override
    public void onRecipeClicked(String recipeDomainId) {
        navigator.toRecipeEditor(recipeDomainId);
    }

    @Override
    public void onNavigateUpClicked() {
        navigator.onSupportNavigateUp();
    }
}
