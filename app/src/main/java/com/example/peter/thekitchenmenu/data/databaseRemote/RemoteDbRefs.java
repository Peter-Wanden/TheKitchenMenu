package com.example.peter.thekitchenmenu.data.databaseRemote;

import static com.example.peter.thekitchenmenu.app.Constants.FB_COLLECTION_USED_PRODUCTS;
import static com.example.peter.thekitchenmenu.app.Constants.FB_COLLECTION_USERS;
import static com.example.peter.thekitchenmenu.app.Constants.FB_COLLECTION_PRODUCTS;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class RemoteDbRefs {

    /**
     * @param remoteUserId The user ID of the current user.
     * @return a reference to /collection_users/[remoteUser_id]/collection_products/
     */
    public static DatabaseReference getRefProdMy(String remoteUserId) {

        return FirebaseDatabase.
                getInstance().
                getReference(FB_COLLECTION_USERS).
                child(remoteUserId).
                child(FB_COLLECTION_PRODUCTS);
    }

    /**
     * @param remoteUserId this users remote database user ID.
     * @param remoteProdRefKey the remote db product reference ID
     * @return a reference to /collection_users/[remoteUserId]/collection_products/[productID]
     */
    public static DatabaseReference getRefProdMy(String remoteUserId, String remoteProdRefKey) {
        return getRefProdMy(remoteUserId).
                child(remoteProdRefKey);
    }

    /**
     * Returns a database reference that points to the list of community products
     * @return a reference to /collection_products/
     */
    public static DatabaseReference getRefProdComm() {

        return FirebaseDatabase.
                getInstance().
                getReference(FB_COLLECTION_PRODUCTS);
    }

    /**
     * @return a reference to /collection_users/
     */
    public static DatabaseReference getRefUsers() {

        return FirebaseDatabase.
                getInstance().
                getReference().
                child(FB_COLLECTION_USERS);
    }

    /**
     * @return a reference to /collection_used_products/
     */
    public static DatabaseReference getRefUserProd() {

        return FirebaseDatabase.
                getInstance().
                getReference().
                child(FB_COLLECTION_USED_PRODUCTS);
    }

}
