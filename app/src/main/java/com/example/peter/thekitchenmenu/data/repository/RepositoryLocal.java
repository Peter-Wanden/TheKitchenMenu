package com.example.peter.thekitchenmenu.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.databaseLocal.ProdCommDAO;
import com.example.peter.thekitchenmenu.data.databaseLocal.ProdMyDAO;
import com.example.peter.thekitchenmenu.data.databaseLocal.TKMLocalDatabase;
import com.example.peter.thekitchenmenu.data.model.DmProdComm;
import com.example.peter.thekitchenmenu.data.model.DmProdMy;

import java.util.List;

/**
 * Manages access to the data models for the local database
 */
public class RepositoryLocal {

    private static final String LOG_TAG = RepositoryLocal.class.getSimpleName();

    // Data access objects
    ProdCommDAO mProdCommDAO;
    ProdMyDAO mProdMyDAO;

    // Data list members
    private LiveData<List<DmProdComm>> mGetAllProdComms;
    private LiveData<List<DmProdComm>> mGetProdCommsByIdArray;
    private LiveData<List<DmProdMy>> mGetProdMys;
    private LiveData<List<DmProdMy>> mGetProdMysByIdArray;
    private LiveData<DmProdMy> mGetProdMyById;

    // Constructor
    RepositoryLocal(Application application) {
        // Gets an instance of the local Room database.
        TKMLocalDatabase mDb = TKMLocalDatabase.getInstance(application);
        // Gets a reference to the 'community products' DAO
        mProdCommDAO = mDb.productCommunityDAO();
        // Gets a reference to the 'my products' DAO
        mProdMyDAO = mDb.productMyDAO();
    }

    // -------------------------------------------------------------//
    //                        DmProdComm                      //
    // -------------------------------------------------------------//

    /**
     * Gets a list of all data model objects in the product_community table
     * @return a complete list of all {@link DmProdComm} objects ordered by description.
     */
    LiveData<List<DmProdComm>> getAllProdComms() {
        mGetAllProdComms = mProdCommDAO.getAllProdComms();
        return mGetAllProdComms;
    }

    /**
     * Gets a list of {@link DmProdComm} objects given an array of ID's
     * @param idArray an integer array containing a list of id's
     * @return a list of {@link DmProdComm} objects
     */
    LiveData<List<DmProdComm>> getProdCommsByIdArray(int[] idArray) {
        mGetProdCommsByIdArray = mProdCommDAO.getProdCommsByIdArray(idArray);
        return mGetProdCommsByIdArray;
    }

    /**
     * Gets a single {@link DmProdComm} by ID
     * @param id the ID of the {@link DmProdComm} to retrieve.
     * @return the requested object, or null if not found.
     */
    LiveData<DmProdComm> getProdCommById(int id) {
        return mProdCommDAO.getProdCommById(id);
    }

    /**
     * Gets a {@link DmProdComm} by its remote ID
     * @param remoteId the remote ID to fetch.
     * @return an {@link DmProdComm}.
     */
    DmProdComm getProdCommByRemoteId(String remoteId) {
        return mProdCommDAO.getProdCommByRemoteId(remoteId);
    }

    /**
     * Inserts a single {@link DmProdComm} object into the database
     * @param dmProdComm the object to be inserted.
     */
    void insertProdComm(DmProdComm dmProdComm) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.insertProdComm(dmProdComm));
    }

    /**
     * Inserts a List of {@link DmProdComm} objects into the database
     * @param dmProdComm the list of objects to be inserted.
     */
    public void insertProdComms(List<DmProdComm> dmProdComm) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.insertProdComms(dmProdComm));
    }

    /**
     * Inserts an {@link DmProdComm} object into the database
     * @param dmProdComm the object to insert.
     */
    void updateProdComm(DmProdComm dmProdComm) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.updateProdComm(dmProdComm));
    }

    /**
     * Deletes a single {@link DmProdComm} object from the local database
     * @param dmProdComm the {@link DmProdComm} object to delete.
     */
    public void deleteProdComm(DmProdComm dmProdComm) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.deleteProdComm(dmProdComm));
    }

    /**
     * Deletes all {@link DmProdComm} objects from the local database
     */
    public void deleteAllProdComms() {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.deleteAllProdComms());
    }

    /**
     * Synchronises remote and local {@link DmProdComm} objects.
     * @param remoteDmProdComm the remote product to sync.
     */
    void remoteSyncProdComm(DmProdComm remoteDmProdComm) {
        // Sends the remote product to be synchronised with the local database.
        SyncProdComm sPC = new SyncProdComm(this, remoteDmProdComm);
        sPC.matchAndSync();
    }

    // -------------------------------------------------------------//
    //                        DmProdMy                             //
    // -------------------------------------------------------------//

    /**
     * Gets a list of all {@link DmProdMy} objects in the product_my table
     * @return a list of all {@link DmProdMy} objects.
     */
    LiveData<List<DmProdMy>> getAllProdMys() {
        mGetProdMys = mProdMyDAO.getProdMys();
        return mGetProdMys;
    }

    /**
     * Gets a list of {@link DmProdMy} objects given an array of ID's
     * @param idArray an integer array containing a list of id's
     * @return a list of {@link DmProdMy} objects
     */
    LiveData<List<DmProdMy>> getProdMysByIdArray(int[] idArray) {
        mGetProdMysByIdArray = mProdMyDAO.getProdMysByIdArray(idArray);
        return mGetProdMysByIdArray;
    }

    /**
     * Gets a single {@link DmProdMy} by ID
     * @param id the ID of the {@link DmProdMy} to retrieve.
     * @return the requested object, or null if not found.
     */
    LiveData<DmProdMy> getProdMyById(int id) {
        mGetProdMyById = mProdMyDAO.getProdMyById(id);
        return mGetProdMyById;
    }

    /**
     * Gets a {@link DmProdMy} by its remote ID
     * @param remoteId the remote ID to fetch.
     * @return an {@link DmProdMy}.
     */
    DmProdMy getProdMyByRemoteId(String remoteId) {
        return mProdMyDAO.getProdMyByRemoteId(remoteId);
    }

    DmProdMy getProdMyByCommId(int commId) {
        return mProdMyDAO.getProdMyByCommId(commId);
    }

    /**
     * Inserts a single {@link DmProdMy} object into the database
     * @param dmProdMy the object to be inserted.
     */
    void insertProdMy(DmProdMy dmProdMy) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.insertProdMy(dmProdMy));
    }

    /**
     * Inserts a List of {@link DmProdMy} objects into the database
     * @param dmProdMy the list of objects to be inserted.
     */
    public void insertProdMy(List<DmProdMy> dmProdMy) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.insertProdMys(dmProdMy));
    }

    /**
     * Inserts an {@link DmProdMy} object into the database
     * @param dmProdMy the object to insert.
     */
    void updateProdMy(DmProdMy dmProdMy) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.updateProdMy(dmProdMy));
    }

    /**
     * Deletes a single {@link DmProdMy} object from the local database
     * @param dmProdMy the {@link DmProdMy} object to delete.
     */
    public void deleteProdMy(DmProdMy dmProdMy) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.deleteProdMy(dmProdMy));
    }

    /**
     * Deletes all {@link DmProdMy} objects from the local database
     */
    public void deleteAllProdMys() {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.deleteAllProdMys());
    }

    /**
     * Synchronises remote and local {@link DmProdMy} objects.
     * @param remoteDmProdMy the remote product to sync.
     */
    void remoteSyncProdMy(DmProdMy remoteDmProdMy) {
        SyncProdMy sPm = new SyncProdMy(this, remoteDmProdMy);
        sPm.matchAndSync();
    }
}
