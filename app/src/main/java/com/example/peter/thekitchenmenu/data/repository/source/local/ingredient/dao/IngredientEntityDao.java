package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity.*;

@Dao
public interface IngredientEntityDao {

    @Query("SELECT * FROM " + TABLE_INGREDIENTS + " ORDER BY " + NAME)
    List<IngredientEntity> getAll();

    @Query("SELECT * FROM " + TABLE_INGREDIENTS + " WHERE id = :id")
    IngredientEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IngredientEntity ingredientEntity);

    @Update
    void update(IngredientEntity ingredientEntity);

    @Query("DELETE FROM " + TABLE_INGREDIENTS + " WHERE id = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_INGREDIENTS)
    void deleteAll();

}
