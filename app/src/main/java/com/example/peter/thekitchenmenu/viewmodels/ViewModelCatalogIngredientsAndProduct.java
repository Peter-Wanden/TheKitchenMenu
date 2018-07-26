package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.IngredientAndProductDAO;
import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.IngredientAndProduct;

import java.util.List;

/**
 * View model matching a recipes ingredients and products
 */
public class ViewModelCatalogIngredientsAndProduct extends AndroidViewModel {

    /* Instantiate an object for the data we want to cache */
    private LiveData<List<IngredientAndProduct>> ingredients;

    public ViewModelCatalogIngredientsAndProduct(@NonNull Application application, int recipeId) {
        super(application);

        /* Initialise member variable */
        TKMDatabase database = TKMDatabase.getInstance(this.getApplication());
        ingredients = database.getIngredientsAndProducts().loadIngredientsAndProducts(recipeId);
    }

    /* Getter for our live data object */
    public LiveData<List<IngredientAndProduct>> getIngredients(int recipeId) {
        return ingredients;
    }
}
