package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeComponentStateEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeComponentStateEntity.*;

@Dao
public interface RecipeComponentStateEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_COMPONENT_STATE)
    List<RecipeComponentStateEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_COMPONENT_STATE + " WHERE " + ID + " = :id")
    RecipeComponentStateEntity getById(String id);

    @Query("SELECT * FROM " + TABLE_RECIPE_COMPONENT_STATE + " WHERE " + RECIPE_ID + " = :recipeId")
    List<RecipeComponentStateEntity> getAllByRecipeId(String recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeComponentStateEntity recipeComponentStateEntity);

    @Update
    void update(RecipeComponentStateEntity recipeComponentStateEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_COMPONENT_STATE + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE_COMPONENT_STATE + " WHERE " + RECIPE_ID + " = :recipeId")
    void deleteByRecipeId(String recipeId);

    @Query("DELETE FROM " + TABLE_RECIPE_COMPONENT_STATE)
    void deleteAll();
}
