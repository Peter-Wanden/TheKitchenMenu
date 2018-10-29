package com.example.peter.thekitchenmenu.repository;

import com.example.peter.thekitchenmenu.app.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class FirebaseReferences {

    /**
     * Returns a database reference that points to the current users MyProducts list
     * @param userId The user ID of the current user.
     * @return The Firebase real time database reference to the current users MyProducts location.
     */
    public static DatabaseReference getReferenceMyProducts(String userId) {

        return FirebaseDatabase
                .getInstance()
                .getReference(Constants.FB_COLLECTION_USERS)
                .child(userId)
                .child(Constants.FB_COLLECTION_PRODUCTS);
    }

    /**
     * Returns a database reference that points to the entire list of community products
     * @return a database reference pointing to the list of community products
     */
    public static DatabaseReference getReferenceCommunityProducts() {

        return FirebaseDatabase
                .getInstance()
                .getReference(Constants.FB_COLLECTION_PRODUCTS);
    }
}
