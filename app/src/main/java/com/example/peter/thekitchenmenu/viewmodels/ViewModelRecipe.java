package com.example.peter.thekitchenmenu.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Recipe;

/**
 * View model for a recipe
 */
public class ViewModelRecipe extends ViewModel {

    private LiveData<Recipe> recipe;

    public ViewModelRecipe(TKMDatabase mDb, int mRecipeId) {
        recipe = mDb.getRecipeDao().loadRecipeById(mRecipeId);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
}

