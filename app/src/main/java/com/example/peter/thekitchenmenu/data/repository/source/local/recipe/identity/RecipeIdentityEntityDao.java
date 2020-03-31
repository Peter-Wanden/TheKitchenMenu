package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.RecipeIdentityEntity.*;

@Dao
public interface RecipeIdentityEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_IDENTITY + " ORDER BY " + TITLE)
    List<RecipeIdentityEntity> gatAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_IDENTITY + " WHERE " + ID + " = :id")
    RecipeIdentityEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeIdentityEntity recipeIdentityEntity);

    @Update
    void update(RecipeIdentityEntity recipeIdentityEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_IDENTITY + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE_IDENTITY)
    void deleteAll();
}
