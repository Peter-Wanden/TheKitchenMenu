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
public class ViewModelCatalogCommunityProductList extends ViewModel {

    private static final String LOG_TAG =
            ViewModelCatalogCommunityProductList.class.getSimpleName();

    private String mUserId;

    // A new list to hold the community product list as they are de-serialized.
    private List<Product> communityProductList = new ArrayList<>();

    // Get a reference to the product collection
    private static final DatabaseReference productReference =
            FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FB_COLLECTION_PRODUCTS);

    private final MediatorLiveData<List<Product>> communityProductLiveData = new MediatorLiveData<>();

    public ViewModelCatalogCommunityProductList(String userId) {

        // Keeps a copy of the user ID throughout the owners lifecycle.
        mUserId = userId;

        // Set up the MediatorLiveData to convert DataSnapshot objects into Product objects.
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(productReference
                .orderByChild(Constants.PRODUCT_BASE_DESCRIPTION_KEY));

        AppExecutors.getInstance().networkIO().execute(() ->
                communityProductLiveData.addSource(liveData, snapshot -> {
                    if (snapshot != null && snapshot.exists()) {

                        // This is a snapshot of the entire collection at reference 'products'.
                        // Clear out the old data.
                        communityProductList.clear();

                        // Loop through and add the new data.
                        for (DataSnapshot shot : snapshot.getChildren()) {

                            // Get the product values
                            Product product = shot.getValue(Product.class);

                            if (product != null) {
                                product.setFbProductReferenceKey(shot.getKey());
                            }

                            // Add the product to the list
                            communityProductList.add(product);
                        }

                        // Post the new data to LiveData
                        communityProductLiveData.postValue(communityProductList);

                    } else {

                        communityProductLiveData.setValue(null);
                    }
                }));
    }

    // Fetches the generated list of products
    public LiveData<List<Product>> getProductsLiveData() {
        return communityProductLiveData;
    }

    // Getters and setters
    public String getUserId() {
        return mUserId;
    }
    public void setUserId(String userId) {
        mUserId = userId;
    }
}
