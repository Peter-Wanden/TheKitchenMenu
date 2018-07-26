package com.example.peter.thekitchenmenu.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.TKMDatabase;

public class ViewModelFactoryRecipe extends ViewModelProvider.NewInstanceFactory{

    private final TKMDatabase mDb;
    private final int mRecipeId;

    public ViewModelFactoryRecipe(TKMDatabase database, int recipeId){
        mDb = database;
        mRecipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModelRecipe(mDb, mRecipeId);
    }
}
