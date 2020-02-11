package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity.*;

@Dao
public interface RecipeIngredientEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT)
    List<RecipeIngredientEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + RECIPE_ID + " = :recipeId")
    List<RecipeIngredientEntity> getByRecipeId(String recipeId);

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + PRODUCT_ID + " = :productId")
    List<RecipeIngredientEntity> getByProductId(String productId);

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + INGREDIENT_ID + " = :ingredientId")
    List<RecipeIngredientEntity> getByIngredientId(String ingredientId);

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + ID + " = :id")
    RecipeIngredientEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeIngredientEntity ingredientEntity);

    @Update
    void update(RecipeIngredientEntity ingredientEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE_INGREDIENT)
    void deleteAll();
}
