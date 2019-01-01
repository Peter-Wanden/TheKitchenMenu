package com.example.peter.thekitchenmenu.data.databaseRemote;

import android.os.Handler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Used to attach and detach listeners to DatabaseReferences and Queries. Adds a 2 second
 * delay when a listener is removed to prevent unnecessary re-querying due to configuration changes
 * etc.
 */
public class DataListenerPending {

    private static final String TAG = "DataListenerPending";

    private final Handler mHandler = new Handler();
    private final Query mQuery;
    private final ValueEventListener mListener;
    private boolean mListenerRemovePending = false;
    private boolean mListenerState;

    private final Runnable mRemoveListener = new Runnable() {
        @Override
        public void run() {
            mQuery.removeEventListener(mListener);
            mListenerRemovePending = false;
            mListenerState = false;
        }
    };

    /**
     * @param query A reference to the query object from which the data is derived. It is used
     *              by this class to attach and detach the listener.
     * @param listener the listener to attach to this query.
     */
    public DataListenerPending(Query query, ValueEventListener listener) {
        this.mQuery = query;
        this.mListener = listener;
    }

    /**
     * @param reference A reference to the query object from which the data is derived.
     *                          It is used by this class to attach and detach the listener.
     * @param listener The listener to attach to this reference.
     */
    public DataListenerPending(DatabaseReference reference, ValueEventListener listener) {
        this.mQuery = reference;
        this.mListener = listener;
    }

    /**
     * Adds and removes the listener when called
     * @param attachState true to add a listener, false to remove it.
     */
    public void setListenerState(boolean attachState) {
        if (attachState) {
            if (mListenerRemovePending) {
                // If asked to attach listener and it is currently in a state of pending removal,
                // stop it from being removed.
                mHandler.removeCallbacks(mRemoveListener);
            } else {
                // Only attach listener if it is not already attached.
                if (!mListenerState) {
                    mQuery.addValueEventListener(mListener);
                    mListenerState = true;
                }
            }
            mListenerRemovePending = false;
        } else {
            // Request to remove listener received. Wait for 2 secs to account for config changes.
            mHandler.postDelayed(mRemoveListener, 2000);
            mListenerRemovePending = true;
        }
    }

    public boolean getListenerState() {
        return mListenerState;
    }
}
