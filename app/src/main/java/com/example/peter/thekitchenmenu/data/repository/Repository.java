package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.databaseLocal.TKMLocalDatabase;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;

import java.util.List;

import androidx.lifecycle.LiveData;

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

        mObservableProdComm = new MediatorLiveDataActive<>(this, DmProdComm.TAG);
        mObservableProdComm.addSource(mDatabase.prodCommDAO().getAll(), prodComm -> {
            if (mDatabase.getDatabaseCreated().getValue() != null) {
                mObservableProdComm.postValue(prodComm);
            }
        });

        mObservableProdMys = new MediatorLiveDataActive<>(this, DmProdMy.TAG);
        mObservableProdMys.addSource(mDatabase.prodMyDAO().getAll(), prodMy -> {
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

    void observedStateChange(String dataModel, boolean observedState) {
        Log.d(TAG, "observedStateChange: " + dataModel + " to: " + observedState);
        mSyncManager.setModelToSync(dataModel, observedState);
    }

    public LiveData<List<DmProdComm>> getAllProdComm() {
        return mObservableProdComm;
    }

    public LiveData<List<DmProdMy>> getAllProdMys() {
        return mObservableProdMys;
    }

    DmProdComm getProdCommByRemoteId(String remoteId) {
        return mDatabase.prodCommDAO().getByRemoteId(remoteId);
    }

    DmProdMy getProdMyByRemoteId(String remoteId) {
        return mDatabase.prodMyDAO().getByRemoteId(remoteId);
    }

    void insertAllProdComm (List<DmProdComm> listProdComm) {
        mDatabase.prodCommDAO().insertAll(listProdComm);
    }

    void insertAllProdMy(List<DmProdMy> listProdMy) {
        mDatabase.prodMyDAO().insertAll(listProdMy);
    }

    void updateAllProdComm (List<DmProdComm> listProdComm) {
        mDatabase.prodCommDAO().updateAll(listProdComm);
    }

    void updateAllProdMy(List<DmProdMy> listProdMy) {
        mDatabase.prodMyDAO().updateAll(listProdMy);
    }

    public Cursor searchProducts(String query) {
        return mDatabase.prodCommDAO().searchAllProducts(query);
    }
}
