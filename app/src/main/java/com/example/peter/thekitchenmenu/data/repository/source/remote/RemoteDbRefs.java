package com.example.peter.thekitchenmenu.data.repository.source.remote;

import static com.example.peter.thekitchenmenu.app.Constants.FB_COLLECTION_FAVORITE_PRODUCTS;
import static com.example.peter.thekitchenmenu.app.Constants.REMOTE_USER_LOCATION;
import static com.example.peter.thekitchenmenu.app.Constants.REMOTE_PRODUCT_LOCATION;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class RemoteDbRefs {

    public static DatabaseReference getUserProductData(String remoteUserId) {
        return FirebaseDatabase.getInstance().getReference(REMOTE_USER_LOCATION).
                child(remoteUserId).child(REMOTE_PRODUCT_LOCATION);
    }

    public static DatabaseReference getUserProductData(String remoteUserId, String remoteProductId) {
        return getUserProductData(remoteUserId).
                child(remoteProductId);
    }

    public static DatabaseReference getRemoteProductData() {
        return FirebaseDatabase.getInstance().getReference(REMOTE_PRODUCT_LOCATION);
    }

    public static DatabaseReference getRemoteUsers() {
        return FirebaseDatabase.getInstance().getReference().child(REMOTE_USER_LOCATION);
    }

    public static DatabaseReference getUsersProducts() {
        return FirebaseDatabase.getInstance().getReference().child(FB_COLLECTION_FAVORITE_PRODUCTS);
    }
}
