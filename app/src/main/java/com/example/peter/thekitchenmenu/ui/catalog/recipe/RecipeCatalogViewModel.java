package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.entity.FavoriteRecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

import java.util.LinkedHashMap;
import java.util.List;

public class RecipeCatalogViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-RecipeCatalogVM";

    private DataSource<RecipeEntity> recipeRepository;
    private RecipeNavigator navigator;

    public ObservableBoolean dataLoading = new ObservableBoolean(); // todo - setup empty screen
    public ObservableBoolean isDataLoadingError = new ObservableBoolean();

    private LinkedHashMap<String, RecipeEntity> recipeMap = new LinkedHashMap<>();
    private boolean recipesAreLoading;
    private LinkedHashMap<String, FavoriteRecipeEntity> favoriteRecipeMap = new LinkedHashMap<>();
    private boolean favoriteRecipesAreLoading;

    public RecipeCatalogViewModel(@NonNull Application application,
                                  DataSource<RecipeEntity> recipeRepository) {
        super(application);
        this.recipeRepository = recipeRepository;
    }

    void setNavigator(RecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start() {
        loadRecipeEntities();
        loadFavoriteRecipeEntities(); // todo
    }

    private void loadRecipeEntities() {
        loadRecipeEntities(false);
    }

    private void loadRecipeEntities(boolean forceUpdate) {
        loadRecipeEntities(forceUpdate, true);
    }

    private void loadRecipeEntities(boolean forceUpdate, final boolean showLoadingUi) {
        recipeMap.clear();
        recipesAreLoading = true;

        if (showLoadingUi)
            dataLoading.set(true);

//        if (forceUpdate)
//            recipeRepository.refresh();

        recipeRepository.getAll(new DataSource.GetAllCallback<RecipeEntity>() {
            @Override
            public void onAllLoaded(List<RecipeEntity> recipeEntities) {
                if (showLoadingUi)
                    dataLoading.set(false);

                isDataLoadingError.set(false); // todo - setup loading error screen
                recipesAreLoading = false;

                if (recipeMap == null)
                    recipeMap = new LinkedHashMap<>();
                else
                    recipeMap.clear();

                for (RecipeEntity entity : recipeEntities) {
                    recipeMap.put(entity.getId(), entity);
                }
                prepareModels();

            }

            @Override
            public void onDataNotAvailable() {
                isDataLoadingError.set(true);
                recipesAreLoading = false;
                prepareModels();
            }
        });
    }

    private void loadFavoriteRecipeEntities() {
        loadFavoriteRecipeEntities(false);
    }

    private void loadFavoriteRecipeEntities(boolean forceUpdate) {
        loadFavoriteRecipeEntities(forceUpdate, false);
    }

    private void loadFavoriteRecipeEntities(boolean forceUpdate, boolean showLoadingUi) {
        favoriteRecipeMap.clear();
        favoriteRecipesAreLoading = true;

        if (showLoadingUi)
            dataLoading.set(true);

        if (forceUpdate) {

        }
    }

    private void prepareModels() {

    }

    public void addRecipe() {

        if (navigator != null)
            navigator.addRecipe();
    }
}
