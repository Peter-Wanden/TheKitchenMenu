package com.example.peter.thekitchenmenu.data.databaseLocal;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;
import com.example.peter.thekitchenmenu.data.entity.FtsProdComm;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.TABLE_PRODUCT_COMM;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.TABLE_PROD_COMM_DESC;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.TABLE_PROD_COMM_ID;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.TABLE_PROD_COMM_MADE_BY;
import static com.example.peter.thekitchenmenu.data.entity.FtsProdComm.TABLE_FTS_PROD_COMM;

@Database(entities = {
        DmProdComm.class,
        DmProdMy.class,
        FtsProdComm.class},
        version = 2,
        exportSchema = false)
public abstract class TKMLocalDatabase extends RoomDatabase {

    private static final String TAG = "TKMLocalDatabase";

    private static final String TKM_LOCAL_DATABASE = "tkm_local_database";

    private static TKMLocalDatabase sInstance;

    // DAO's
    public abstract ProdCommDAO prodCommDAO();
    public abstract ProdMyDAO prodMyDAO();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

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
                                   final List<DmProdComm> prodComms,
                                   final List<DmProdMy> prodMys) {
        database.runInTransaction(() -> {
            database.prodCommDAO().insertAll(prodComms);
            database.prodMyDAO().insertAll(prodMys);
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
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }


    private static final Migration MIGRATION_2_3 = new Migration(2,3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE VIRTUAL TABLE IF NOT EXISTS " + TABLE_FTS_PROD_COMM +
                            " USING FTS4(" +
                            TABLE_PROD_COMM_DESC + " TEXT, " +
                            TABLE_PROD_COMM_MADE_BY + " TEXT, " +
                            "content=" + TABLE_PRODUCT_COMM + ")");

            database.execSQL(
                    "INSERT INTO " + TABLE_FTS_PROD_COMM +
                    " (`rowid`, " + TABLE_PROD_COMM_DESC + ", " + TABLE_PROD_COMM_MADE_BY + ") "
                    + "SELECT " + TABLE_PROD_COMM_ID + ", " + TABLE_PROD_COMM_DESC + ", " +
                    TABLE_PROD_COMM_MADE_BY + " FROM " + TABLE_PRODUCT_COMM);
        }
    };
}
