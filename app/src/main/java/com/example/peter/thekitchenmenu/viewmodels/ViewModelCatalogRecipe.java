package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Recipe;

import java.util.List;

public class ViewModelCatalogRecipe extends AndroidViewModel {

    /* Instantiate an object for the data we want to cache */
    private LiveData<List<Recipe>> recipes;

    public ViewModelCatalogRecipe(@NonNull Application application) {
        super(application);

        /* Initialise member variable */
        TKMDatabase database = TKMDatabase.getInstance(this.getApplication());
        recipes = database.getRecipeDao().loadRecipes();
    }

    /* Getter for our live data object */
    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
