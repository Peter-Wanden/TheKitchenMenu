package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity.*;

@Dao
public interface RecipeDurationEntityDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    List<RecipeDurationEntity> getAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :id")
    RecipeDurationEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeDurationEntity recipeDurationEntity);

    @Update
    void update(RecipeDurationEntity recipeDurationEntity);

    @Query("DELETE FROM " + TABLE_NAME + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_NAME)
    void deleteAll();

}
