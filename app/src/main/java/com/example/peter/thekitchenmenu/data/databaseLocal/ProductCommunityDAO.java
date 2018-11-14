package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;

import java.util.List;

@Dao
public interface ProductCommunityDAO {

    // Gets all ProductsCommunity objects stored locally.
    @Query("SELECT * " +
            "FROM "+ Constants.TABLE_PRODUCT_COMM +
            " ORDER BY " + Constants.TABLE_PRODUCT_COMM_DESCRIPTION)
    LiveData<List<ProductCommunity>> getAllProdComms();

    // Gets a list of CommunityProducts given an array if ID's.
    @Query("SELECT * " +
            "FROM " + Constants.TABLE_PRODUCT_COMM +
            " WHERE " + Constants.TABLE_PRODUCT_COMM_ID +
            " IN(:idArray)")
    LiveData<List<ProductCommunity>> getProdCommsByIdArray(int[] idArray);

    // Gets a single ProductCommunity by specifying its ID
    @Query("SELECT * " +
           "FROM " +
            Constants.TABLE_PRODUCT_COMM +
            " WHERE _id = :id")
    LiveData<ProductCommunity> getProdCommById(int id);

    // Retrieves a single product by specifying its remote database reference
    @Query("SELECT * " +
            "FROM " +
            Constants.TABLE_PRODUCT_COMM +
            " WHERE " +
            Constants.PRODUCT_COMM_FB_REFERENCE_KEY + " = :remoteId")
    ProductCommunity getProdCommByRemoteId(String remoteId);

    @Insert
    void insertProdComm(ProductCommunity prodComm);

    @Insert
    void insertProdComms(List<ProductCommunity> prodComm);

    @Update
    void updateProdComm(ProductCommunity prodComm);

    @Delete
    void deleteProdComm(ProductCommunity prodComm);

    @Query("DELETE FROM " + Constants.TABLE_PRODUCT_COMM)
    void deleteAllProdComms();
}
