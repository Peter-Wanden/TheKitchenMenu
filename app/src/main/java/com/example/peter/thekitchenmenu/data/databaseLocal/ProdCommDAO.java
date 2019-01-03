package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.database.Cursor;

import com.example.peter.thekitchenmenu.data.entity.DmProdComm;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static android.app.SearchManager.SUGGEST_COLUMN_INTENT_DATA;
import static android.app.SearchManager.SUGGEST_COLUMN_TEXT_1;
import static android.app.SearchManager.SUGGEST_COLUMN_TEXT_2;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.TABLE_PROD_COMM;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_DESC;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_ID;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_MADE_BY;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_REMOTE_REF_ID;
import static com.example.peter.thekitchenmenu.data.entity.FtsProdComm.TABLE_FTS_PROD_COMM;

@Dao
public interface ProdCommDAO {

    // Gets all DmProdComm objects stored locally.
    @Query("SELECT * " +
            "FROM "+ TABLE_PROD_COMM +
            " ORDER BY " + PROD_COMM_DESC)
    LiveData<List<DmProdComm>> getAll();

    // The Integer type parameter tells Room to use a PositionalDataSource
    // object, with position-based loading under the hood.
    // ToDo - Implement paging adapter
    @Query("SELECT * " +
            "FROM "+ TABLE_PROD_COMM +
            " ORDER BY " + PROD_COMM_DESC)
    DataSource.Factory<Integer, DmProdComm> getAllPaged();

    // Gets a list of DmProdComm given an array if ID's.
    @Query("SELECT * " +
            "FROM " + TABLE_PROD_COMM +
            " WHERE " + PROD_COMM_ID +
            " IN(:idArray)")
    LiveData<List<DmProdComm>> getByIdArray(int[] idArray);

    // Gets a single DmProdComm by specifying its ID
    @Query("SELECT * " +
           "FROM " + TABLE_PROD_COMM +
            " WHERE id = :id")
    LiveData<DmProdComm> getById(int id);

    // Retrieves a single DmProdComm by specifying its remote database reference
    @Query("SELECT * " +
            "FROM " + TABLE_PROD_COMM +
            " WHERE " + PROD_COMM_REMOTE_REF_ID + " = :remoteId")
    DmProdComm getByRemoteId(String remoteId);

    @Insert
    void insert(DmProdComm dmProdComm);

    @Insert
    void insertAll(List<DmProdComm> listDmProdComm);

    @Update
    void update(DmProdComm dmProdComm);

    @Update
    void updateAll(List<DmProdComm> listDmProdComm);

    @Delete
    void delete(DmProdComm dmProdComm);

    @Query("DELETE FROM " + TABLE_PROD_COMM)
    void deleteAll();

    @Query("SELECT " + TABLE_PROD_COMM + "." + PROD_COMM_ID + " AS _id, " +
            TABLE_PROD_COMM + "." + PROD_COMM_DESC + " AS " + SUGGEST_COLUMN_TEXT_1 + ", " +
            TABLE_PROD_COMM + "." + PROD_COMM_MADE_BY + " AS " + SUGGEST_COLUMN_TEXT_2 + ", " +
            TABLE_PROD_COMM + "." + PROD_COMM_ID + " AS " + SUGGEST_COLUMN_INTENT_DATA + " " +
            "FROM " + TABLE_PROD_COMM + " JOIN " + TABLE_FTS_PROD_COMM + " ON (" +
            TABLE_PROD_COMM + "." + PROD_COMM_ID + " = " + TABLE_FTS_PROD_COMM + ".rowid)" +
            " WHERE " + TABLE_FTS_PROD_COMM + " MATCH :query")
    Cursor searchAllProducts(String query);
}
