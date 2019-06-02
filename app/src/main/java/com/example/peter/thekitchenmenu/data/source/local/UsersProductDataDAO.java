package com.example.peter.thekitchenmenu.data.source.local;

import static com.example.peter.thekitchenmenu.data.entity.ProductEntity.REMOTE_PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity.TABLE_USERS_PRODUCT_DATA;
import static com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity.PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity.ID;

import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsersProductDataDAO {

    @Query("SELECT * FROM " + TABLE_USERS_PRODUCT_DATA)
    LiveData<List<ProductUserDataEntity>> getAll();

    @Query("SELECT * FROM " + TABLE_USERS_PRODUCT_DATA +
            " WHERE " + ID + " IN(:idArray)")
    LiveData<List<ProductUserDataEntity>> getByIdArray(int[] idArray);

    @Query("SELECT * FROM " + TABLE_USERS_PRODUCT_DATA +
            " WHERE " + ID + " = :id")
    LiveData<ProductUserDataEntity> getById(int id);

    @Query("SELECT * FROM " + TABLE_USERS_PRODUCT_DATA +
            " WHERE " + PRODUCT_ID + " = :productId")
    ProductUserDataEntity getByLocalProductId(int productId);

    @Insert
    void insert(ProductUserDataEntity productUserDataEntity);

    @Insert
    void insertAll(List<ProductUserDataEntity> listProductUserDatumEntities);

    @Update
    void update(ProductUserDataEntity productUserDataEntity);

    @Update
    void updateAll(List<ProductUserDataEntity> listProductUserDatumEntities);

    @Delete
    void delete(ProductUserDataEntity productUserDataEntity);

    @Query("DELETE FROM " + TABLE_USERS_PRODUCT_DATA)
    void deleteAll();
}
