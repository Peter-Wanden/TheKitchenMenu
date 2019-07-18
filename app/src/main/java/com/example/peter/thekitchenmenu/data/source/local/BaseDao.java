package com.example.peter.thekitchenmenu.data.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T object);

    @Update
    void update(T object);

    @Delete
    void delete(T object);
}
