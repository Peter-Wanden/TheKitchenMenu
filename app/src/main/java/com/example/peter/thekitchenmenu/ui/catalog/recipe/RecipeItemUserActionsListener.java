package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.UseCaseRecipeIdentityAndDurationList;

/**
 * Listener used with data binding to process user actions on a recipe in a catalog list
 */
public interface RecipeItemUserActionsListener {

    void onRecipeClicked(UseCaseRecipeIdentityAndDurationList.ListItemModel listItemModel);

    void onAddToFavoritesClicked(UseCaseRecipeIdentityAndDurationList.ListItemModel listItemModel);

    void onRemoveFromFavoritesClicked(UseCaseRecipeIdentityAndDurationList.ListItemModel listItemModel);
}
