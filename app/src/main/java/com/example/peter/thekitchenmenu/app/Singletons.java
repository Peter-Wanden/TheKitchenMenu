package com.example.peter.thekitchenmenu.app;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRemote;

/**
 * Android Application class. Used for accessing singletons.
 */
public class Singletons extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public TKMDatabase getDatabase() {
        return TKMDatabase.getInstance(this, mAppExecutors);
    }

    public Repository getRepository() {
        return Repository.getInstance(this, getDatabase());
    }

    public RepositoryRemote getRepositoryRemote() {
        return RepositoryRemote.getInstance(this);
    }
}
