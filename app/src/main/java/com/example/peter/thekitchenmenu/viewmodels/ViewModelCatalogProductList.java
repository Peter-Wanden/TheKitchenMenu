package com.example.peter.thekitchenmenu.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.peter.thekitchenmenu.AppExecutors;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.FirebaseQueryLiveData;
import com.example.peter.thekitchenmenu.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is made up of a combination of teh following:
 * See: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 * See: https://android.jlelse.eu/android-architecture-components-with-firebase-907b7699f6a0
 */
public class ViewModelCatalogProductList extends ViewModel {

    private static final String LOG_TAG = ViewModelCatalogProductList.class.getSimpleName();

    // A new list to hold products as they are de-serialized
    private List<Product> productList = new ArrayList<>();

    // Get a reference to the product collection
    private static final DatabaseReference productReference =
            FirebaseDatabase
            .getInstance()
            .getReference(Constants.FB_PRODUCT_REFERENCE);

    private final MediatorLiveData<List<Product>> productLiveData = new MediatorLiveData<>();

    public ViewModelCatalogProductList() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into Product objects
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(productReference);
        Log.e(LOG_TAG, "ViewModelCatalogProductList() called.");

        productLiveData.addSource(liveData, snapshot -> {
            if(snapshot != null) {
                AppExecutors.getInstance().networkIO().execute(() -> {
                    int loopValue = 0;
                    for(DataSnapshot shot : snapshot.getChildren()) {
                        Product product = shot.getValue(Product.class);
                        if(!productList.contains(product)){
                            Log.e(LOG_TAG, "");
                            loopValue ++;
                            productList.add(product);
                        }
                    }
                    productLiveData.postValue(productList);
                    Log.e(LOG_TAG, "productLiveData.postValue(productList) has: " + productList.size() + " items" );
                });
            } else {
                productLiveData.setValue(null);
            }
        });
    }

    // Fetches the generated list of products
    public LiveData<List<Product>> getProductsLiveData() {
        Log.e(LOG_TAG, "getProductsLiveData() - has: " + productList.size() + " items.");
        return productLiveData;
    }
}
