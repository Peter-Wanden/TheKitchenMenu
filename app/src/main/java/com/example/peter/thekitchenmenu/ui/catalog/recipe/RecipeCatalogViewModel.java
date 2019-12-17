package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.RecipeListItemModel;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.UseCaseRecipeIdentityAndDurationList;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.UseCaseRecipeIdentityAndDurationListRequestModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.UseCaseRecipeIdentityAndDurationListResponseModel;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.UseCaseRecipeIdentityAndDurationList.*;

public class RecipeCatalogViewModel extends ViewModel {

    private static final String TAG = "tkm-" + RecipeCatalogViewModel.class.getSimpleName() + " ";

    @NonNull
    private UseCaseHandler handler;
    @Nonnull
    private UseCaseRecipeIdentityAndDurationList useCase;
    private RecipeNavigator navigator;

    public ObservableBoolean dataLoading = new ObservableBoolean(); // todo - setup empty screen
    public ObservableBoolean isDataLoadingError = new ObservableBoolean();
    public ObservableBoolean showEmptyScreen = new ObservableBoolean();

    private MutableLiveData<List<RecipeListItemModel>> recipeList = new MutableLiveData<>();

    public RecipeCatalogViewModel(@Nonnull UseCaseHandler handler,
                                  @Nonnull UseCaseRecipeIdentityAndDurationList useCase) {
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
                getRequestModel(UseCaseRecipeIdentityAndDurationList.RecipeFilter.ALL),
                getCallback()
        );
    }

    private UseCaseRecipeIdentityAndDurationListRequestModel getRequestModel(
            UseCaseRecipeIdentityAndDurationList.RecipeFilter filter) {
        return new UseCaseRecipeIdentityAndDurationListRequestModel(filter);
    }

    private UseCaseInteractor.Callback<UseCaseRecipeIdentityAndDurationListResponseModel>
    getCallback() {
        return new UseCaseInteractor.Callback<UseCaseRecipeIdentityAndDurationListResponseModel>() {

            @Override
            public void onSuccess(UseCaseRecipeIdentityAndDurationListResponseModel response) {
                dataLoading.set(false);
                RecipeCatalogViewModel.this.recipeList.setValue(response.getRecipeListItemModels());
            }

            @Override
            public void onError(UseCaseRecipeIdentityAndDurationListResponseModel response) {
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
