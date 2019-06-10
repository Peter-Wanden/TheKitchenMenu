package com.example.peter.thekitchenmenu.data.source.local;

import static com.example.peter.thekitchenmenu.data.entity.UsedProductEntity.TABLE_USED_PRODUCTS;
import static com.example.peter.thekitchenmenu.data.entity.UsedProductEntity.PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.UsedProductEntity.ID;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsedProductEntityDAO {

    @Query("SELECT * FROM " + TABLE_USED_PRODUCTS)
    List<UsedProductEntity> getAll();

    @Query("SELECT * FROM " + TABLE_USED_PRODUCTS +
            " WHERE " + ID + " = :id")
    UsedProductEntity getById(String id);

    @Query("SELECT * FROM " + TABLE_USED_PRODUCTS +
            " WHERE " + PRODUCT_ID + " = :productId")
    UsedProductEntity getByProductId(String productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UsedProductEntity usedProduct);

    @Update
    void update(UsedProductEntity usedProduct);

    @Query("DELETE FROM " + TABLE_USED_PRODUCTS + " WHERE " + ID + " = :usedProductId")
    void deleteByUsedProductId(String usedProductId);

    @Query("DELETE FROM " + TABLE_USED_PRODUCTS + " WHERE " + PRODUCT_ID + " = :productId")
    void deleteByProductId(String productId);

    @Query("DELETE FROM " + TABLE_USED_PRODUCTS)
    void deleteAll();
}
