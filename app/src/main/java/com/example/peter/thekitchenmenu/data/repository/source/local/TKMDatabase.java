package com.example.peter.thekitchenmenu.data.repository.source.local;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductFastTextSearch;
import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import static com.example.peter.thekitchenmenu.data.entity.ProductEntity.*;
import static com.example.peter.thekitchenmenu.data.entity.ProductFastTextSearch.TABLE_FTS_PRODUCT;

@Database(entities = {
        ProductEntity.class,
        ProductFastTextSearch.class,
        FavoriteProductEntity.class,
        RecipeEntity.class,
        RecipeCourseEntity.class,
        RecipeIdentityEntity.class,
        RecipeDurationEntity.class,
        IngredientEntity.class,
        RecipePortionsEntity.class},
        version = 2,
        exportSchema = false)
public abstract class TKMDatabase extends RoomDatabase {

    private static final String TKM_LOCAL_DATABASE = "tkm_local_database";
    private static TKMDatabase sInstance;

    public abstract ProductEntityDao productEntityDao();
    public abstract FavoriteProductEntityDao favoriteProductEntityDao();
    public abstract RecipeEntityDao recipeEntityDao();
    public abstract RecipeCourseEntityDao recipeCourseEntityDao();
    public abstract RecipeIdentityEntityDao recipeIdentityEntityDao();
    public abstract RecipeDurationEntityDao recipeDurationEntityDao();
    public abstract IngredientEntityDao ingredientEntityDao();
    public abstract RecipePortionsEntityDao portionsEntityDao();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static TKMDatabase getInstance(final Context context,
                                          final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (TKMDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private static TKMDatabase buildDatabase(
            final Context appContext,
            final AppExecutors executors) {

        return Room.databaseBuilder(appContext, TKMDatabase.class, TKM_LOCAL_DATABASE)
                .addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executors.diskIO().execute(() -> {
                    // Add anything here that needs to be completed during database creation,
                    // Loading large amounts of data for example.
                    // See Persistence Sample for more information.
                    TKMDatabase database = TKMDatabase.getInstance(appContext, executors);
                    // insertData(database, prodComms, prodMys);
                    database.setDatabaseCreated();
                });
            }
        })
                .addMigrations(MIGRATION_2_3)
                .build();
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
                            " USING FTS4(" + DESCRIPTION + " TEXT, " +
                                SHOPPING_LIST_ITEM_NAME + " TEXT, " +
                            "content=" + TABLE_PRODUCT + ")");

            database.execSQL("INSERT INTO " + TABLE_FTS_PRODUCT +
                    " (`rowid`, " + DESCRIPTION + ", " + SHOPPING_LIST_ITEM_NAME + ") "
                    + "SELECT " + ID + ", " + DESCRIPTION + ", " +
                    SHOPPING_LIST_ITEM_NAME + " FROM " + TABLE_PRODUCT);
        }
    };
}
