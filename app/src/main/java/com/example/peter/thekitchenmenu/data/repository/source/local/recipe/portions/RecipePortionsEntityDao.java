package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsEntity.*;

@Dao
public interface RecipePortionsEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_PORTIONS)
    List<RecipePortionsEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_PORTIONS + " WHERE " + ID + " = :id")
    RecipePortionsEntity getById(String id);

    @Query("SELECT * FROM " + TABLE_RECIPE_PORTIONS + " WHERE " + RECIPE_ID + " = :recipeId")
    RecipePortionsEntity getByRecipeId(String recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipePortionsEntity portionsEntity);

    @Update
    void update(RecipePortionsEntity portionsEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_PORTIONS + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE_PORTIONS)
    void deleteAll();
}
