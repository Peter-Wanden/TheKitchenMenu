package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.ui.common.views.ObservableViewMvc;

public interface RecipeListItemView
        extends
        ObservableViewMvc<RecipeListItemView.RecipeListItemUserActions> {

    interface RecipeListItemUserActions {
        void onRecipeClicked(String recipeDomainId);

        void onAddToFavoritesClicked(String recipeDomainId);

        void onRemoveFromFavoritesClicked(String recipeDomainId);
    }

    void bindRecipe(RecipeResponse recipeResponse);
}