package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.database.Cursor;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {

    private static final String TAG = "tkm-Repository";
    private static Repository sInstance;
    private final TKMDatabase database;
    private final SyncManager syncManager;

    private MediatorLiveDataActive<List<ProductEntity>> observableProducts;
    private MediatorLiveDataActive<List<UsedProductEntity>> observableUsersProductData;

    private Repository(final Context context, final TKMDatabase database) {

        this.database = database;
        syncManager = new SyncManager(context);

//        observableProducts = new MediatorLiveDataActive<>(this, ProductEntity.TAG);
//        observableProducts.addSource(this.database.productEntityDao().getAll(), products -> {
//
//            if (this.database.getDatabaseCreated().getValue() != null) {
//                observableProducts.postValue(products);
//            }
//        });

        observableUsersProductData = new MediatorLiveDataActive<>(this,
                UsedProductEntity.TAG);

        observableUsersProductData.addSource(this.database.usedProductEntityDao().getAll(),
                usersProductData -> {

            if (this.database.getDatabaseCreated().getValue() != null) {
                observableUsersProductData.postValue(usersProductData);
            }
        });
    }

    public static Repository getInstance(Context context, final TKMDatabase database) {
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
//        Log.d(TAG, "observedStateChange: " + dataModel + " to: " + observedState);
        syncManager.setModelToSync(dataModel, observedState);
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        return observableProducts;
    }

    public LiveData<List<UsedProductEntity>> getAllUserProductData() {
        return observableUsersProductData;
    }

    void insertAllProducts(List<ProductEntity> productsToInsert) {
//        database.productEntityDao().insertAll(productsToInsert);
    }

    void insertAllUserProductData(List<UsedProductEntity> userProductDataToInsert) {
        database.usedProductEntityDao().insertAll(userProductDataToInsert);
    }

    void updateProducts(List<ProductEntity> productsToUpdate) {
//        database.productEntityDao().updateAll(productsToUpdate);
    }

    void updateUsersProductData(List<UsedProductEntity> usedProductEntityToUpdate) {
        database.usedProductEntityDao().updateAll(usedProductEntityToUpdate);
    }

    public Cursor findProductsThatMatch(String searchQuery) {
        return database.productEntityDao().findProductsThatMatch(searchQuery);
    }
}
