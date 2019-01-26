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
    private final TKMLocalDatabase database;
    private final SyncManager syncManager;

    private MediatorLiveDataActive<List<UsersProductData>> observableUsersProductData;
    private MediatorLiveDataActive<List<Product>> observableProducts;

    private Repository(final Context context, final TKMLocalDatabase database) {

        this.database = database;
        syncManager = new SyncManager(context);

        observableProducts = new MediatorLiveDataActive<>(this, Product.TAG);
        observableProducts.addSource(this.database.productDAO().getAll(),
                products -> {
            if (this.database.getDatabaseCreated().getValue() != null) {
                observableProducts.postValue(products);
            }
        });

        observableUsersProductData = new MediatorLiveDataActive<>(this, UsersProductData.TAG);
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

    public LiveData<List<Product>> getAllProducts() {
        return observableProducts;
    }

    public LiveData<List<UsersProductData>> getAllUserProductData() {
        return observableUsersProductData;
    }

    Product getProductByRemoteId(String remoteId) {
        return database.productDAO().getByRemoteId(remoteId);
    }

    UsersProductData getUserProductDataByRemoteId(String remoteId) {
        return database.userProductDataDAO().getByRemoteId(remoteId);
    }

    void insertAllProducts(List<Product> productsToInsert) {
        database.productDAO().insertAll(productsToInsert);
    }

    void insertAllUserProductData(List<UsersProductData> userProductDataToInsert) {
        database.userProductDataDAO().insertAll(userProductDataToInsert);
    }

    void updateProducts(List<Product> productsToUpdate) {
        database.productDAO().updateAll(productsToUpdate);
    }

    void updateUsersProductData(List<UsersProductData> usersProductDataToUpdate) {
        database.userProductDataDAO().updateAll(usersProductDataToUpdate);
    }

    public Cursor findProductsThatMatch(String searchQuery) {
        return database.productDAO().findProductsThatMatch(searchQuery);
    }
}
