package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.RecipeIdentityAndDurationList;

/**
 * Listener used with data binding to process user actions on a recipe in a catalog list
 */
public interface RecipeItemUserActionsListener {

    void onRecipeClicked(RecipeIdentityAndDurationList.ListItemModel listItemModel);

    void onAddToFavoritesClicked(RecipeIdentityAndDurationList.ListItemModel listItemModel);

    void onRemoveFromFavoritesClicked(RecipeIdentityAndDurationList.ListItemModel listItemModel);
}
