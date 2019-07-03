package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.data.repository.RecipeRepository;

public class RecipeEditorViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-RecipeEditorVM";

    RecipeRepository recipeRepository;

    public RecipeEditorViewModel(@NonNull Application application,
                                 RecipeRepository recipeRepository) {
        super(application);
        this.recipeRepository = recipeRepository;
    }


}
