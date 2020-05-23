package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import androidx.databinding.ObservableBoolean;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
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

    public ObservableBoolean dataLoading = new ObservableBoolean(); // todo - setup empty screen
    public ObservableBoolean isDataLoadingError = new ObservableBoolean();
    public ObservableBoolean showEmptyScreen = new ObservableBoolean();

    private MutableLiveData<List<Recipe>> recipeList = new MutableLiveData<>();

    public RecipeCatalogViewModel(@Nonnull UseCaseHandler handler,
                                  @Nonnull RecipeList useCase) {
        this.handler = handler;
        this.useCase = useCase;
    }

    MutableLiveData<List<Recipe>> getRecipeListLiveData() {
        return recipeList;
    }

    void start() {
        loadRecipes();
    }

    private void loadRecipes() {
        dataLoading.set(true);

        handler.executeAsync(
                useCase,
                getRequestModel(RecipeListFilter.ALL_RECIPES),
                getCallback()
        );
    }

    private RecipeListRequest getRequestModel(RecipeListFilter filter) {
        return new RecipeListRequest.Builder().getDefault().
                setModel(
                        new RecipeListRequest.Model.Builder().
                                getDefault().
                                setFilter(filter).
                                build()).
                build();
    }

    private UseCaseBase.Callback<RecipeListResponse> getCallback() {
        return new UseCaseBase.Callback<RecipeListResponse>() {
            @Override
            public void onSuccessResponse(RecipeListResponse response) {
                dataLoading.set(false);
//                recipeList.setValue(response.getRecipeListItemModels());
            }

            @Override
            public void onErrorResponse(RecipeListResponse response) {
                dataLoading.set(false);
//                dataLoadingFailed(response.getResultStatus());

            }
        };
    }

//    private void dataLoadingFailed(ResultStatus reason) {
//        dataLoading.set(false);
//        if (reason == ResultStatus.DATA_NOT_AVAILABLE) {
//            showEmptyScreen.set(true);
//        } else if (reason == ResultStatus.DATA_LOADING_ERROR) {
//            isDataLoadingError.set(true);
//        }
//    }

    void addRecipe() {

    }

    void viewRecipe(RecipeListItemModel listItemModel) {

    }

    void addToFavorites(RecipeListItemModel listItemModel) {

    }

    void removeFromFavorites(RecipeListItemModel listItemModel) {

    }
}
