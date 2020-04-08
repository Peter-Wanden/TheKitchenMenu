package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity.*;

@Dao
public interface RecipeDurationDao {

    @Query("SELECT * FROM " + TABLE_RECIPE_DURATION)
    List<RecipeDurationEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE_DURATION + " WHERE id = :id")
    RecipeDurationEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeDurationEntity recipeDurationEntity);

    @Update
    void update(RecipeDurationEntity recipeDurationEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_DURATION + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE_DURATION)
    void deleteAll();

}
