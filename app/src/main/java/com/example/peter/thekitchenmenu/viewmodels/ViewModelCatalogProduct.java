package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;

import java.util.List;

public class ViewModelCatalogProduct extends AndroidViewModel {

    /* Instantiate an object for the data we want to cache */
    private LiveData<List<Product>> products;

    public ViewModelCatalogProduct(@NonNull Application application) {
        super(application);

        /* Initialise member variable */
        TKMDatabase database = TKMDatabase.getInstance(this.getApplication());
        products = database.getProductDao().loadProducts();
    }

    /* Getter for our live data object */
    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
