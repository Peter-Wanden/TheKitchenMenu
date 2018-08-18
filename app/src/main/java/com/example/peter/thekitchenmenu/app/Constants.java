package com.example.peter.thekitchenmenu.app;

import android.net.Uri;

public class Constants {

    /* **********
     * PRODUCTS *
     ************/

    /* Key values for a product */
    public static final String PRODUCT_KEY = "current_product_key";
    public static final String PRODUCT_ID_KEY = "current_product_id_key";
    public static final String PRODUCT_FB_REFERENCE_KEY = "product_fb_reference_key";
    public static final String PRODUCT_STATUS_KEY = "product_status_key";
    public static final String PRODUCT_IS_CREATOR_KEY = "product_is_owner_key";
    public static final String PRODUCT_IS_EXISTING_KEY = "product_is_existing_key";
    public static final String PRODUCT_PUT_ON_USED_LIST = "product_put_on_used_list_key";
    public static final String PRODUCT_ON_USED_LIST_KEY = "product_on_used_list_key";
    public static final String BASE_FIELDS_EDITABLE_STATUS_KEY = "base_fields_editable_status_key";
    public static final String USER_CUSTOM_FIELDS_EDITABLE_STATUS_KEY = "user_custom_fields_editable_status_key";

    public static final String PRODUCT_BASE_DESCRIPTION_KEY = "description";
    public static final String PRODUCT_BASE_MADE_BY_KEY = "madeBy";
    public static final String PRODUCT_BASE_CATEGORY_KEY = "category";
    public static final String PRODUCT_BASE_SHELF_LIFE_KEY = "shelfLife";
    public static final String PRODUCT_BASE_PACK_SIZE_KEY = "packSize";
    public static final String PRODUCT_BASE_UNIT_OF_MEASURE_KEY = "unitOfMeasure";
    public static final String PRODUCT_BASE_PRICE_AVE_KEY = "packAvePrice";
    public static final String PRODUCT_BASE_CREATED_BY_KEY = "createdBy";
    public static final String PRODUCT_BASE_DOC_REF_KEY = "documentReference";

    public static final String PRODUCT_USER_LOCAL_IMAGE_URI_KEY = "localImageUri";
    public static final String PRODUCT_USER_FB_STORAGE_IMAGE_URI_KEY = "fbStorageImageUri";
    public static final String PRODUCT_USER_LOCATION_ROOM_KEY = "locationRoom";
    public static final String PRODUCT_USER_LOCATION_IN_ROOM_KEY = "locationInRoom";
    public static final String PRODUCT_USER_RETAILER_KEY = "retailer";
    public static final String PRODUCT_USER_PACK_PRICE_KEY = "packPrice";
    public static final String PRODUCT_USER_FB_REFERENCE_KEY = "fbProductReferenceKey";
    public static final String PRODUCT_USER_FB_USED_USER_KEY = "fbUsedProductsUserKey";

    /* Default values for a product */
    public static final int DEFAULT_PRODUCT_ID = -1;
    public static final String DEFAULT_FB_PRODUCT_ID = "no_fd_id";
    public static final String DEFAULT_FB_USED_PRODUCT_ID = "no_used_product_id";
    public static final String DEFAULT_PRODUCT_DESCRIPTION = "no_description";
    public static final String DEFAULT_PRODUCT_MADE_BY = "anonymous";
    public static final String DEFAULT_PRODUCT_RETAILER = "no_retailer";
    public static final int DEFAULT_PRODUCT_UOM = 0;
    public static final int DEFAULT_PRODUCT_PACK_SIZE = 0;
    public static final int DEFAULT_PRODUCT_SHELF_LIFE = 0;
    public static final String DEFAULT_PRODUCT_LOC = "no_location";
    public static final String DEFAULT_PRODUCT_LOC_IN_ROOM = "no_location_in_room";
    public static final int DEFAULT_PRODUCT_CATEGORY = 0;
    public static final double DEFAULT_PRODUCT_PRICE = 0.00;
    public static final double DEFAULT_PRODUCT_PRICE_AVERAGE = 0.00;
    public static final Uri DEFAULT_LOCAL_IMAGE_URI = Uri.parse("");
    public static final Uri DEFAULT_FB_IMAGE_STORAGE_URI = Uri.parse("");

    /* Product screen titles */
    public static final int ACTIVITY_TITLE_PRODUCT_VIEW = 1;
    public static final int ACTIVITY_TITLE_PRODUCT_EDIT = 2;
    public static final int ACTIVITY_TITLE_PRODUCT_ADD = 3;

    /* *************
     * Permissions *
     ***************/
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_STORAGE_PERMISSION = 2;
    public static final int REQUEST_IMAGE_MEDIA_STORE = 3;
    public static final int REQUEST_IMAGE_PICKER = 4;

    /* ***************
     * File provider *
     *****************/
    public static final String FILE_PROVIDER_AUTHORITY
            = "com.example.peter.thekitchenmenu.fileprovider";

    /* *******************
     * Firebase database *
     *********************/
    public static final String FB_COLLECTION_PRODUCTS = "/collection_products/";
    public static final String FB_COLLECTION_USERS = "/collection_users/";
    public static final String FB_COLLECTION_USED_PRODUCTS = "/collection_used_products";
    public static final int REQUEST_CODE_SIGN_IN = 5;
    public static final String ANONYMOUS = "anonymous";
    public static final String FB_STORAGE_IMAGE_REFERENCE = "/collection_product_images/";

    /* *******
     * Users *
     *********/
    public static final String USER_ID_KEY = "user_id_key";
}
