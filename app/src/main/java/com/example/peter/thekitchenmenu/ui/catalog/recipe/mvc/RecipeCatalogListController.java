package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeList;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListResponse;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView;
import com.example.peter.thekitchenmenu.ui.common.ScreensNavigator;

public class RecipeCatalogListController
        implements
        UseCaseBase.Callback<RecipeListResponse>,
        RecipeListItemView.RecipeListItemUserActions {

    private enum ScreenState {
        IDLE, LOADING_DATA, LOADING_ERROR, DATA_DISPLAYED
    }

    private final UseCaseHandler handler;
    private final RecipeList useCase;
    private final ScreensNavigator navigator;

    private RecipeCatalogListView view;

    private ScreenState screenState = ScreenState.IDLE;

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
        screenState = ScreenState.LOADING_DATA;
        view.showProgressBar();

        RecipeListRequest.Model model = new RecipeListRequest.Model.Builder().
                getDefault().
                setFilter(RecipeList.RecipeListFilter.ALL_RECIPES).
                build();

        RecipeListRequest request = new RecipeListRequest.Builder().
                getDefault().
                setModel(model).
                build();

        handler.executeAsync(useCase, request, this);
    }

    @Override
    public void onUseCaseSuccess(RecipeListResponse response) {
        screenState = ScreenState.DATA_DISPLAYED;
        view.hideProgressBar();
        view.bindRecipes(response.getModel().getRecipes());
    }

    @Override
    public void onUseCaseError(RecipeListResponse response) {
        screenState = ScreenState.LOADING_ERROR;
        view.hideProgressBar();

    }

    @Override
    public void onRecipeClicked(String recipeDomainId) {
        navigator.toRecipeEditor(recipeDomainId);
    }

    @Override
    public void onAddToFavoritesClicked(String recipeDomainId) {

    }

    @Override
    public void onRemoveFromFavoritesClicked(String recipeDomainId) {

    }
}
