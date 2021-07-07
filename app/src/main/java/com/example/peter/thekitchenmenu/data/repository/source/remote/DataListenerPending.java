package com.example.peter.thekitchenmenu.data.repository.source.remote;

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

    private final Handler handler = new Handler();
    private final Query query;
    private final ValueEventListener listener;
    private boolean listenerRemovalIsPending = false;
    private boolean listenerIsAttached;

    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovalIsPending = false;
            listenerIsAttached = false;
        }
    };

    /**
     * @param query A reference to the query object from which the data is derived. It is used
     *              by this class to attach and detach the listener.
     * @param listener the listener to attach to this query.
     */
    public DataListenerPending(Query query, ValueEventListener listener) {
        this.query = query;
        this.listener = listener;
    }

    /**
     * @param reference A reference to the query object from which the data is derived.
     *                  It is used by this class to attach and detach the listener.
     * @param listener  The listener to attach to this reference.
     */
    public DataListenerPending(DatabaseReference reference, ValueEventListener listener) {
        this.query = reference;
        this.listener = listener;
    }

    /**
     * Adds and removes the listener when called
     * @param isAttached true to add a listener, false to remove it.
     */
    public void setListenerIsAttached(boolean isAttached) {
        if (isAttached) {
            if (listenerRemovalIsPending) {
                // If asked to attach listener and it is currently in a state of pending removal,
                // stop it from being removed.
                handler.removeCallbacks(removeListener);
            } else {
                // Only attach listener if it is not already attached.
                if (!listenerIsAttached) {
                    query.addValueEventListener(listener);
                    listenerIsAttached = true;
                }
            }
            listenerRemovalIsPending = false;
        } else {
            // Request to remove listener received. Wait for 2 secs to account for config changes.
            handler.postDelayed(removeListener, 2000);
            listenerRemovalIsPending = true;
        }
    }

    public boolean getListenerIsAttached() {
        return listenerIsAttached;
    }
}
