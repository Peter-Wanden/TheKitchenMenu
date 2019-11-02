package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.peter.thekitchenmenu.data.model.RecipeListItemModel;

import java.util.List;

import static com.example.peter.thekitchenmenu.ui.catalog.recipe.RecipeListDataInteractor.*;

public class RecipeCatalogViewModel
        extends ViewModel
        implements RecipeListCallback {

    private static final String TAG = "tkm-RecipeCatalogVM";

    @NonNull
    private RecipeListDataInteractor dataInteractor;
    private RecipeNavigator navigator;

    public ObservableBoolean dataLoading = new ObservableBoolean(); // todo - setup empty screen
    public ObservableBoolean isDataLoadingError = new ObservableBoolean();
    public ObservableBoolean showEmptyScreen = new ObservableBoolean();

    private MutableLiveData<List<RecipeListItemModel>> recipeList = new MutableLiveData<>();

    public RecipeCatalogViewModel(@NonNull RecipeListDataInteractor dataInteractor) {
        this.dataInteractor = dataInteractor;
        dataInteractor.registerListener(this);
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
        dataInteractor.getRecipes(RecipeFilter.ALL);
    }

    @Override
    public void recipeList(List<RecipeListItemModel> recipeList) {
        dataLoading.set(false);
        this.recipeList.setValue(recipeList);
    }

    @Override
    public void dataLoadingFailed(FailReason reason) {
        dataLoading.set(false);
        if (reason == FailReason.NO_DATA_AVAILABLE) {
            showEmptyScreen.set(true);
        } else if (reason == FailReason.DATA_LOADING_ERROR) {
            isDataLoadingError.set(true);
        }
    }

    void addRecipe() {
        if (navigator != null)
            navigator.addRecipe();
    }

    void viewRecipe(RecipeListItemModel listItemModel) {

    }

    void addToFavorites(RecipeListItemModel listItemModel) {

    }

    void removeFromFavorites(RecipeListItemModel listItemModel) {

    }
}
