package com.example.peter.thekitchenmenu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TKMDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tkmdb";
    public static final int DATABASE_VERSION = 1;

    public TKMDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /* String that creates the product table */
        final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE "
                + TKMContract.ProductEntry.TABLE_NAME + " ("
                + TKMContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TKMContract.ProductEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + TKMContract.ProductEntry.COLUMN_RETAILER + " TEXT NOT NULL, "
                + TKMContract.ProductEntry.COLUMN_UOM + " INTEGER NOT NULL, "
                + TKMContract.ProductEntry.COLUMN_PACK_SIZE + " INTEGER NOT NULL, "
                + TKMContract.ProductEntry.COLUMN_SHELF_LIFE + " INTEGER NOT NULL, "
                + TKMContract.ProductEntry.COLUMN_LOC_ROOM + " TEXT, "
                + TKMContract.ProductEntry.COLUMN_LOC_IN_ROOM + " TEXT, "
                + TKMContract.ProductEntry.COLUMN_PACKS_PER_MONTH + " INTEGER DEFAULT NULL, "
                + TKMContract.ProductEntry.COLUMN_CATEGORY + " INTEGER DEFAULT NULL, "
                + TKMContract.ProductEntry.COLUMN_PACK_PRICE + " REAL NOT NULL DEFAULT 0);";

        /* Create the table */
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
