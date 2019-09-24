package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipePortions;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

public class RecipePortionsViewModel extends ViewModel implements RecipeModelComposite.RecipeModelActions {

    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;
    private DataSourceRecipePortions dataSource;
    private Resources resources;


    private RecipePortionsViewModel(DataSourceRecipePortions dataSource,
                                    TimeProvider timeProvider, Resources resources) {
        this.dataSource = dataSource;
        this.resources = resources;
    }

    @Override
    public void start(String recipeId) {

    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {

    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }
}
