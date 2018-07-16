package com.example.peter.thekitchenmenu.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.tkmapplibrary.dataValidation.InputValidation;

import java.util.Objects;

public class TKMProvider extends ContentProvider {

    public static final String LOG_TAG = TKMProvider.class.getSimpleName();

    /* URI matcher code for the content URI for the products table */
    public static final int PRODUCTS = 100;

    /* URI matcher code for the content URI for a simgle product */
    public static final int PRODUCT_ID = 101;

    /* URI matcher object to match a content URI with a corresponding code */
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /* Static initializer */
    static {
        /* addURI here for all patterns. Returns a corresponding code when a match is found */

        /* This URI is used to provide access to MULTIPLE rows of the products table*/
        sUriMatcher.addURI(
                TKMContract.CONTENT_AUTHORITY, TKMContract.PATH_PRODUCTS, PRODUCTS);

        /* This URI is used to provide access to ONE row of the product table */
        sUriMatcher.addURI(
                TKMContract.CONTENT_AUTHORITY, TKMContract.PATH_PRODUCTS + "/#", PRODUCT_ID);

    }

    /* Database helper object */
    private TKMDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TKMDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        /* Get readable database */
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        /* This cursor will hold the result of the query */
        Cursor cursor;

        /* Match the URI ot the given code */
        int match = sUriMatcher.match(uri);
        switch (match) {

            case PRODUCTS:
                /* Query the products table directly and return all matching rows */
                cursor = database.query(TKMContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case PRODUCT_ID:
                /* Insert the product ID from the URI */
                selection = TKMContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                /* Query the products table for the entry with a matching ID */
                cursor = database.query(TKMContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

                default:
                    throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        /* Set notification URI on the Cursor so we know what content URI the Cursor was created for */
        /* If the data at this URI changes, then we know we need to update the Cursor */
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                assert values != null;
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Validate all fields then insert a product into the database with the given content values.
     * Return the new content URI for that specific row in the database.
     */
    private Uri insertProduct(Uri uri, ContentValues values) {
        /* Test the validity of the values */

        // Product description
        String description = values.getAsString(TKMContract.ProductEntry.COLUMN_DESCRIPTION);
        int validateDescription = InputValidation.validateProductDescription(description);
        if (validateDescription == 0) {
            throw new IllegalArgumentException("Product requires a description");
        } else if(validateDescription == 1) {
            throw new IllegalArgumentException("Product description too long, 120 char limit");
        }

        // Retailer
        String retailer = values.getAsString(TKMContract.ProductEntry.COLUMN_RETAILER);
        int validateRetailer = InputValidation.validateRetailer(retailer);
        if (validateRetailer == 0) {
            throw new IllegalArgumentException("Retailer requires a name");
        } else if(validateRetailer == 1) {
            throw new IllegalArgumentException("Retailer name too long, 120 char limit");
        }

        // Unit of measure
        int UoM = values.getAsInteger(TKMContract.ProductEntry.COLUMN_UOM);
        boolean validateUoM = InputValidation.validateUoM(UoM);
        if(!validateUoM) {
            throw new IllegalArgumentException("Invalid unit of measure, " +
                    "values between 0 and 2 allowed");
        }

        // Pack size
        int packSize = values.getAsInteger(TKMContract.ProductEntry.COLUMN_PACK_SIZE);
        boolean validatePackSize = InputValidation.validatePackSize(packSize);
        if(!validatePackSize) {
            throw new IllegalArgumentException("Pack size out of range, must be a value between " +
                    "1 and 1000");
        }

        // Shelf life
        int shelfLife = values.getAsInteger(TKMContract.ProductEntry.COLUMN_SHELF_LIFE);
        boolean validateShelfLife = InputValidation.validateShelfLife(shelfLife);
        if(!validateShelfLife) {
            throw new IllegalArgumentException("Invalid shelf life, must be a value between " +
                    "0 and 12");
        }

        // Location room
        String locRoom = values.getAsString(TKMContract.ProductEntry.COLUMN_LOC_ROOM);
        int validateLocRoom = InputValidation.validateLocRoom(locRoom);
        if (validateLocRoom == 0) {
            throw new IllegalArgumentException("Room requires a name");
        } else if(validateLocRoom == 1) {
            throw new IllegalArgumentException("Room name too long, 60 char limit");
        }

        // Location in room
        String locInRoom = values.getAsString(TKMContract.ProductEntry.COLUMN_LOC_IN_ROOM);
        int validateLocInRoom = InputValidation.validateLocInRoom(locInRoom);
        if (validateLocInRoom == 0) {
            throw new IllegalArgumentException("Location in room requires value");
        } else if(validateLocInRoom == 1) {
            throw new IllegalArgumentException("Location name too long, 60 char limit");
        }

        // Packs per month
        int packsPerMonth = values.getAsInteger(TKMContract.ProductEntry.COLUMN_PACK_PRICE);
        boolean validatePacksPerMonth = InputValidation.validatePacksPerMonth(packsPerMonth);
        if(!validatePacksPerMonth) {
            throw new IllegalArgumentException("Packs per month must be between 0 and 1000");
        }

        // Category
        int category = values.getAsInteger(TKMContract.ProductEntry.COLUMN_CATEGORY);
        boolean validateCategory = InputValidation.validateCategory(category);
        if(!validateCategory){
            throw new IllegalArgumentException("Category must be between 0 and 2");
        }

        // Price - does not require validation

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new product with the given values
        long id = database.insert(TKMContract.ProductEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the product content URI
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch(match) {
            case PRODUCTS:
                return updateProducts(uri, values, selection, selectionArgs);
                // ToDO - does there need to be a break; here?
            case PRODUCT_ID:
                // For the Product_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = TKMContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProducts(uri, values, selection, selectionArgs);
                // ToDO - does there need to be a break; here?
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update products in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments. Return the number of rows that were
     * successfully updated.
     */
    private int updateProducts(Uri uri,
                               ContentValues values,
                               String selection,
                               String[] selectionArgs) {

        // If the {@link ProductEntry#COLUMN_PRODUCT_NAME} key is present, check that the
        // description value is not null.
        if(values.containsKey(TKMContract.ProductEntry.COLUMN_DESCRIPTION)){
            String description = values.getAsString(TKMContract.ProductEntry.COLUMN_DESCRIPTION);
            if (description == null) {
                throw new IllegalArgumentException("Product requires a description");
            }
        }



        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(TKMContract.ProductEntry.TABLE_NAME, values, selection,
                selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            Objects.requireNonNull(getContext())
                    .getContentResolver()
                    .notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
