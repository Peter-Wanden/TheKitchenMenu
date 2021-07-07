package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity.*;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM " + TABLE_INGREDIENTS + " WHERE " + DATA_ID + " = :id")
    IngredientEntity getById(String id);

    @Query("SELECT * FROM " + TABLE_INGREDIENTS + " WHERE " + DOMAIN_ID + " = :domainId")
    List<IngredientEntity> getAllByDomainId(String domainId);

    @Query("SELECT * FROM " + TABLE_INGREDIENTS + " ORDER BY " + NAME)
    List<IngredientEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IngredientEntity ingredientEntity);

    @Update
    void update(IngredientEntity ingredientEntity);

    @Query("DELETE FROM " + TABLE_INGREDIENTS + " WHERE " + DATA_ID + " = :dataId")
    void deleteByDataId(String dataId);

    @Query("DELETE FROM " + TABLE_INGREDIENTS + " WHERE " + DOMAIN_ID + " = :domainId")
    void deleteAllByDomainId(String domainId);

    @Query("DELETE FROM " + TABLE_INGREDIENTS)
    void deleteAll();

}
