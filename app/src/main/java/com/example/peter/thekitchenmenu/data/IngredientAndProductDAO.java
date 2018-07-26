package com.example.peter.thekitchenmenu.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.peter.thekitchenmenu.model.IngredientAndProduct;

import java.util.List;

@Dao
public interface IngredientAndProductDAO {
    @Query("SELECT Products.id AS productId, " +
            "Products.Description AS productDescription, " +
            "Products.Unit_of_Measure AS UoM, " +
            "Products.Retailer AS retailer, " +
            "Ingredient.Id AS ingredientId, " +
            "Ingredient.OneMg_Is_N_Grams AS oneMgIsNGrams, " +
            "Ingredient.Quantity_Per_Serving AS quantityPerServing, " +
            "Ingredient.Freeze AS freeze " +
            "FROM Ingredient " +
            "INNER JOIN Products ON Ingredient.Product_Id = Products.id " +
            "WHERE Ingredient.Recipe_Id =:recipeId")

    LiveData<List<IngredientAndProduct>> loadIngredientsAndProducts(final int recipeId);
}
