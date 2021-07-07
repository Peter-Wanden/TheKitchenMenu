package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView;
import com.example.peter.thekitchenmenu.ui.common.views.ObservableViewMvc;

import java.util.List;

public interface RecipeCatalogListView
        extends
        ObservableViewMvc<RecipeListItemView.RecipeListItemUserActions>,
        RecipeListItemView.RecipeListItemUserActions {

    void bindRecipes(List<Recipe> recipes);

    void showProgressBar();

    void hideProgressBar();
}
