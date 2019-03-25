package com.example.peter.thekitchenmenu.data.databaseLocal;

import static com.example.peter.thekitchenmenu.data.entity.ProductEntity.REMOTE_PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.TABLE_USERS_PRODUCT_DATA;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.ID;

import com.example.peter.thekitchenmenu.data.entity.UsersProductData;

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
    LiveData<List<UsersProductData>> getAll();

    @Query("SELECT * FROM " + TABLE_USERS_PRODUCT_DATA +
            " WHERE " + ID + " IN(:idArray)")
    LiveData<List<UsersProductData>> getByIdArray(int[] idArray);

    @Query("SELECT * FROM " + TABLE_USERS_PRODUCT_DATA +
            " WHERE " + ID + " = :id")
    LiveData<UsersProductData> getById(int id);

    @Query("SELECT * FROM " + TABLE_USERS_PRODUCT_DATA +
            " WHERE " + REMOTE_PRODUCT_ID + " = :remoteId")
    UsersProductData getByRemoteId(String remoteId);

    @Query("SELECT * FROM " + TABLE_USERS_PRODUCT_DATA +
            " WHERE " + PRODUCT_ID + " = :productId")
    UsersProductData getByLocalProductId(int productId);

    @Insert
    void insert(UsersProductData usersProductData);

    @Insert
    void insertAll(List<UsersProductData> listUsersProductData);

    @Update
    void update(UsersProductData usersProductData);

    @Update
    void updateAll(List<UsersProductData> listUsersProductData);

    @Delete
    void delete(UsersProductData usersProductData);

    @Query("DELETE FROM " + TABLE_USERS_PRODUCT_DATA)
    void deleteAll();
}
