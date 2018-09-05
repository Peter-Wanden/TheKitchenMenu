package com.example.peter.thekitchenmenu.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * This factory allows the passing in of variables to a ViewModel constructor
 */
public class ViewModelFactoryProducts
        extends
        ViewModelProvider.NewInstanceFactory {

    private static final String LOG_TAG = ViewModelFactoryProducts.class.getSimpleName();

    private String mUserId;

    public ViewModelFactoryProducts(String userId) {
        mUserId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ViewModelCatalogMyProducts(mUserId);
    }
}
