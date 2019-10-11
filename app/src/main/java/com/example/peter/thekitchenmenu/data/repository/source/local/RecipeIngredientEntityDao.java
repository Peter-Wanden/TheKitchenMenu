package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity.*;

@Dao
public interface RecipeIngredientEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT)
    List<RecipeIngredientQuantityEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + RECIPE_ID + " = :recipeId")
    List<RecipeIngredientQuantityEntity> getByRecipeId(String recipeId);

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + PRODUCT_ID + " = :productId")
    List<RecipeIngredientQuantityEntity> getByProductId(String productId);

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + INGREDIENT_ID + " = :ingredientId")
    List<RecipeIngredientQuantityEntity> getByIngredientId(String ingredientId);

    @Query("SELECT * FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + ID + " = :id")
    RecipeIngredientQuantityEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeIngredientQuantityEntity ingredientEntity);

    @Update
    void update(RecipeIngredientQuantityEntity ingredientEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_INGREDIENT + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE_INGREDIENT)
    void deleteAll();
}
