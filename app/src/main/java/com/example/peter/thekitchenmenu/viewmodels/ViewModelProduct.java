package com.example.peter.thekitchenmenu.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;

import java.util.List;

/**
 * View model for a single item
 */
public class ViewModelProduct extends ViewModel {

    private LiveData<Product> product;

    public ViewModelProduct(TKMDatabase database, int productId) {
        product = database.getProductDao().loadProductById(productId);
    }

    public LiveData<Product> getProduct() {
        return product;
    }
}


