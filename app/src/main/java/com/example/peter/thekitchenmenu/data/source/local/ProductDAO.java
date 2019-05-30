package com.example.peter.thekitchenmenu.data.source.local;

import android.database.Cursor;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static android.app.SearchManager.*;
import static com.example.peter.thekitchenmenu.data.entity.ProductEntity.*;
import static com.example.peter.thekitchenmenu.data.entity.ProductFastTextSearch.TABLE_FTS_PRODUCT;

@Dao
public interface ProductDAO {

    @Query("SELECT * " +
            "FROM "+ TABLE_PRODUCT +
            " ORDER BY " + DESCRIPTION)
    LiveData<List<ProductEntity>> getAll();

    // The Integer type parameter tells Room to use a PositionalDataSource
    // object, with position-based loading under the hood.
    // ToDo - Implement paging adapter
    @Query("SELECT * " +
            "FROM "+ TABLE_PRODUCT +
            " ORDER BY " + DESCRIPTION)
    DataSource.Factory<Integer, ProductEntity> getAllPaged();

    @Query("SELECT * " +
            "FROM " + TABLE_PRODUCT +
            " WHERE " + ID +
            " IN(:idArray)")
    LiveData<List<ProductEntity>> getByIdArray(int[] idArray);

    @Query("SELECT * " +
           "FROM " + TABLE_PRODUCT +
            " WHERE id = :id")
    LiveData<ProductEntity> getById(int id);

    @Query("SELECT * " +
            "FROM " + TABLE_PRODUCT +
            " WHERE " + REMOTE_PRODUCT_ID + " = :remoteId")
    ProductEntity getByRemoteId(String remoteId);

    @Insert
    void insert(ProductEntity productEntity);

    @Insert
    void insertAll(List<ProductEntity> listProductEntity);

    @Update
    void update(ProductEntity productEntity);

    @Update
    void updateAll(List<ProductEntity> listProductEntity);

    @Delete
    void delete(ProductEntity productEntity);

    @Query("DELETE FROM " + TABLE_PRODUCT)
    void deleteAll();

    @Query("SELECT " +
            TABLE_PRODUCT + "." + ID + " AS _id, " +
            TABLE_PRODUCT + "." + DESCRIPTION + " AS " + SUGGEST_COLUMN_TEXT_1 + ", " +
            TABLE_PRODUCT + "." + SHOPPING_LIST_ITEM_NAME + " AS " + SUGGEST_COLUMN_TEXT_2 + ", " +
            TABLE_PRODUCT + "." + ID + " AS " + SUGGEST_COLUMN_INTENT_DATA + " " +
            "FROM " +
            TABLE_PRODUCT + " JOIN " + TABLE_FTS_PRODUCT + " ON (" +
            TABLE_PRODUCT + "." + ID + " = " + TABLE_FTS_PRODUCT + ".rowid)" +
            " WHERE " +
            TABLE_FTS_PRODUCT + " MATCH :query")
    Cursor findProductsThatMatch(String query);
}
