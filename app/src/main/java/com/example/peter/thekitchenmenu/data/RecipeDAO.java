package com.example.peter.thekitchenmenu.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.peter.thekitchenmenu.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {

    /* Query whole table */
    @Query("SELECT * FROM Recipes ORDER BY Category")
    LiveData<List<Recipe>> loadRecipes();

    /* Query one recipe */
    @Query("SELECT * FROM Recipes WHERE id = :id")
    LiveData<Recipe> loadRecipeById(int id);

    @Insert
    void insertRecipe(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);
}
