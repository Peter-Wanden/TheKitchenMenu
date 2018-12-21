package com.example.peter.thekitchenmenu.app;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;


// For more information on this class see
// https://www.youtube.com/watch?v=998tPb10DFM&index=6&list=PL6nth5sRD25hVezlyqlBO9dafKMc5fAU2
public class HandlerWorker extends HandlerThread {

    private static final String TAG = "HandlerWorker";

    private Handler mHandler;

    public HandlerWorker(String name) {
        // Provide own name
        super(name);
        // Start the thread looper
        start();
        mHandler = new Handler(getLooper());
    }

    // As an interface for the outer world to interact with this class, use a public method
    // that returns an instance of this class that provides a builder so that additional tasks
    // can be added
    public HandlerWorker execute(Runnable task) {
        mHandler.post(task);
        return this;
    }
}
