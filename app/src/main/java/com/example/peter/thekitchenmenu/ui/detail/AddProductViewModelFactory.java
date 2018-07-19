package com.example.peter.thekitchenmenu.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.TKMDatabase;

public class AddProductViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final TKMDatabase mDb;
    private final int mProductId;


    public AddProductViewModelFactory(TKMDatabase mDb, int mProductId) {
        this.mDb = mDb;
        this.mProductId = mProductId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddProductViewModel(mDb, mProductId);
    }
}
