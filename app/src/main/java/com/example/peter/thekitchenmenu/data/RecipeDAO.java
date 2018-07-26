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

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface RecipeDAO {

    /* Query whole table */
    @Query("SELECT * FROM Recipe ORDER BY Category")
    LiveData<List<Recipe>> loadRecipes();

    /* Query one recipe */
    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    LiveData<Recipe> loadRecipeById(int recipeId);

    @Insert
    void insertRecipe(Recipe recipe);

    @Insert
    void insertRecipes(Recipe... recipes);

    @Update(onConflict = IGNORE)
    void updateRecipe(Recipe... recipe);

    @Delete
    void deleteRecipe(Recipe... recipes);
}
