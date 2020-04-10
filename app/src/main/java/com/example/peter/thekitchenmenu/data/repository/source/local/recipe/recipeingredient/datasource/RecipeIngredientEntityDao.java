package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientEntity.*;

@Dao
public interface RecipeIngredientEntityDao {

    String TABLE = TABLE_RECIPE_INGREDIENT;

    @Query("SELECT * FROM " + TABLE + " WHERE " + DATA_ID + " = :dataId")
    RecipeIngredientEntity getByDataId(String dataId);

    @Query("SELECT * FROM " + TABLE + " WHERE " + RECIPE_INGREDIENT_ID + " = :recipeIngredientId")
    List<RecipeIngredientEntity> getAllByRecipeIngredientId(String recipeIngredientId);

    @Query("SELECT * FROM " + TABLE + " WHERE " + RECIPE_DOMAIN_ID + " = :recipeId")
    List<RecipeIngredientEntity> getAllByRecipeId(String recipeId);

    @Query("SELECT * FROM " + TABLE + " WHERE " + PRODUCT_DATA_ID + " = :productId")
    List<RecipeIngredientEntity> getAllByProductId(String productId);

    @Query("SELECT * FROM " + TABLE + " WHERE " + INGREDIENT_DOMAIN_ID + " = :ingredientId")
    List<RecipeIngredientEntity> getAllByIngredientId(String ingredientId);

    @Query("SELECT * FROM " + TABLE)
    List<RecipeIngredientEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeIngredientEntity ingredientEntity);

    @Update
    void update(RecipeIngredientEntity ingredientEntity);

    @Query("DELETE FROM " + TABLE + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE + " WHERE " + RECIPE_INGREDIENT_ID + " = :recipeIngredientId")
    void deleteAllByRecipeIngredientId(String recipeIngredientId);

    @Query("DELETE FROM " + TABLE + " WHERE " + RECIPE_DOMAIN_ID + " = :recipeId")
    void deleteAllByRecipeId(String recipeId);

    @Query("DELETE FROM " + TABLE + " WHERE " + PRODUCT_DATA_ID + " = :productId")
    void deleteAllByProductId(String productId);

    @Query("DELETE FROM " + TABLE + " WHERE " + INGREDIENT_DOMAIN_ID + " = :ingredientId")
    void deleteAllByIngredientId(String ingredientId);

    @Query("DELETE FROM " + TABLE)
    void deleteAll();
}
