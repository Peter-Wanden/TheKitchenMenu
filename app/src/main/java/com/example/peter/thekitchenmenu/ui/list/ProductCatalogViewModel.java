package com.example.peter.thekitchenmenu.ui.list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;

import java.util.List;

public class ProductCatalogViewModel extends AndroidViewModel {

    public static final String LOG_TAG = ProductCatalogViewModel.class.getSimpleName();

    /* Instantiate an object for the data we want to cache */
    private LiveData<List<Product>> products;

    public ProductCatalogViewModel(@NonNull Application application) {
        super(application);

        /* Initialise member variable */
        TKMDatabase database = TKMDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Actively retrieving the products from the DataBase");
        products = database.productDAO().loadProducts();
    }

    /* Getter for our live data object */
    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
