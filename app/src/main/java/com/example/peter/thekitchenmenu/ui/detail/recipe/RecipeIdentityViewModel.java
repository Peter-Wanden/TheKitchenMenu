package com.example.peter.thekitchenmenu.ui.detail.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.repository.RecipeRepository;

public class RecipeIdentityViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-RecipeIdentityVM";

    RecipeRepository recipeRepository;

    public RecipeIdentityViewModel(@NonNull Application application,
                                   RecipeRepository recipeRepository) {
        super(application);
        this.recipeRepository = recipeRepository;
    }

    void start(String recipeId) {

    }
}
