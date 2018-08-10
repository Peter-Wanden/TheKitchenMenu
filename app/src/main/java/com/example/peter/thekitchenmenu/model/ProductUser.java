package com.example.peter.thekitchenmenu.model;

import com.example.peter.thekitchenmenu.app.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This class collects information about a product that is specific to a particular user.
 */
public class ProductUser {

    // The unique reference for to which this user product information relates to
    private String docRefProductBase;
    // The users ID
    private String userId;
    // Uri to product image on users device
    private String localImageUri;
    // Firebase storage image Uri
    private String fbStorageImageUri;
    private String locationRoom;
    private String locationInRoom;
    private String retailer;
    private double packPrice;

    public ProductUser() {}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.USER_ID_KEY, userId);
        result.put(Constants.PRODUCT_BASE_DOC_REF_KEY, docRefProductBase);
        result.put(Constants.PRODUCT_USER_LOCAL_IMAGE_URI_KEY, localImageUri);
        result.put(Constants.PRODUCT_USER_FB_STORAGE_IMAGE_URI_KEY, fbStorageImageUri);
        result.put(Constants.PRODUCT_USER_LOCATION_ROOM_KEY, locationRoom);
        result.put(Constants.PRODUCT_USER_LOCATION_IN_ROOM_KEY, locationInRoom);
        result.put(Constants.PRODUCT_USER_RETAILER_KEY, retailer);
        result.put(Constants.PRODUCT_USER_PACK_PRICE_KEY, packPrice);

        return result;
    }

    public String getDocRefProductBase() {
        return docRefProductBase;
    }

    public void setDocRefProductBase(String docRefProductBase) {
        this.docRefProductBase = docRefProductBase;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(String localImageUri) {
        this.localImageUri = localImageUri;
    }

    public String getFbStorageImageUri() {
        return fbStorageImageUri;
    }

    public void setFbStorageImageUri(String fbStorageImageUri) {
        this.fbStorageImageUri = fbStorageImageUri;
    }

    public String getLocationRoom() {
        return locationRoom;
    }

    public void setLocationRoom(String locationRoom) {
        this.locationRoom = locationRoom;
    }

    public String getLocationInRoom() {
        return locationInRoom;
    }

    public void setLocationInRoom(String locationInRoom) {
        this.locationInRoom = locationInRoom;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public double getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(double packPrice) {
        this.packPrice = packPrice;
    }
}
