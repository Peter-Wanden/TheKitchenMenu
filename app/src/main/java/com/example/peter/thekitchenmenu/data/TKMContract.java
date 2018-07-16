package com.example.peter.thekitchenmenu.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TKMContract {

    /* The name for the content provider */
    public static final String CONTENT_AUTHORITY = "com.example.peter.thekitchenmenu";

    /* Base Uri */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /* Path to products */
    public static final String PATH_PRODUCTS = "products";

    /* Inner class that defines the table contents of the product table */
    public static final class ProductEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the product table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PRODUCTS)
                .build();

        /* The MIME type of the {@link #CONTENT_URI} for a list of products */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/"
                + CONTENT_AUTHORITY
                + "/"
                + PATH_PRODUCTS;

        /* The MIME type of the {@link #CONTENT_URI} for a single product */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE
                        + "/"
                        + CONTENT_AUTHORITY
                        + "/"
                        + PATH_PRODUCTS;

        /* Used internally as the name of our product table. */
        public static final String TABLE_NAME = "Product";

        /* Unique ID for the product */
        public final static String _ID = BaseColumns._ID;

        /* Product description column */
        public static final String COLUMN_DESCRIPTION = "Description";

        /* Retailers name column */
        public static final String COLUMN_RETAILER = "Retailer";

        /* Pack size column */
        public static final String COLUMN_PACK_SIZE = "Pack_Size";

        /* Location room column */
        public static final String COLUMN_LOC_ROOM = "Location_Room";

        /* Location in room column */
        public static final String COLUMN_LOC_IN_ROOM = "Location_in_Room";

        /* Packs per shop */
        public static final String COLUMN_PACKS_PER_MONTH = "Packs_Per_Month";

        /* Pack price column */
        public static final String COLUMN_PACK_PRICE = "Pack_Price";

        /**
         * Fields with defined constant values
         */
        public static final int NOTHING_SELECTED = 0; // Default values for constant fields

        /* Unit of measure column */
        public static final String COLUMN_UOM = "Unit_of_Measure";
        /* Possible values for the unit of measure column */
        public static final int GRAMS = 1;
        public static final int MILLILITRES = 2;
        public static final int COUNT = 3;

        /* Category column */
        public static final String COLUMN_CATEGORY = "Category";
        /* Possible values for the category column */
        public static final int NON_FOOD = 1;
        public static final int FOOD_OTHER = 2;
        public static final int RECIPE = 3;

        /* Shelf life column */
        public static final String COLUMN_SHELF_LIFE = "Shelf_Life";
        /* Possible values for the shelf life */
        public static final int SHELF_LIFE_1_DAY = 1;
        public static final int SHELF_LIFE_3_DAYS = 3;
        public static final int SHELF_LIFE_5_DAYS = 5;
        public static final int SHELF_LIFE_7_DAYS = 7;
        public static final int SHELF_LIFE_14_DAYS = 14;
        public static final int SHELF_LIFE_21_DAYS = 21;
        public static final int SHELF_LIFE_31_DAYS = 31;
        public static final int SHELF_LIFE_90_DAYS = 90;
        public static final int SHELF_LIFE_LONG_LIFE = 365;




        /* Returns whether or not the given category is valid */
        public static boolean isValidCategory(int category) {
            return category == NON_FOOD || category == FOOD_OTHER || category == RECIPE;
        }

        /* Returns whether or not the given unit of measure is valid */
        public static boolean isValidUOM(int uom) {
            return uom == GRAMS || uom == MILLILITRES || uom == COUNT;
        }
    }
}
