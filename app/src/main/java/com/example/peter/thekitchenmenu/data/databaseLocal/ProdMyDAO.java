package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ProductMy;

import java.util.List;

@Dao
public interface ProdMyDAO {

    // Gets all of ProductMy object stored locally.
    @Query("SELECT " +
            "* " +
            "FROM " +
            Constants.TABLE_PRODUCT_MY)
    LiveData<List<ProductMy>> getProdMys();

    // Gets a list of ProductMys given an array if ID's.
    @Query("SELECT * " +
            "FROM " + Constants.TABLE_PRODUCT_MY +
            " WHERE " + Constants.TABLE_PRODUCT_MY_ID +
            " IN(:idArray)")
    LiveData<List<ProductMy>> getProdMysByIdArray(int[] idArray);

    // Gets a single ProductMy by specifying its ID
    @Query("SELECT * " +
            "FROM " + Constants.TABLE_PRODUCT_MY +
            " WHERE " + Constants.TABLE_PRODUCT_MY_ID + " = :id")
    LiveData<ProductMy> getProdMyById(int id);

    // Gets a single ProductMy by specifying is remote database reference
    @Query("SELECT * " +
            "FROM " + Constants.TABLE_PRODUCT_MY + " " +
            "WHERE " + Constants.PRODUCT_MY_FB_REFERENCE_KEY + " = :remoteId")
    ProductMy getProdMyByRemoteId(String remoteId);

    @Insert
    void insertProdMy(ProductMy prodMy);

    @Insert
    void insertProdMys(List<ProductMy> prodMys);

    @Update
    void updateProdMy(ProductMy prodMy);

    @Delete
    void deleteProdMy(ProductMy prodMy);

    @Query("DELETE FROM " + Constants.TABLE_PRODUCT_MY)
    void deleteAllProdMys();
}
