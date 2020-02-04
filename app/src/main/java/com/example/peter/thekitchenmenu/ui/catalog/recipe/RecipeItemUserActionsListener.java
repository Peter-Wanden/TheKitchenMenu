package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipelist.RecipeListItemModel;

/**
 * RecipeClientListener used with data binding to process user actions on a recipe in a catalog list
 */
public interface RecipeItemUserActionsListener {

    void onRecipeClicked(RecipeListItemModel listItemModel);

    void onAddToFavoritesClicked(RecipeListItemModel listItemModel);

    void onRemoveFromFavoritesClicked(RecipeListItemModel listItemModel);
}
