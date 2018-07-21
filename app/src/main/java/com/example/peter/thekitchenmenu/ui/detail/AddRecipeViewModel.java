package com.example.peter.thekitchenmenu.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Recipe;

/**
 * View model for a single recipe
 */
public class AddRecipeViewModel extends ViewModel {

    private LiveData<Recipe> recipe;

    public AddRecipeViewModel(TKMDatabase mDb, int mRecipeId) {
        recipe = mDb.recipeDao().loadRecipeById(mRecipeId);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
}

