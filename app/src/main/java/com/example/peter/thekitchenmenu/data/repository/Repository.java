package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.databaseLocal.TKMLocalDatabase;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * The 'single source of truth' for the app data
 */
public class Repository {

    private static final String TAG = "Repository";

    private static Repository sInstance;

    private final TKMLocalDatabase mDatabase;
    private final SyncManager mSyncManager;

    private MediatorLiveDataActive<List<DmProdMy>> mObservableProdMys;
    private MediatorLiveDataActive<List<DmProdComm>> mObservableProdComm;

    private Repository(final Context context, final TKMLocalDatabase database) {

        mDatabase = database;
        mSyncManager = new SyncManager(context);

        // Community Products
        mObservableProdComm = new MediatorLiveDataActive<>(this, DmProdComm.TAG);

        mObservableProdComm.addSource(mDatabase.prodCommDAO().getAll(), prodComm ->
        {
            if (mDatabase.getDatabaseCreated().getValue() != null) {
                mObservableProdComm.postValue(prodComm);
            }
        });

        // My Products
        mObservableProdMys = new MediatorLiveDataActive<>(this, DmProdMy.TAG);

        mObservableProdMys.addSource(mDatabase.prodMyDAO().getAll(), prodMy ->
        {
            if (mDatabase.getDatabaseCreated().getValue() != null) {
                mObservableProdMys.postValue(prodMy);
            }
        });
    }

    public static Repository getInstance(Context context, final TKMLocalDatabase database) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(context, database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Activates / deactivates remote synchronisation for a data model and its dependents.
     * @param dataModel the data model to synchronise
     * @param isActive  true turns remote synchronisation on, false turns it off.
     */
    void dataModelIsActive(String dataModel, boolean isActive) {
        mSyncManager.setTargetObservedState(dataModel, isActive);
    }

    /**
     * @return a list of all {@link DmProdComm} objects.
     */
    public LiveData<List<DmProdComm>> getAllProdComm() {
        return mObservableProdComm;
    }

    /**
     * @return a list of all {@link DmProdMy} objects.
     */
    public LiveData<List<DmProdMy>> getAllProdMys() {
        return mObservableProdMys;
    }

    /**
     * @param remoteId the remote ID of the object to fetch.
     * @return a DmProdComm object.
     */
    DmProdComm getProdCommByRemoteId(String remoteId) {
        return mDatabase.prodCommDAO().getByRemoteId(remoteId);
    }

    /**
     * @param remoteId the remote ID of the object to fetch.
     * @return a DmProdMy object.
     */
    DmProdMy getProdMyByRemoteId(String remoteId) {
        return mDatabase.prodMyDAO().getByRemoteId(remoteId);
    }

    /**
     * @param dmProdComm the object to be inserted.
     */
    void insertProdComm(DmProdComm dmProdComm) {
        mDatabase.prodCommDAO().insert(dmProdComm);
    }

    /**
     * @param prodMy the object to be inserted.
     */
    void insertProdMy(DmProdMy prodMy) {
        mDatabase.prodMyDAO().insert(prodMy);
    }

    /**
     * @param prodComm the object to update.
     */
    void updateProdComm(DmProdComm prodComm) {
        mDatabase.prodCommDAO().update(prodComm);
    }

    /**
     * @param idList an array of id's to fetch.
     * @return a list of {@link DmProdComm} where id's match the id's in the input array.
     */
    public LiveData<List<DmProdComm>> getProdCommByIdArray(int[] idList) {
        return mDatabase.prodCommDAO().getByIdArray(idList);
    }

    /**
     * @param id the id of the {@link DmProdComm} object to return.
     * @return the requested object.
     */
    public LiveData<DmProdComm> getProdCommById(int id) {
        return mDatabase.prodCommDAO().getById(id);
    }

    /**
     * @param id the local database id of the object to return
     * @return the requested DmProdMy object.
     */
    public LiveData<DmProdMy> getProdMyById(int id) {
        return mDatabase.prodMyDAO().getById(id);
    }

    public DmProdMy getProdMyByCommId(int commId) {
        return mDatabase.prodMyDAO().getByCommId(commId);
    }

    /**
     * @param pm the object to update.
     */
    void updateProdMy(DmProdMy pm) {
        mDatabase.prodMyDAO().update(pm);
    }

    public Cursor searchProducts(String query) {
        return mDatabase.prodCommDAO().searchAllProducts(query);
    }
}
