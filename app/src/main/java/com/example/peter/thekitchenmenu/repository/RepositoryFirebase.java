package com.example.peter.thekitchenmenu.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.peter.thekitchenmenu.model.ProductCommunity;
import com.example.peter.thekitchenmenu.model.ProductMy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Manages data access for the remote database
 */
class RepositoryFirebase {

    private static final String LOG_TAG = RepositoryFirebase.class.getSimpleName();

    // A reference to the repository so that all communications with the app go through its
    // repository instance.
    private Repository mRepository;
    // Manages the listeners connection to the ProductCommunity remote data
    private DataListenerPending mCommunityProductsListener;
    // Manages the listeners connection to the ProductMy remote data
    private DataListenerPending mMyProductsListener;

    /**
     * Constructor
     * @param repository an instance reference to the Repository
     */
    public RepositoryFirebase(Repository repository) {

        // Reference to the repository
        this.mRepository = repository;

        // Database reference to the community products location in Firebase.
        DatabaseReference productCommunityReference =
                FirebaseReferences.getReferenceCommunityProducts();

        // Listens to changes in the remote database and reflects them into the local database.
        ValueEventListener communityProductVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    ProductCommunity productCommunity = shot.getValue(ProductCommunity.class);

                    if (productCommunity != null) {
                        productCommunity.setFbProductReferenceKey(shot.getKey());
                        mRepository.synchroniseCommunityProduct(productCommunity);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "Unable to update community products, with error: "
                        + databaseError);
            }
        };
        mCommunityProductsListener = new DataListenerPending(
                productCommunityReference, communityProductVEL);
    }

    /**
     * Adds or removes a ValueEventListener to the ProductCommunity reference in Firebase.
     * @param activeState true to add the lister, false to remove it.
     */
    void isLiveProductCommunityList(boolean activeState) {
        // Log.e(LOG_TAG, "FIREBASE - Product listener is: " + activeState);
        mCommunityProductsListener.changeListenerState(activeState);
    }

    /**
     * Sets up the remote collection_users/{user_id}/collection_products/ location ready for
     * synchronisation.
     * Receives and converts the data at the specified location to a local database object.
     * Sends the data to be processed by the repository.
     * @param mUserId the unique Firebase user ID.
     */
    private void initialiseProductMyVEL(String mUserId) {

        DatabaseReference productMyReference =
                FirebaseReferences.getReferenceMyProducts(mUserId);

        ValueEventListener myProductVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot: snapshot.getChildren()) {
                    ProductMy p = shot.getValue(ProductMy.class);

                    if(p != null) {
                        mRepository.synchroniseProductMy(p);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "Unable to update MyProducts, with error: " + databaseError);
            }
        };
        mMyProductsListener = new DataListenerPending(productMyReference, myProductVEL);
    }

    /**
     * Adds or removes a ValueEventListener to the collection_users/{userId]/collection_products
     * reference in Firebase.
     * @param activeState true to add the lister, false to remove it.
     */
    void isLiveProductMyList(boolean activeState, String userId) {
        initialiseProductMyVEL(userId);
        mMyProductsListener.changeListenerState(activeState);
    }
}
