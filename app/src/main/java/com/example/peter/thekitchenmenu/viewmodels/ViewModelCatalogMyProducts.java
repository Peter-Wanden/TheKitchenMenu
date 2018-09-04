package com.example.peter.thekitchenmenu.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

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
 * This class is made up of a combination of the following:
 * See: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 * See: https://android.jlelse.eu/android-architecture-components-with-firebase-907b7699f6a0
 */
public class ViewModelCatalogMyProducts extends ViewModel {

    private static final String LOG_TAG =
            ViewModelCatalogMyProducts.class.getSimpleName();

    private String mUserId;

    // A new list to hold the users used products as they are de-serialized
    private List<Product> myProductList = new ArrayList<>();

    private final MediatorLiveData<List<Product>> myProductLiveData = new MediatorLiveData<>();

    public ViewModelCatalogMyProducts(String userId) {

        // Set the user ID before anything else as its needed for the database reference
        mUserId = userId;

        // Get a reference to the users product collection
        DatabaseReference productUsersReference =
                FirebaseDatabase
                        .getInstance()
                        .getReference(Constants.FB_COLLECTION_USERS)
                        .child(mUserId)
                        .child(Constants.FB_COLLECTION_PRODUCTS);

        // Set up the MediatorLiveData to convert DataSnapshot objects into Product objects
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(productUsersReference
                .orderByChild(Constants.PRODUCT_BASE_DESCRIPTION_KEY));

        AppExecutors.getInstance().networkIO().execute(() ->
                myProductLiveData.addSource(liveData, snapshot -> {

                    if (snapshot != null && snapshot.exists()) {

                        // This is a snapshot of the entire collection at reference
                        // /collection/users/[user ID]/collection_products/.
                        // Clear out the old data
                        myProductList.clear();

                        // Loop through and add the new data
                        for (DataSnapshot shot : snapshot.getChildren()) {

                            // Get the product values
                            Product product = shot.getValue(Product.class);

                            if (product != null) {
                                product.setFbProductReferenceKey(shot.getKey());
                            }
                            // Add the product to the list
                            myProductList.add(product);
                        }
                        // Post the new data to LiveData
                        myProductLiveData.postValue(myProductList);

                    } else {

                        myProductLiveData.setValue(null);
                    }
                }));
    }

    // Fetches the generated list of products
    public LiveData<List<Product>> getProductsLiveData() {
        return myProductLiveData;
    }

    // Getters and setters
    public String getUserId() {
        return mUserId;
    }
    public void setUserId(String userId) {
        mUserId = userId;
    }
}
