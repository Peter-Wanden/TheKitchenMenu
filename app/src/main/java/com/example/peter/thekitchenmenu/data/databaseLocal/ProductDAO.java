package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.database.Cursor;

import com.example.peter.thekitchenmenu.data.entity.Product;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static android.app.SearchManager.*;
import static com.example.peter.thekitchenmenu.data.entity.Product.*;
import static com.example.peter.thekitchenmenu.data.entity.ProductFastTextSearch.TABLE_FTS_PRODUCT;

@Dao
public interface ProductDAO {

    @Query("SELECT * " +
            "FROM "+ TABLE_PRODUCT +
            " ORDER BY " + DESCRIPTION)
    LiveData<List<Product>> getAll();

    // The Integer type parameter tells Room to use a PositionalDataSource
    // object, with position-based loading under the hood.
    // ToDo - Implement paging adapter
    @Query("SELECT * " +
            "FROM "+ TABLE_PRODUCT +
            " ORDER BY " + DESCRIPTION)
    DataSource.Factory<Integer, Product> getAllPaged();

    @Query("SELECT * " +
            "FROM " + TABLE_PRODUCT +
            " WHERE " + ID +
            " IN(:idArray)")
    LiveData<List<Product>> getByIdArray(int[] idArray);

    @Query("SELECT * " +
           "FROM " + TABLE_PRODUCT +
            " WHERE id = :id")
    LiveData<Product> getById(int id);

    @Query("SELECT * " +
            "FROM " + TABLE_PRODUCT +
            " WHERE " + REMOTE_PRODUCT_ID + " = :remoteId")
    Product getByRemoteId(String remoteId);

    @Insert
    void insert(Product product);

    @Insert
    void insertAll(List<Product> listProduct);

    @Update
    void update(Product product);

    @Update
    void updateAll(List<Product> listProduct);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM " + TABLE_PRODUCT)
    void deleteAll();

    @Query("SELECT " +
            TABLE_PRODUCT + "." + ID + " AS _id, " +
            TABLE_PRODUCT + "." + DESCRIPTION + " AS " + SUGGEST_COLUMN_TEXT_1 + ", " +
            TABLE_PRODUCT + "." + MADE_BY + " AS " + SUGGEST_COLUMN_TEXT_2 + ", " +
            TABLE_PRODUCT + "." + ID + " AS " + SUGGEST_COLUMN_INTENT_DATA + " " +
            "FROM " +
            TABLE_PRODUCT + " JOIN " + TABLE_FTS_PRODUCT + " ON (" +
            TABLE_PRODUCT + "." + ID + " = " + TABLE_FTS_PRODUCT + ".rowid)" +
            " WHERE " +
            TABLE_FTS_PRODUCT + " MATCH :query")
    Cursor findProductsThatMatch(String query);
}
