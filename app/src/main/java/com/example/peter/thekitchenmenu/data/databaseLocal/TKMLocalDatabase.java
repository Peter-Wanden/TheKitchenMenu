package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.entity.UsersProductData;
import com.example.peter.thekitchenmenu.data.entity.ProductFastTextSearch;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import static com.example.peter.thekitchenmenu.data.entity.Product.*;
import static com.example.peter.thekitchenmenu.data.entity.ProductFastTextSearch.TABLE_FTS_PRODUCT;

@Database(entities = {
        Product.class,
        UsersProductData.class,
        ProductFastTextSearch.class},
        version = 2,
        exportSchema = false)
public abstract class TKMLocalDatabase extends RoomDatabase {

    private static final String TAG = "TKMLocalDatabase";
    private static final String TKM_LOCAL_DATABASE = "tkm_local_database";
    private static TKMLocalDatabase sInstance;

    public abstract ProductDAO productDAO();
    public abstract UsersProductDataDAO userProductDataDAO();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static TKMLocalDatabase getInstance(final Context context,
                                               final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (TKMLocalDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private static TKMLocalDatabase buildDatabase(
            final Context appContext,
            final AppExecutors executors) {

        return Room.databaseBuilder(appContext, TKMLocalDatabase.class, TKM_LOCAL_DATABASE)
                .addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executors.diskIO().execute(() -> {
                    // Add anything here that needs to be completed during database creation,
                    // Loading large amounts of data for example.
                    // See Persistence Sample for more information.
                    TKMLocalDatabase database = TKMLocalDatabase.getInstance(appContext, executors);
                    // insertData(database, prodComms, prodMys);
                    database.setDatabaseCreated();
                });
            }
        })
                .addMigrations(MIGRATION_2_3)
                .build();
    }

    private static void insertData(final TKMLocalDatabase database,
                                   final List<Product> products,
                                   final List<UsersProductData> usersProductData) {
        database.runInTransaction(() -> {
            database.productDAO().insertAll(products);
            database.userProductDataDAO().insertAll(usersProductData);
        });
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(TKM_LOCAL_DATABASE).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        isDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return isDatabaseCreated;
    }


    private static final Migration MIGRATION_2_3 = new Migration(2,3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS " + TABLE_FTS_PRODUCT +
                            " USING FTS4(" +
                            DESCRIPTION + " TEXT, " +
                            MADE_BY + " TEXT, " +
                            "content=" + TABLE_PRODUCT + ")");

            database.execSQL("INSERT INTO " + TABLE_FTS_PRODUCT +
                    " (`rowid`, " + DESCRIPTION + ", " + MADE_BY + ") "
                    + "SELECT " + ID + ", " + DESCRIPTION + ", " +
                            MADE_BY + " FROM " + TABLE_PRODUCT);
        }
    };
}
