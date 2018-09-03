package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

// This factory allows the passing of variables to a view model constructor
public class ViewModelFactoryProducts
        extends
        ViewModelProvider.NewInstanceFactory {

    private static final String LOG_TAG = ViewModelFactoryProducts.class.getSimpleName();

    private String mUserId;

    public ViewModelFactoryProducts(String userId) {
        mUserId = userId;
        Log.e(LOG_TAG, "ViewModelFactoryProducts constructor reports UsedId: " + mUserId);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModelCatalogProductUsedList(mUserId);
    }
}
