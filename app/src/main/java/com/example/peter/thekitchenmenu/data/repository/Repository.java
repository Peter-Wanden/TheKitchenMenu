package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.databaseLocal.TKMLocalDatabase;
import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.entity.UsersProductData;

import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {

    private static final String TAG = "Repository";

    private static Repository sInstance;

    private final TKMLocalDatabase mDatabase;
    private final SyncManager mSyncManager;

    private MediatorLiveDataActive<List<UsersProductData>> mObservableProdMys;
    private MediatorLiveDataActive<List<Product>> mObservableProdComm;

    private Repository(final Context context, final TKMLocalDatabase database) {

        mDatabase = database;
        mSyncManager = new SyncManager(context);

        mObservableProdComm = new MediatorLiveDataActive<>(this, Product.TAG);
        mObservableProdComm.addSource(mDatabase.prodCommDAO().getAll(), prodComm -> {
            if (mDatabase.getDatabaseCreated().getValue() != null) {
                mObservableProdComm.postValue(prodComm);
            }
        });

        mObservableProdMys = new MediatorLiveDataActive<>(this, UsersProductData.TAG);
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

    public LiveData<List<Product>> getAllProdComm() {
        return mObservableProdComm;
    }

    public LiveData<List<UsersProductData>> getAllProdMys() {
        return mObservableProdMys;
    }

    Product getProdCommByRemoteId(String remoteId) {
        return mDatabase.prodCommDAO().getByRemoteId(remoteId);
    }

    UsersProductData getProdMyByRemoteId(String remoteId) {
        return mDatabase.prodMyDAO().getByRemoteId(remoteId);
    }

    void insertAllProdComm (List<Product> listProdComm) {
        mDatabase.prodCommDAO().insertAll(listProdComm);
    }

    void insertAllProdMy(List<UsersProductData> listProdMy) {
        mDatabase.prodMyDAO().insertAll(listProdMy);
    }

    void updateAllProdComm (List<Product> listProdComm) {
        mDatabase.prodCommDAO().updateAll(listProdComm);
    }

    void updateAllProdMy(List<UsersProductData> listProdMy) {
        mDatabase.prodMyDAO().updateAll(listProdMy);
    }

    public Cursor searchProducts(String query) {
        return mDatabase.prodCommDAO().searchAllProducts(query);
    }
}
