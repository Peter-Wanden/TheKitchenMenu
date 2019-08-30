package com.example.peter.thekitchenmenu.data.repository.source.local;

import static com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity.TABLE_FAVORITE_PRODUCTS;
import static com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity.PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity.ID;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavoriteProductEntityDao {

    @Query("SELECT * FROM " + TABLE_FAVORITE_PRODUCTS)
    List<FavoriteProductEntity> getAll();

    @Query("SELECT * FROM " + TABLE_FAVORITE_PRODUCTS +
            " WHERE " + ID + " = :id")
    FavoriteProductEntity getById(String id);

    @Query("SELECT * FROM " + TABLE_FAVORITE_PRODUCTS +
            " WHERE " + PRODUCT_ID + " = :productId")
    FavoriteProductEntity getByProductId(String productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteProductEntity favoriteProduct);

    @Update
    void update(FavoriteProductEntity favoriteProduct);

    @Query("DELETE FROM " + TABLE_FAVORITE_PRODUCTS + " WHERE " + ID + " = :favoriteProductId")
    void deleteByFavoriteProductId(String favoriteProductId);

    @Query("DELETE FROM " + TABLE_FAVORITE_PRODUCTS + " WHERE " + PRODUCT_ID + " = :productId")
    void deleteByProductId(String productId);

    @Query("DELETE FROM " + TABLE_FAVORITE_PRODUCTS)
    void deleteAll();
}
