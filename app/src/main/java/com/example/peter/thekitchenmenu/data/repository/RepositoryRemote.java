package com.example.peter.thekitchenmenu.data.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.model.DmProdComm;
import com.example.peter.thekitchenmenu.data.model.DmProdMy;
import com.example.peter.thekitchenmenu.data.databaseRemote.DataListenerPending;
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteDbRefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Manages data access for the remote database
 */
class RepositoryRemote {

    private static final String LOG_TAG = RepositoryRemote.class.getSimpleName();

    // A reference to the repository so that all communications with the app go through its
    // repository instance.
    private Repository mRepository;
    // Manages the listeners connection to the DmProdComm remote data
    private DataListenerPending mCommProdListener;
    // Manages the listeners connection to the DmProdMy remote data
    private DataListenerPending mMyProductsListener;

    /**
     * Constructor
     * @param repository an instance reference to the Repository
     */
    RepositoryRemote(Repository repository) {

        // Reference to the repository
        this.mRepository = repository;

        // Database reference to the community products location in Firebase.
        DatabaseReference productCommunityReference =
                RemoteDbRefs.getRefProdComm();

        // Listens to changes in the remote database and reflects them into the local database.
        ValueEventListener communityProductVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    DmProdComm dmProdComm = shot.getValue(DmProdComm.class);

                    if (dmProdComm != null) {
                        dmProdComm.setFbProductReferenceKey(shot.getKey());
                        mRepository.syncProdComm(dmProdComm);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "Unable to update community products, with error: "
                        + databaseError);
            }
        };
        mCommProdListener = new DataListenerPending(
                productCommunityReference, communityProductVEL);
    }

    /**
     * Adds or removes a ValueEventListener to the DmProdComm reference in Firebase.
     * @param activeState true to add the lister, false to remove it.
     */
    void isLiveProdComm(boolean activeState) {
        // Log.e(LOG_TAG, "FIREBASE - Product COMM listener is: " + activeState);
        mCommProdListener.changeListenerState(activeState);
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
                RemoteDbRefs.getRefProdMy(mUserId);

        ValueEventListener myProductVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot: snapshot.getChildren()) {
                    DmProdMy p = shot.getValue(DmProdMy.class);

                    if(p != null) {
                        mRepository.remoteSyncProdMy(p);
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
    void isLiveProdMy(boolean activeState, String userId) {
        initialiseProductMyVEL(userId);
        // Log.e(LOG_TAG, "FIREBASE - Product MY listener is: " + activeState);
        mMyProductsListener.changeListenerState(activeState);
    }
}
