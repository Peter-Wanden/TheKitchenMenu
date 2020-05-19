package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeList;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListResponse;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView;
import com.example.peter.thekitchenmenu.ui.common.ScreensNavigator;

public class RecipeCatalogListController
        implements
        UseCase.Callback<RecipeListResponse>,
        RecipeListItemView.RecipeListItemUserActions {

    private enum ScreenState {
        IDLE, LOADING, LOADING_ERROR;
    }

    private final UseCaseHandler handler;
    private final RecipeList useCase;
    private final ScreensNavigator navigator;

    private RecipeCatalogListView view;

    public RecipeCatalogListController(UseCaseHandler handler,
                                       RecipeList useCase,
                                       ScreensNavigator navigator) {
        this.handler = handler;
        this.useCase = useCase;
        this.navigator = navigator;
    }

    void bindView(RecipeCatalogListView view) {
        this.view = view;
    }

    void onStart() {
        view.registerListener(this);
        loadRecipes();
    }

    void onStop() {
        view.unregisterListener(this);
    }

    private void loadRecipes() {
        RecipeListRequest.Model model = new RecipeListRequest.Model.Builder().
                getDefault().
                setFilter(RecipeList.RecipeListFilter.ALL).
                build();

        RecipeListRequest request = new RecipeListRequest.Builder().
                getDefault().
                setModel(model).
                build();

        handler.execute(useCase, request, this);
    }

    @Override
    public void onUseCaseSuccess(RecipeListResponse response) {

    }

    @Override
    public void onUseCaseError(RecipeListResponse response) {

    }

    @Override
    public void onRecipeClicked(String recipeDomainId) {

    }

    @Override
    public void onAddToFavoritesClicked(String recipeDomainId) {

    }

    @Override
    public void onRemoveFromFavoritesClicked(String recipeDomainId) {

    }
}
