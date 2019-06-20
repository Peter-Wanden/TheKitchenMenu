package com.example.peter.thekitchenmenu.app;

import androidx.lifecycle.MutableLiveData;

public abstract class Constants {

    private static final String TAG = Constants.class.getSimpleName();
    // TODO - Move constants to their respective packages

    /* **********
     * PRODUCTS *
     ************/

    /* Key values for a product_viewer_identity */
    public static final String PRODUCT_KEY = "current_product_key";
    public static final String PRODUCT_REMOTE_REFERENCE_KEY = "product_fb_reference_key";
    public static final String PRODUCT_IS_CREATOR_KEY = "product_is_owner_key";

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
    public static final String FB_COLLECTION_FAVORITE_PRODUCTS = "/collection_favorite_products";
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
