package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.databaseLocal.TKMLocalDatabase;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;

import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {

    private static final String TAG = "Repository";
    private static Repository sInstance;
    private final TKMLocalDatabase database;
    private final SyncManager syncManager;

    private MediatorLiveDataActive<List<ProductUserDataEntity>> observableUsersProductData;
    private MediatorLiveDataActive<List<ProductEntity>> observableProducts;

    private Repository(final Context context, final TKMLocalDatabase database) {

        this.database = database;
        syncManager = new SyncManager(context);

        observableProducts = new MediatorLiveDataActive<>(this, ProductEntity.TAG);
        observableProducts.addSource(this.database.productDAO().getAll(),
                products -> {
            if (this.database.getDatabaseCreated().getValue() != null) {
                observableProducts.postValue(products);
            }
        });

        observableUsersProductData = new MediatorLiveDataActive<>(this, ProductUserDataEntity.TAG);
        observableUsersProductData.addSource(this.database.userProductDataDAO().getAll(),
                usersProductData -> {
            if (this.database.getDatabaseCreated().getValue() != null) {
                observableUsersProductData.postValue(usersProductData);
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
        syncManager.setModelToSync(dataModel, observedState);
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        return observableProducts;
    }

    public LiveData<List<ProductUserDataEntity>> getAllUserProductData() {
        return observableUsersProductData;
    }

    ProductEntity getProductByRemoteId(String remoteId) {
        return database.productDAO().getByRemoteId(remoteId);
    }

    ProductUserDataEntity getUserProductDataByRemoteId(String remoteId) {
        return database.userProductDataDAO().getByRemoteId(remoteId);
    }

    void insertAllProducts(List<ProductEntity> productsToInsert) {
        database.productDAO().insertAll(productsToInsert);
    }

    void insertAllUserProductData(List<ProductUserDataEntity> userProductDataToInsert) {
        database.userProductDataDAO().insertAll(userProductDataToInsert);
    }

    void updateProducts(List<ProductEntity> productsToUpdate) {
        database.productDAO().updateAll(productsToUpdate);
    }

    void updateUsersProductData(List<ProductUserDataEntity> productUserDataEntityToUpdate) {
        database.userProductDataDAO().updateAll(productUserDataEntityToUpdate);
    }

    public Cursor findProductsThatMatch(String searchQuery) {
        return database.productDAO().findProductsThatMatch(searchQuery);
    }
}
