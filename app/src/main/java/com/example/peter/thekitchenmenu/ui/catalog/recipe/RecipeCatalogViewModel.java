package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.UseCaseRecipeIdentityAndDurationList;

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

    private MutableLiveData<List<UseCaseRecipeIdentityAndDurationList.ListItemModel>> recipeList = new MutableLiveData<>();

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

    MutableLiveData<List<UseCaseRecipeIdentityAndDurationList.ListItemModel>> getRecipeListLiveData() {
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

    private UseCaseRecipeIdentityAndDurationList.Request getRequestModel(
            UseCaseRecipeIdentityAndDurationList.RecipeFilter filter) {
        return new UseCaseRecipeIdentityAndDurationList.Request(filter);
    }

    private UseCaseInteractor.Callback<UseCaseRecipeIdentityAndDurationList.Response>
    getCallback() {
        return new UseCaseInteractor.Callback<UseCaseRecipeIdentityAndDurationList.Response>() {

            @Override
            public void onSuccess(UseCaseRecipeIdentityAndDurationList.Response response) {
                dataLoading.set(false);
                RecipeCatalogViewModel.this.recipeList.setValue(response.getRecipeListItemModels());
            }

            @Override
            public void onError(UseCaseRecipeIdentityAndDurationList.Response response) {
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

    void viewRecipe(UseCaseRecipeIdentityAndDurationList.ListItemModel listItemModel) {
        navigator.viewRecipe(listItemModel.getRecipeId());
    }

    void addToFavorites(UseCaseRecipeIdentityAndDurationList.ListItemModel listItemModel) {

    }

    void removeFromFavorites(UseCaseRecipeIdentityAndDurationList.ListItemModel listItemModel) {

    }
}
