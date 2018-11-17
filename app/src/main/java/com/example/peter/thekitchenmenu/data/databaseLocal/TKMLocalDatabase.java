package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.DmProdComm;
import com.example.peter.thekitchenmenu.data.model.DmProdMy;

@Database(entities = {
        DmProdComm.class,
        DmProdMy.class},
        version = 2,
        exportSchema = false)
public abstract class TKMLocalDatabase extends RoomDatabase {

    private static final String LOG_TAG = TKMLocalDatabase.class.getSimpleName();

    // DAO's
    public abstract ProdCommDAO productCommunityDAO();
    public abstract ProdMyDAO productMyDAO();

    private static final Object LOCK = new Object();

    // Local database name
    private static final String TKM_LOCAL_DATABASE = Constants.LOCAL_DATABASE_NAME;

    private static volatile TKMLocalDatabase sInstance;

    public static TKMLocalDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TKMLocalDatabase.class, TKMLocalDatabase.TKM_LOCAL_DATABASE)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }
}
