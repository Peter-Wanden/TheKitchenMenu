package com.example.peter.thekitchenmenu.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.TKMDatabase;

public class AddRecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final TKMDatabase mDb;
    private final int mRecipeId;


    public AddRecipeViewModelFactory(TKMDatabase mDb, int mRecipeId) {
        this.mDb = mDb;
        this.mRecipeId = mRecipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddRecipeViewModel(mDb, mRecipeId);
    }
}
