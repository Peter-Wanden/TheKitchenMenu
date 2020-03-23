package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity.ID;
import static com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity.TABLE_RECIPE;

@Dao
public interface RecipeEntityDao {

    @Query("SELECT * FROM " + TABLE_RECIPE)
    List<RecipeMetadataEntity> getAll();

    @Query("SELECT * FROM " + TABLE_RECIPE + " WHERE " + ID + " = :id")
    RecipeMetadataEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeMetadataEntity recipeMetadataEntity);

    @Update
    void update(RecipeMetadataEntity recipeMetadataEntity);

    @Query("DELETE FROM " + TABLE_RECIPE + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE)
    void deleteAll();
}
