package com.example.peter.thekitchenmenu.app;

import android.net.Uri;

public class Constants {

    /* **********
     * PRODUCTS *
     ************/

    /* Key values for a product */
    public static final String PRODUCT_KEY = "current_product_key";
    public static final String PRODUCT_ID = "current_product_id";

    /* Default values for a product */
    public static final int DEFAULT_PRODUCT_ID = -1;
    public static final String DEFAULT_PRODUCT_DESCRIPTION = "no_description";
    public static final String DEFAULT_PRODUCT_RETAILER = "no_retailer";
    public static final int DEFAULT_PRODUCT_UOM = 0;
    public static final int DEFAULT_PRODUCT_PACK_SIZE = 0;
    public static final int DEFAULT_PRODUCT_SHELF_LIFE = 0;
    public static final String DEFAULT_PRODUCT_LOC = "no_location";
    public static final String DEFAULT_PRODUCT_LOC_IN_ROOM = "no_location_in_room";
    public static final int DEFAULT_PRODUCT_CATEGORY = 0;
    public static final double DEFAULT_PRODUCT_PRICE = 0.00;
    public static final Uri DEFAULT_LOCAL_IMAGE_URI = null;

    /* *********
     * RECIPES *
     ***********/
    /* Key value for a recipe */
    public static final String RECIPE_KEY = "current_recipe_key";
    /* Key value for a recipe ID */
    public static final String RECIPE_ID = "current_recipe_id";

    /* Default values for a recipe */
    public static final int DEFAULT_RECIPE_ID = -1;
    public static final String DEFAULT_RECIPE_TITLE = "no_title";
    public static final String DEFAULT_RECIPE_DESCRIPTION = "no_description";
    public static final int DEFAULT_RECIPE_CATEGORY = 0;
    public static final int DEFAULT_RECIPE_SERVINGS = 0;
    public static final int DEFAULT_RECIPE_SITTINGS = 1;

    /* *************
     * Permissions *
     * *************/
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_STORAGE_PERMISSION = 1;

    /* ***************
     * File provider *
     *****************/
    public static final String FILE_PROVIDER_AUTHORITY
            = "com.example.peter.thekitchenmenu.fileprovider";
}
