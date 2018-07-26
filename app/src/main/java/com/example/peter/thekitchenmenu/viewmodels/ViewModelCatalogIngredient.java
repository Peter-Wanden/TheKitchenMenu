package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Ingredient;

import java.util.List;

public class ViewModelCatalogIngredient extends AndroidViewModel {

    /* Instantiate an object for the data we want to cache */
    private LiveData<List<Ingredient>> ingredients;


    public ViewModelCatalogIngredient(@NonNull Application application, int recipeId) {
        super(application);

        /* Initialise member variable */
        TKMDatabase database = TKMDatabase.getInstance(this.getApplication());
        ingredients = database.getIngredientDao().loadIngredientsForRecipe(recipeId);
    }

    /* Getter for our live data object */
    public LiveData<List<Ingredient>> getIngredient() {return ingredients;}
}
