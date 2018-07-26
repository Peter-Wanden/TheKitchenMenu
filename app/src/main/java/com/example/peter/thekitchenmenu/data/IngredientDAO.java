package com.example.peter.thekitchenmenu.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.peter.thekitchenmenu.model.Ingredient;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface IngredientDAO {

    @Insert
    void insert(Ingredient ingredients);

    @Update(onConflict = REPLACE)
    void update(Ingredient... ingredients);

    @Delete
    void delete(Ingredient... ingredients);

    /* Query for retrieving ingredients for a recipe */
    @Query("SELECT * FROM Ingredient WHERE Recipe_id=:recipeId")
    LiveData<List<Ingredient>> loadIngredientsForRecipe(final int recipeId);
}
