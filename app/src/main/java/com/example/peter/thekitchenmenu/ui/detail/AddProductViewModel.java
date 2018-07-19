package com.example.peter.thekitchenmenu.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;

/**
 * View model for a single product
 */
public class AddProductViewModel extends ViewModel{

    private LiveData<Product> product;

    public AddProductViewModel(TKMDatabase mDb, int mProductId) {
        product = mDb.productDAO().loadProductById(mProductId);
    }

    public LiveData<Product> getProduct() {
        return product;
    }
}
