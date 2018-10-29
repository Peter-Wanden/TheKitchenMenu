package com.example.peter.thekitchenmenu.repository;

import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Used to attach and detach listeners to Firebase DatabaseReferences and Queries. Adds a 2 second
 * delay when a listener is removed to prevent unnecessary re-querying due to configuration changes
 * etc.
 */
public class DataListenerPending {

    private static final String LOG_TAG = DataListenerPending.class.getSimpleName();

    private final Handler mHandler = new Handler();
    private final Query mQuery;
    private final ValueEventListener listener;
    private boolean mListenerRemovePending = false;

    private final Runnable mRemoveListener = new Runnable() {
        @Override
        public void run() {
            mQuery.removeEventListener(listener);
            mListenerRemovePending = false;
        }
    };

    /**
     * Firebase query constructor
     *
     * @param query A reference to the query object from which the data is derived. It is used
     *              by this class to attach and detach the listener.
     * @param listener the listener to attach to this query.
     */
    public DataListenerPending(Query query, ValueEventListener listener) {
        this.mQuery = query;
        this.listener = listener;
    }

    /**
     * Firebase database reference constructor
     *
     * @param reference A reference to the query object from which the data is derived.
     *                          It is used by this class to attach and detach the listener.
     * @param listener The listener to attach to this reference.
     */
    public DataListenerPending(DatabaseReference reference, ValueEventListener listener) {
        this.mQuery = reference;
        this.listener = listener;
    }

    /**
     * Adds and removes the listener when called
     * @param attachState true to add a listener, false to remove it.
     */
    public void changeListenerState(boolean attachState) {
        if (attachState) {
            if (mListenerRemovePending) {
                mHandler.removeCallbacks(mRemoveListener);
            } else {
                mQuery.addValueEventListener(listener);
                Log.e(LOG_TAG, "ValueEventListener added");
            }
            mListenerRemovePending = false;
        } else {
            mHandler.postDelayed(mRemoveListener, 2000);
            mListenerRemovePending = true;
            Log.e(LOG_TAG, "ValueEventListener removed");
        }
    }
}
