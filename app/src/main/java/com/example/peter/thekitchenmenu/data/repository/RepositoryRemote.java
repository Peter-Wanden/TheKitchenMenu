package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.databaseRemote.DataListenerPending;
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteDbRefs;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Queue;

import androidx.annotation.NonNull;

/**
 * Manages data and value event listeners for the remote database
 */
public class RepositoryRemote {

    private static final String TAG = "RepositoryRemote";

    private static RepositoryRemote sInstance;

    private SyncProdComm mSyncProdComm;
    private SyncProdMy mSyncProdMy;

    // Manages the listeners connection to the DmProdComm remote data
    private DataListenerPending mProdCommListener;
    // Manages the listeners connection to the DmProdMy remote data
    private DataListenerPending mProdMyListener;

    /**
     * @param context the Application context
     */
    private RepositoryRemote(Context context) {
        mSyncProdComm = new SyncProdComm(context);
        mSyncProdMy = new SyncProdMy(context);
    }

    public static RepositoryRemote getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (RepositoryRemote.class) {
                if (sInstance == null) {
                    sInstance = new RepositoryRemote(context);
                }
            }
        }
        return sInstance;
    }

    private void initialiseProdCommVel() {
        Log.d(TAG, "initialiseProdCommVel: called");

        // Database reference to the community products location in Firebase.
        DatabaseReference prodCommRef = RemoteDbRefs.getRefProdComm();

        // A Queue to store the modified returned data
        Queue<DmProdComm> remoteData = new LinkedList<>();

        // Listens to changes in the remote database and reflects them into the local database.
        ValueEventListener prodCommVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    DmProdComm pc = shot.getValue(DmProdComm.class);

                    if (pc != null) {
                        // Add the remote reference key
                        pc.setRemoteProdRefKey(shot.getKey());
                        remoteData.add(pc);
                    }
                }
                // Copy the remote data for processing and empty the queue ready for new updates
                Queue<DmProdComm> syncData = new LinkedList<>(remoteData);
                remoteData.clear();
                mSyncProdComm.syncRemoteData(syncData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Unable to update community products, with error: "
                        + databaseError);
            }
        };
        mProdCommListener = new DataListenerPending(prodCommRef, prodCommVEL);
    }

    /**
     * Adds or removes a ValueEventListener to the DmProdComm reference in Firebase.
     *
     * @param activeState true to add the lister, false to remove it.
     */
    void isLiveProdComm(boolean activeState) {

        // // If there isn't an active listener, create one and update its state.
        if (mProdCommListener == null) {
            initialiseProdCommVel();
            mProdCommListener.changeListenerState(activeState);

        // If the current listeners state is not equal to the required state, update it.
        } else if (mProdCommListener.getListenerState() != activeState) {
            mProdCommListener.changeListenerState(activeState);
        }
    }

    /**
     * Sets up the remote collection_users/{user_id}/collection_products/ location ready for
     * synchronisation.
     * Receives and converts the data at the specified location to a local database object.
     * Sends the data to be processed by the repository.
     *
     * @param mUserId the unique Firebase user ID.
     */
    private void initialiseProdMyVel(String mUserId) {
        Log.d(TAG, "initialiseProdMyVel: called");

        DatabaseReference prodMyRef = RemoteDbRefs.getRefProdMy(mUserId);

        Queue<DmProdMy> remoteData = new LinkedList<>();

        ValueEventListener prodMyVel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    DmProdMy pm = shot.getValue(DmProdMy.class);

                    if (pm != null) {
                        remoteData.add(pm);
                    }
                }
                Queue<DmProdMy> syncData = new LinkedList<>(remoteData);
                remoteData.clear();
                mSyncProdMy.syncRemoteData(syncData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Unable to update MyProducts, with error: " + databaseError);
            }
        };
        mProdMyListener = new DataListenerPending(prodMyRef, prodMyVel);
    }

    /**
     * Adds or removes a ValueEventListener to the collection_users/{userId]/collection_products
     * reference in the remote database.
     * @param activeState true to add the lister, false to remove it.
     */
    void isLiveProdMy(boolean activeState, String userId) {
        // If there isn't an active listener, create one and update its state.
        if (mProdMyListener == null) {
            initialiseProdMyVel(userId);
            mProdMyListener.changeListenerState(activeState);

        // If the current listeners state is not equal to the required state, update it.
        } else if (mProdMyListener.getListenerState() != activeState) {
            mProdMyListener.changeListenerState(activeState);
            Log.d(TAG, "isLiveProdMy: Listener state changed to: " + activeState);
        }
    }
}
