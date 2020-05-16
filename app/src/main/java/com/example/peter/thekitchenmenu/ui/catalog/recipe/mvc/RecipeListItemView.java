package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.view.View;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;

public interface RecipeListItemView {

    public interface RecipeListItemUserActionsListener {
        void onRecipeClicked(String recipeDomainId);

        void onAddToFavoritesClicked(String recipeDomainId);

        void onRemoveFromFavoritesClicked(String recipeDomainId);
    }

    View getRootView();

    void bindRecipe(RecipeResponse recipeResponse);

    void registerListener(RecipeListItemUserActionsListener listener);

    void unregisterListener(RecipeListItemUserActionsListener listener);
}