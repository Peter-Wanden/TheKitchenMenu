package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.DmProdComm;

import java.util.List;

@Dao
public interface ProdCommDAO {

    // Gets all ProductsCommunity objects stored locally.
    @Query("SELECT * " +
            "FROM "+ Constants.TABLE_PRODUCT_COMM +
            " ORDER BY " + Constants.TABLE_PRODUCT_COMM_DESCRIPTION)
    LiveData<List<DmProdComm>> getAllProdComms();

    // Gets a list of CommunityProducts given an array if ID's.
    @Query("SELECT * " +
            "FROM " + Constants.TABLE_PRODUCT_COMM +
            " WHERE " + Constants.TABLE_PRODUCT_COMM_ID +
            " IN(:idArray)")
    LiveData<List<DmProdComm>> getProdCommsByIdArray(int[] idArray);

    // Gets a single DmProdComm by specifying its ID
    @Query("SELECT * " +
           "FROM " +
            Constants.TABLE_PRODUCT_COMM +
            " WHERE _id = :id")
    LiveData<DmProdComm> getProdCommById(int id);

    // Retrieves a single product by specifying its remote database reference
    @Query("SELECT * " +
            "FROM " +
            Constants.TABLE_PRODUCT_COMM +
            " WHERE " +
            Constants.PRODUCT_COMM_FB_REFERENCE_KEY + " = :remoteId")
    DmProdComm getProdCommByRemoteId(String remoteId);

    @Insert
    void insertProdComm(DmProdComm dmProdComm);

    @Insert
    void insertProdComms(List<DmProdComm> dmProdComm);

    @Update
    void updateProdComm(DmProdComm dmProdComm);

    @Delete
    void deleteProdComm(DmProdComm dmProdComm);

    @Query("DELETE FROM " + Constants.TABLE_PRODUCT_COMM)
    void deleteAllProdComms();
}
