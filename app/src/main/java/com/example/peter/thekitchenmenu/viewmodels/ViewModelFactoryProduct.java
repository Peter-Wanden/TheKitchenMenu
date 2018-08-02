package com.example.peter.thekitchenmenu.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.TKMDatabase;

/* Allows the passing of variables to the DAO */
public class ViewModelFactoryProduct extends ViewModelProvider.NewInstanceFactory {

    private final TKMDatabase mDb;
    private final int mProductId;

    public ViewModelFactoryProduct(TKMDatabase database, int productId){
        mDb = database;
        mProductId = productId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModelLocalProduct(mDb, mProductId);
    }
}
