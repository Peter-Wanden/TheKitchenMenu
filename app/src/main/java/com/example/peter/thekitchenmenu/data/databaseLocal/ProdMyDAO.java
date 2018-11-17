package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import static com.example.peter.thekitchenmenu.app.Constants.TABLE_PRODUCT_MY;
import static com.example.peter.thekitchenmenu.app.Constants.TABLE_PRODUCT_MY_ID;
import static com.example.peter.thekitchenmenu.app.Constants.PRODUCT_MY_FB_REFERENCE_KEY;
import static com.example.peter.thekitchenmenu.app.Constants.TABLE_PRODUCT_MY_COMM_ID;

import com.example.peter.thekitchenmenu.data.model.DmProdMy;

import java.util.List;

@Dao
public interface ProdMyDAO {

    // Gets all of DmProdMy object stored locally.
    @Query("SELECT * FROM " + TABLE_PRODUCT_MY)
    LiveData<List<DmProdMy>> getProdMys();

    // Gets a list of ProductMys given an array if ID's.
    @Query("SELECT * FROM " + TABLE_PRODUCT_MY + " WHERE " + TABLE_PRODUCT_MY_ID + " IN(:idArray)")
    LiveData<List<DmProdMy>> getProdMysByIdArray(int[] idArray);

    // Gets a single DmProdMy by specifying its ID
    @Query("SELECT * FROM " + TABLE_PRODUCT_MY + " WHERE " + TABLE_PRODUCT_MY_ID + " = :id")
    LiveData<DmProdMy> getProdMyById(int id);

    // Gets a single DmProdMy by specifying is remote database reference
    @Query("SELECT * FROM " + TABLE_PRODUCT_MY + " WHERE " + PRODUCT_MY_FB_REFERENCE_KEY + " = :remoteId")
    DmProdMy getProdMyByRemoteId(String remoteId);

    @Query("SELECT * FROM " + TABLE_PRODUCT_MY + " WHERE " + TABLE_PRODUCT_MY_COMM_ID + " = :commId")
    DmProdMy getProdMyByCommId(int commId);

    @Insert
    void insertProdMy(DmProdMy dmProdMy);

    @Insert
    void insertProdMys(List<DmProdMy> dmProdMIES);

    @Update
    void updateProdMy(DmProdMy dmProdMy);

    @Delete
    void deleteProdMy(DmProdMy dmProdMy);

    @Query("DELETE FROM " + TABLE_PRODUCT_MY)
    void deleteAllProdMys();
}
