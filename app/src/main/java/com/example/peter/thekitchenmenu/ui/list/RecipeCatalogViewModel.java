package com.example.peter.thekitchenmenu.ui.list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Recipe;

import java.util.List;

public class RecipeCatalogViewModel extends AndroidViewModel {

    public static final String LOG_TAG = RecipeCatalogViewModel.class.getSimpleName();

    /* Instantiate an object for the data we want to cache */
    private LiveData<List<Recipe>> recipes;

    public RecipeCatalogViewModel(@NonNull Application application) {
        super(application);

        /* Initialise member variable */
        TKMDatabase database = TKMDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Actively retrieving the recipes from the Database");
        recipes = database.recipeDao().loadRecipes();
    }

    /* Getter for our live data object */
    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
