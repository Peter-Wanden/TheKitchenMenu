package com.example.peter.thekitchenmenu.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.databaseLocal.ProdCommDAO;
import com.example.peter.thekitchenmenu.data.databaseLocal.ProdMyDAO;
import com.example.peter.thekitchenmenu.data.databaseLocal.TKMLocalDatabase;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.data.model.ProductMy;

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
    private LiveData<List<ProductCommunity>> mGetAllProdComms;
    private LiveData<List<ProductCommunity>> mGetProdCommsByIdArray;
    private LiveData<List<ProductMy>> mGetProdMys;
    private LiveData<List<ProductMy>> mGetProdMysByIdArray;

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
    //                        ProductCommunity                      //
    // -------------------------------------------------------------//

    /**
     * Gets a list of all data model objects in the product_community table
     * @return a complete list of all {@link ProductCommunity} objects ordered by description.
     */
    LiveData<List<ProductCommunity>> getAllProdComms() {
        mGetAllProdComms = mProdCommDAO.getAllProdComms();
        return mGetAllProdComms;
    }

    /**
     * Gets a list of {@link ProductCommunity} objects given an array of ID's
     * @param idArray an integer array containing a list of id's
     * @return a list of {@link ProductCommunity} objects
     */
    LiveData<List<ProductCommunity>> getProdCommsByIdArray(int[] idArray) {
        mGetProdCommsByIdArray = mProdCommDAO.getProdCommsByIdArray(idArray);
        return mGetProdCommsByIdArray;
    }

    /**
     * Gets a single {@link ProductCommunity} by ID
     * @param id the ID of the {@link ProductCommunity} to retrieve.
     * @return the requested object, or null if not found.
     */
    LiveData<ProductCommunity> getProdCommById(int id) {
        return mProdCommDAO.getProdCommById(id);
    }

    /**
     * Gets a {@link ProductCommunity} by its remote ID
     * @param remoteId the remote ID to fetch.
     * @return an {@link ProductCommunity}.
     */
    ProductCommunity getProdCommByRemoteId(String remoteId) {
        return mProdCommDAO.getProdCommByRemoteId(remoteId);
    }

    /**
     * Inserts a single {@link ProductCommunity} object into the database
     * @param prodComm the object to be inserted.
     */
    void insertProdComm(ProductCommunity prodComm) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.insertProdComm(prodComm));
    }

    /**
     * Inserts a List of {@link ProductCommunity} objects into the database
     * @param prodComm the list of objects to be inserted.
     */
    public void insertProdComms(List<ProductCommunity> prodComm) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.insertProdComms(prodComm));
    }

    /**
     * Inserts an {@link ProductCommunity} object into the database
     * @param prodComm the object to insert.
     */
    void updateProdComm(ProductCommunity prodComm) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.updateProdComm(prodComm));
    }

    /**
     * Deletes a single {@link ProductCommunity} object from the local database
     * @param prodComm the {@link ProductCommunity} object to delete.
     */
    public void deleteProdComm(ProductCommunity prodComm) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.deleteProdComm(prodComm));
    }

    /**
     * Deletes all {@link ProductCommunity} objects from the local database
     */
    public void deleteAllProdComms() {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdCommDAO.deleteAllProdComms());
    }

    /**
     * Synchronises remote and local {@link ProductCommunity} objects.
     * @param remoteProdComm the remote product to sync.
     */
    void remoteSyncProdComm(ProductCommunity remoteProdComm) {
        // Sends the remote product to be synchronised with the local database.
        SyncProdComm sPC = new SyncProdComm(this, remoteProdComm);
        sPC.matchAndSync();
    }

    // -------------------------------------------------------------//
    //                        ProductMy                             //
    // -------------------------------------------------------------//

    /**
     * Gets a list of all {@link ProductMy} objects in the product_my table
     * @return a list of all {@link ProductMy} objects.
     */
    LiveData<List<ProductMy>> getAllProdMys() {
        mGetProdMys = mProdMyDAO.getProdMys();
        return mGetProdMys;
    }

    /**
     * Gets a list of {@link ProductMy} objects given an array of ID's
     * @param idArray an integer array containing a list of id's
     * @return a list of {@link ProductMy} objects
     */
    LiveData<List<ProductMy>> getProdMysByIdArray(int[] idArray) {
        mGetProdMysByIdArray = mProdMyDAO.getProdMysByIdArray(idArray);
        return mGetProdMysByIdArray;
    }

    /**
     * Gets a single {@link ProductMy} by ID
     * @param id the ID of the {@link ProductMy} to retrieve.
     * @return the requested object, or null if not found.
     */
    LiveData<ProductMy> getProdMyById(int id) {
        return mProdMyDAO.getProdMyById(id);
    }

    /**
     * Gets a {@link ProductMy} by its remote ID
     * @param remoteId the remote ID to fetch.
     * @return an {@link ProductMy}.
     */
    ProductMy getProdMyByRemoteId(String remoteId) {
        return mProdMyDAO.getProdMyByRemoteId(remoteId);
    }

    /**
     * Inserts a single {@link ProductMy} object into the database
     * @param prodMy the object to be inserted.
     */
    void insertProdMy(ProductMy prodMy) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.insertProdMy(prodMy));
    }

    /**
     * Inserts a List of {@link ProductMy} objects into the database
     * @param prodMy the list of objects to be inserted.
     */
    public void insertProdMy(List<ProductMy> prodMy) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.insertProdMys(prodMy));
    }

    /**
     * Inserts an {@link ProductMy} object into the database
     * @param prodMy the object to insert.
     */
    void updateProdMy(ProductMy prodMy) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.updateProdMy(prodMy));
    }

    /**
     * Deletes a single {@link ProductMy} object from the local database
     * @param prodMy the {@link ProductMy} object to delete.
     */
    public void deleteProdMy(ProductMy prodMy) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.deleteProdMy(prodMy));
    }

    /**
     * Deletes all {@link ProductMy} objects from the local database
     */
    public void deleteAllProdMys() {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProdMyDAO.deleteAllProdMys());
    }

    /**
     * Synchronises remote and local {@link ProductMy} objects.
     * @param remoteProdMy the remote product to sync.
     */
    void remoteSyncProdMy(ProductMy remoteProdMy) {
        SyncProdMy sPm = new SyncProdMy(this, remoteProdMy);
        sPm.matchAndSync();
    }
}
