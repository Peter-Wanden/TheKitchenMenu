package com.example.peter.thekitchenmenu.data.databaseLocal;

import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.TABLE_PROD_COMM_REMOTE_REF_ID;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.TABLE_PROD_MY;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.TABLE_PROD_MY_COMM_ID;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.TABLE_PROD_MY_ID;

import com.example.peter.thekitchenmenu.data.entity.DmProdMy;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProdMyDAO {

    // Gets all of DmProdMy object stored locally.
    @Query("SELECT * FROM " + TABLE_PROD_MY)
    LiveData<List<DmProdMy>> getAll();

    // Gets a list of ProductMys given an array if ID's.
    @Query("SELECT * FROM " + TABLE_PROD_MY +
            " WHERE " + TABLE_PROD_MY_ID + " IN(:idArray)")
    LiveData<List<DmProdMy>> getByIdArray(int[] idArray);

    // Gets a single DmProdMy by specifying its ID
    @Query("SELECT * FROM " + TABLE_PROD_MY +
            " WHERE " + TABLE_PROD_MY_ID + " = :id")
    LiveData<DmProdMy> getById(int id);

    // Gets a single DmProdMy by specifying is remote database reference
    @Query("SELECT * FROM " + TABLE_PROD_MY +
            " WHERE " + TABLE_PROD_COMM_REMOTE_REF_ID + " = :remoteId")
    DmProdMy getByRemoteId(String remoteId);

    @Query("SELECT * FROM " + TABLE_PROD_MY +
            " WHERE " + TABLE_PROD_MY_COMM_ID + " = :commId")
    DmProdMy getByCommId(int commId);

    @Insert
    void insert(DmProdMy dmProdMy);

    @Insert
    void insertAll(List<DmProdMy> listProdMy);

    @Update
    void update(DmProdMy dmProdMy);

    @Update
    void updateAll(List<DmProdMy> listDmProdMy);

    @Delete
    void delete(DmProdMy dmProdMy);

    @Query("DELETE FROM " + TABLE_PROD_MY)
    void deleteAll();
}
