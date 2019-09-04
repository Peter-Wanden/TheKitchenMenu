package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.entity.RecipeEntity.ID;
import static com.example.peter.thekitchenmenu.data.entity.RecipeEntity.TABLE_RECIPE;

@Dao
public interface RecipeEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE)
    List<RecipeEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE + " WHERE " + ID + " = :id")
    RecipeEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeEntity recipeEntity);

    @Update
    void update(RecipeEntity recipeEntity);

    @Query("DELETE FROM " + TABLE_RECIPE + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE)
    void deleteAll();
}
