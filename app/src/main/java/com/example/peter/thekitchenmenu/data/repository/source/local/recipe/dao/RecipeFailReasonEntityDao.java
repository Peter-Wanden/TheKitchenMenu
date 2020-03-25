package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.entitymodel.RecipeFailReasonEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.entitymodel.RecipeFailReasonEntity.*;


@Dao
public interface RecipeFailReasonEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_FAIL_REASON)
    List<RecipeFailReasonEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_FAIL_REASON + " WHERE " + ID + " = :id")
    RecipeFailReasonEntity getById(String id);

    @Query("SELECT * FROM " + TABLE_RECIPE_FAIL_REASON + " WHERE " + RECIPE_ID + " = :recipeId")
    List<RecipeFailReasonEntity> getAllByRecipeId(String recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeFailReasonEntity e);

    @Update
    void update(RecipeFailReasonEntity e);

    @Query("DELETE FROM " + TABLE_RECIPE_FAIL_REASON + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE_FAIL_REASON + " WHERE " + RECIPE_ID + " = :recipeId")
    void deleteByRecipeId(String recipeId);

    @Query("DELETE FROM " + TABLE_RECIPE_FAIL_REASON)
    void deleteAll();
}
