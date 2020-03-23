package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import androidx.databinding.ObservableBoolean;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeList;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListItemModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListResponse;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeList.*;

public class RecipeCatalogViewModel extends ViewModel {

    private static final String TAG = "tkm-" + RecipeCatalogViewModel.class.getSimpleName() + " ";

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private RecipeList useCase;
    private RecipeNavigator navigator;

    public ObservableBoolean dataLoading = new ObservableBoolean(); // todo - setup empty screen
    public ObservableBoolean isDataLoadingError = new ObservableBoolean();
    public ObservableBoolean showEmptyScreen = new ObservableBoolean();

    private MutableLiveData<List<RecipeListItemModel>> recipeList = new MutableLiveData<>();

    public RecipeCatalogViewModel(@Nonnull UseCaseHandler handler,
                                  @Nonnull RecipeList useCase) {
        this.handler = handler;
        this.useCase = useCase;
    }

    void setNavigator(RecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    MutableLiveData<List<RecipeListItemModel>> getRecipeListLiveData() {
        return recipeList;
    }

    void start() {
        loadRecipes();
    }

    private void loadRecipes() {
        dataLoading.set(true);

        handler.execute(
                useCase,
                getRequestModel(RecipeFilter.ALL),
                getCallback()
        );
    }

    private RecipeListRequest getRequestModel(RecipeFilter filter) {
        return new RecipeListRequest(filter);
    }

    private UseCase.Callback<RecipeListResponse>
    getCallback() {
        return new UseCase.Callback<RecipeListResponse>() {

            @Override
            public void onSuccess(RecipeListResponse response) {
                dataLoading.set(false);
                RecipeCatalogViewModel.this.recipeList.setValue(response.getRecipeListItemModels());
            }

            @Override
            public void onError(RecipeListResponse response) {
                dataLoading.set(false);
                dataLoadingFailed(response.getResultStatus());

            }
        };
    }

    private void dataLoadingFailed(ResultStatus reason) {
        dataLoading.set(false);
        if (reason == ResultStatus.DATA_NOT_AVAILABLE) {
            showEmptyScreen.set(true);
        } else if (reason == ResultStatus.DATA_LOADING_ERROR) {
            isDataLoadingError.set(true);
        }
    }

    void addRecipe() {
        if (navigator != null)
            navigator.addRecipe();
    }

    void viewRecipe(RecipeListItemModel listItemModel) {
        navigator.viewRecipe(listItemModel.getRecipeId());
    }

    void addToFavorites(RecipeListItemModel listItemModel) {

    }

    void removeFromFavorites(RecipeListItemModel listItemModel) {

    }
}
