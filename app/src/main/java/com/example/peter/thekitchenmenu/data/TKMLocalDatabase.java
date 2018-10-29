package com.example.peter.thekitchenmenu.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.ProductCommunity;
import com.example.peter.thekitchenmenu.model.ProductMy;

import java.util.ArrayList;

@Database(entities = {
        ProductCommunity.class,
        ProductMy.class},
        version = 1,
        exportSchema = false)
public abstract class TKMLocalDatabase extends RoomDatabase {

    private static final String LOG_TAG = TKMLocalDatabase.class.getSimpleName();

    // DAO's
    public abstract ProductCommunityDAO productCommunityDAO();
    public abstract ProductMyDAO productMyDAO();

    private static final Object LOCK = new Object();

    // Local database name
    private static final String TKM_LOCAL_DATABASE = Constants.LOCAL_DATABASE_NAME;

    private static volatile TKMLocalDatabase sInstance;

    public static TKMLocalDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TKMLocalDatabase.class, TKMLocalDatabase.TKM_LOCAL_DATABASE)
                        .build();
            }
        }
        return sInstance;
    }

    ArrayList<ProductCommunity> productCommunity = new ArrayList<>();
    ArrayList<ProductMy> productMy = new ArrayList<>();
}
