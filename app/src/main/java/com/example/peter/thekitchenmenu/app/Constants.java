package com.example.peter.thekitchenmenu.app;

import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.util.Log;

public abstract class Constants {

    private static final String LOG_TAG = Constants.class.getSimpleName();
    // TODO - Move constants to their respective packages

    /* **********
     * PRODUCTS *
     ************/

    /* Key values for a product */
    public static final String PRODUCT_KEY = "current_product_key";
    public static final String PRODUCT_FB_REFERENCE_KEY = "product_fb_reference_key";
    public static final String PRODUCT_IS_CREATOR_KEY = "product_is_owner_key";
    public static final String PRODUCT_IS_EXISTING_KEY = "product_is_existing_key";
    public static final String PRODUCT_PUT_ON_USED_LIST = "product_put_on_used_list_key";
    public static final String PRODUCT_ON_USED_LIST_KEY = "product_on_used_list_key";
    public static final String COMM_FIELDS_EDITABLE_STATUS_KEY = "base_fields_editable_status_key";
    public static final String MY_CUSTOM_FIELDS_EDITABLE_STATUS_KEY = "user_custom_fields_editable_status_key";

    // Firebase community product fields.
    public static final String PRODUCT_COMM_FB_REFERENCE_KEY = "fbProductReferenceKey";
    public static final String PRODUCT_COMM_DESCRIPTION_KEY = "description";
    public static final String PRODUCT_COMM_MADE_BY_KEY = "madeBy";
    public static final String PRODUCT_COMM_CATEGORY_KEY = "category";
    public static final String PRODUCT_COMM_SHELF_LIFE_KEY = "shelfLife";
    public static final String PRODUCT_COMM_PACK_SIZE_KEY = "packSize";
    public static final String PRODUCT_COMM_UNIT_OF_MEASURE_KEY = "unitOfMeasure";
    public static final String PRODUCT_COMM_PRICE_AVE_KEY = "packAvePrice";
    public static final String PRODUCT_COMM_CREATED_BY_KEY = "createdBy";
    public static final String PRODUCT_COMM_CREATE_DATE_KEY = "comm_create_date";
    public static final String PRODUCT_COMM_LAST_UPDATE_KEY = "comm_last_updated";

    // Firebase 'my' fields (user specific information for a product).
    public static final String PRODUCT_MY_LOCAL_IMAGE_URI_KEY = "localImageUri";
    public static final String PRODUCT_MY_FB_STORAGE_IMAGE_URI_KEY = "fbStorageImageUri";
    public static final String PRODUCT_MY_LOCATION_ROOM_KEY = "locationRoom";
    public static final String PRODUCT_MY_LOCATION_IN_ROOM_KEY = "locationInRoom";
    public static final String PRODUCT_MY_RETAILER_KEY = "retailer";
    public static final String PRODUCT_MY_PACK_PRICE_KEY = "packPrice";
    public static final String PRODUCT_MY_FB_REFERENCE_KEY = PRODUCT_COMM_FB_REFERENCE_KEY;
    public static final String PRODUCT_MY_FB_USED_PRODUCT_KEY = "fbUsedProductsUserKey";
    public static final String PRODUCT_MY_CREATE_DATE_KEY = "my_create_date";
    public static final String PRODUCT_MY_LAST_UPDATE_KEY = "my_last_updated";

    /* Default values for a VmProd */
    public static final int DEFAULT_PROD_MY_ID = 0;
    public static final String DEFAULT_REMOTE_REF_KEY = "no_key";
    public static final String DEFAULT_FB_USED_PRODUCT_ID = "no_used_product_id";
    public static final String DEFAULT_PRODUCT_RETAILER = "no_retailer";
    public static final String DEFAULT_PRODUCT_LOC = "no_location";
    public static final String DEFAULT_PRODUCT_LOC_IN_ROOM = "no_location_in_room";
    public static final double DEFAULT_PRODUCT_PRICE = 0.00;
    public static final Uri DEFAULT_LOCAL_IMAGE_URI = Uri.parse("");
    public static final long DEFAULT_MY_CREATE_DATE = 0;
    public static final long DEFAULT_MY_LAST_UPDATE = 0;


    public static final String DEFAULT_FB_PRODUCT_ID = "no_fd_id";
    public static final String DEFAULT_PRODUCT_DESCRIPTION = "no_description";
    public static final String DEFAULT_PRODUCT_MADE_BY = "anonymous";
    public static final int DEFAULT_PRODUCT_UOM = 0;
    public static final int DEFAULT_PRODUCT_PACK_SIZE = 0;
    public static final int DEFAULT_PRODUCT_SHELF_LIFE = 0;
    public static final int DEFAULT_PRODUCT_CATEGORY = 0;
    public static final double DEFAULT_PRODUCT_PRICE_AVERAGE = 0.00;
    public static final Uri DEFAULT_FB_IMAGE_STORAGE_URI = Uri.parse("");
    public static final long DEFAULT_COMM_CREATE_DATE = 0;
    public static final long DEFAULT_COMM_LAST_UPDATE = 0;

    /* ***********************
     * ActivityDetailProd *
     *************************/

    /* Screen titles */
    public static final int ACTIVITY_TITLE_PRODUCT_VIEW = 1;
    public static final int ACTIVITY_TITLE_PRODUCT_EDIT = 2;
    public static final int ACTIVITY_TITLE_PRODUCT_ADD = 3;

    /* Tab indices */
    public static final int TAB_COMM_PRODUCTS = 0;
    public static final int TAB_MY_PRODUCTS = 1;

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
    /*
     * This field is updated when the user is logged in and used application wide constant.
     * To observe, Constants.observe(lifeCycleOwner, observerName);
     * To update, call Constants.getUId().setValue(userID);
     * see: https://developer.android.com/topic/libraries/architecture/livedata
     */
    private static MutableLiveData<String> USER_ID;
    public static MutableLiveData<String> getUserId() {
        if(USER_ID == null) {
            USER_ID = new MutableLiveData<>();
        }
        return USER_ID;
    }

    /* ***************
     * Room Database *
     *****************/
    public static final String LOCAL_DATABASE_NAME = "tkm_local_database";

    /*
     * Table community_product - Stores information that is generic to the product. Note: These
     * constants match the field names as mapped in Firebase where possible. Whilst it is not necessary
     * to duplicate them from the Firebase fields above it does provides greater code reading ease,
     *  plus it offers a direct relationship from firebase fields to ROOM fields.
     */
    public static final String TABLE_PRODUCT_COMM = "product_community";
    public static final String TABLE_PRODUCT_COMM_ID = "_id";

    /* Generic 'community product' fields. */
    public static final String TABLE_PRODUCT_COMM_DESCRIPTION = PRODUCT_COMM_DESCRIPTION_KEY;
    public static final String TABLE_PRODUCT_COMM_FIELD_MADE_BY = PRODUCT_COMM_MADE_BY_KEY;
    public static final String TABLE_PRODUCT_COMM_FIELD_CATEGORY = PRODUCT_COMM_CATEGORY_KEY;
    public static final String TABLE_PRODUCT_COMM_SHELF_LIFE = PRODUCT_COMM_SHELF_LIFE_KEY;
    public static final String TABLE_PRODUCT_COMM_PACK_SIZE = PRODUCT_COMM_PACK_SIZE_KEY;
    public static final String TABLE_PRODUCT_COMM_UNIT_OF_MEASURE = PRODUCT_COMM_UNIT_OF_MEASURE_KEY;
    public static final String TABLE_PRODUCT_COMM_PRICE_AVE = PRODUCT_COMM_PRICE_AVE_KEY;
    public static final String TABLE_PRODUCT_COMM_CREATED_BY = PRODUCT_COMM_CREATED_BY_KEY;
    public static final String TABLE_PRODUCT_COMM_CREATE_DATE = PRODUCT_COMM_CREATE_DATE_KEY;
    public static final String TABLE_PRODUCT_COMM_LAST_UPDATE = PRODUCT_COMM_LAST_UPDATE_KEY;

    /* Table product_my - Stores product information specific to the user/member. */
    // User product fields
    public static final String TABLE_PRODUCT_MY = "product_my";
    public static final String TABLE_PRODUCT_MY_ID = "_id";
    public static final String TABLE_PRODUCT_MY_COMM_ID = "_comm_id";
    public static final String TABLE_PRODUCT_MY_LOCAL_IMAGE_URI = PRODUCT_MY_LOCAL_IMAGE_URI_KEY;
    public static final String TABLE_PRODUCT_MY_FB_STORAGE_IMAGE_URI = PRODUCT_MY_FB_STORAGE_IMAGE_URI_KEY;
    public static final String TABLE_PRODUCT_MY_LOCATION_ROOM = PRODUCT_MY_LOCATION_ROOM_KEY;
    public static final String TABLE_PRODUCT_MY_LOCATION_IN_ROOM = PRODUCT_MY_LOCATION_IN_ROOM_KEY;
    public static final String TABLE_PRODUCT_MY_RETAILER = PRODUCT_MY_RETAILER_KEY;
    public static final String TABLE_PRODUCT_MY_PACK_PRICE = PRODUCT_MY_PACK_PRICE_KEY;
    public static final String TABLE_PRODUCT_MY_FB_REFERENCE_KEY = PRODUCT_MY_FB_REFERENCE_KEY;
    public static final String TABLE_PRODUCT_MY_FB_USED_PRODUCT_KEY = PRODUCT_MY_FB_USED_PRODUCT_KEY;
    public static final String TABLE_PRODUCT_MY_CREATE_DATE = PRODUCT_MY_CREATE_DATE_KEY;
    public static final String TABLE_PRODUCT_MY_LAST_UPDATE = PRODUCT_MY_LAST_UPDATE_KEY;
}
