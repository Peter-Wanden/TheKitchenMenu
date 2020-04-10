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

    @Query("SELECT * FROM " + TABLE_RECIPE_DURATION + " WHERE " + DATA_ID + " = :id")
    RecipeDurationEntity getById(String id);

    @Query("SELECT * FROM " + TABLE_RECIPE_DURATION + " WHERE " + DOMAIN_ID + " = :domainId")
    List<RecipeDurationEntity> getAllByDomainId(String domainId);

    @Query("SELECT * FROM " + TABLE_RECIPE_DURATION)
    List<RecipeDurationEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeDurationEntity recipeDurationEntity);

    @Update
    void update(RecipeDurationEntity recipeDurationEntity);

    @Query("DELETE FROM " + TABLE_RECIPE_DURATION + " WHERE " + DATA_ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_RECIPE_DURATION + " WHERE " + DOMAIN_ID + " = :domainId")
    void deleteAllByDomainId(String domainId);

    @Query("DELETE FROM " + TABLE_RECIPE_DURATION)
    void deleteAll();
}
