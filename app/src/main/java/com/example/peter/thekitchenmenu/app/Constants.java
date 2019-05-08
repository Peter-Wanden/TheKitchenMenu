package com.example.peter.thekitchenmenu.app;


import androidx.lifecycle.MutableLiveData;

public abstract class Constants {

    private static final String TAG = Constants.class.getSimpleName();
    // TODO - Move constants to their respective packages

    /* **********
     * PRODUCTS *
     ************/

    /* Key values for a product_uneditable */
    public static final String PRODUCT_KEY = "current_product_key";
    public static final String PRODUCT_REMOTE_REFERENCE_KEY = "product_fb_reference_key";
    public static final String PRODUCT_IS_CREATOR_KEY = "product_is_owner_key";

    /* Default values for a ProductModel */
    public static final int DEFAULT_PROD_MY_ID = 0;
    public static final String DEFAULT_REMOTE_REF_ID = "no_key";
    public static final String DEFAULT_REMOTE_USED_PRODUCT_ID = "no_used_product_id";
    public static final String DEFAULT_PRODUCT_RETAILER = "no_retailer";
    public static final String DEFAULT_PRODUCT_LOC = "no_location";
    public static final String DEFAULT_PRODUCT_LOC_IN_ROOM = "no_location_in_room";
    public static final double DEFAULT_PRODUCT_PRICE = 0.00;
    public static final String DEFAULT_LOCAL_IMAGE_URI = "";
    public static final long DEFAULT_MY_CREATE_DATE = 0;
    public static final long DEFAULT_MY_LAST_UPDATE = 0;

    public static final int DEFAULT_LOCAL_PROD_COMM_ID = 0;
    public static final String DEFAULT_PRODUCT_DESCRIPTION = "no_description";
    public static final String DEFAULT_PRODUCT_MADE_BY = "anonymous";
    public static final int DEFAULT_PRODUCT_CATEGORY = 0;
    public static final int DEFAULT_PRODUCT_SHELF_LIFE = 0;
    public static final int DEFAULT_PRODUCT_PACK_SIZE = 0;
    public static final int DEFAULT_PRODUCT_UOM = 0;
    public static final double DEFAULT_PRODUCT_PRICE_AVERAGE = 0.0;
    public static final String DEFAULT_PRODUCT_CREATED_BY = "anonymous";

    public static final String DEFAULT_FB_PRODUCT_ID = "no_fd_id";
    public static final String DEFAULT_REMOTE_IMAGE_STORAGE_URI = "";
    public static final long DEFAULT_COMM_CREATE_DATE = 0;
    public static final long DEFAULT_COMM_LAST_UPDATE = 0;

    /* ***********************
     * ProductDetail *
     *************************/

    /* *************
     * Permissions *
     ***************/
    public static final int REQUEST_STORAGE_PERMISSION = 2;
    public static final int REQUEST_IMAGE_MEDIA_STORE = 3;
    public static final int REQUEST_IMAGE_PICKER = 4;
    public static final int REQUEST_CROP_PICTURE = 5;

    /* ***************
     * File provider *
     *****************/
    public static final String FILE_PROVIDER_AUTHORITY
            = "com.example.peter.thekitchenmenu.fileprovider";

    /* *******************
     * Firebase database *
     *********************/
    public static final String REMOTE_PRODUCT_LOCATION = "/collection_products/";
    public static final String REMOTE_USER_LOCATION = "/collection_users/";
    public static final String FB_COLLECTION_USED_PRODUCTS = "/collection_used_products";
    public static final int REQUEST_CODE_SIGN_IN = 5;
    public static final String FB_STORAGE_IMAGE_REFERENCE = "/collection_product_images/";

    /* *******
     * Users *
     *********/
    public static final String ANONYMOUS = "anonymous";
    public static final String USER_ID_KEY = "user_id_key";
    /*
     * This field is updated when the user logs in / out.
     * To observe, Constants.observe(lifeCycleOwner, observerName);
     * To update, call Constants.getUId().setPackMeasurement(userID);
     * see: https://developer.android.com/topic/libraries/architecture/livedata
     */
    private static MutableLiveData<String> USER_ID;
    public static MutableLiveData<String> getUserId() {
        if(USER_ID == null) {
            USER_ID = new MutableLiveData<>();
            USER_ID.postValue(ANONYMOUS);
        }
        return USER_ID;
    }
}
