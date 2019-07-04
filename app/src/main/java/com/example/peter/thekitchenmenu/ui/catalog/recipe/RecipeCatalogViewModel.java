package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.repository.RecipeRepository;

public class RecipeCatalogViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-RecipeCatalogVM";

    private RecipeRepository recipeRepository;
    private RecipeNavigator navigator;


    public RecipeCatalogViewModel(@NonNull Application application,
                                  RecipeRepository recipeRepository) {
        super(application);
        this.recipeRepository = recipeRepository;
    }

    void setNavigator(RecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void addRecipe() {
        navigator.addRecipe();
    }


}