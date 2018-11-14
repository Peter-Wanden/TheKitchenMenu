package com.example.peter.thekitchenmenu.data.model;

import android.net.Uri;

import com.example.peter.thekitchenmenu.app.Constants;

import java.util.HashMap;
import java.util.Map;

public class UCProduct {

    // ProductMy fields.
    private int mProductMyId;
    private String mFbUsedProductsUserKey;
    private String mRetailer;
    private String mLocationRoom;
    private String mLocationInRoom;
    private double mPackPrice;
    private String mLocalImageUri;
    private long mMyCreateDate;
    private long mMyLastUpdate;

    // ProductComm fields.
    private String mDescription;
    private String mMadeBy;
    private int mCategory;
    private int mShelfLife;
    private int mPackSize;
    private int mUnitOfMeasure;
    private double mPackPriceAverage;
    private String mCreatedBy;
    private String mFbStorageImageUri;
    private long mCommCreateDate;
    private long mCommLastUpdate;

    // Fields common between ProductMy and ProductCommunity
    private int mProductCommId;
    private String mFbProductReferenceKey;

    public UCProduct(){}

    public UCProduct(
            int productMyId,
            String fbUsedProductsUserKey,
            String retailer,
            String locationRoom,
            String locationInRoom,
            double packPrice,
            String localImageUri,
            long myCreateDate,
            long myLastUpdate,
            String description,
            String madeBy,
            int category,
            int shelfLife,
            int packSize,
            int unitOfMeasure,
            double mPackPriceAverage,
            String createdBy,
            String fbStorageImageUri,
            long commCreateDate,
            long commLastUpdate,
            int productCommId,
            String fbProductReferenceKey) {

        this.mProductMyId = productMyId;
        this.mFbUsedProductsUserKey = fbUsedProductsUserKey;
        this.mRetailer = retailer;
        this.mLocationRoom = locationRoom;
        this.mLocationInRoom = locationInRoom;
        this.mPackPrice = packPrice;
        this.mLocalImageUri = localImageUri;
        this.mMyCreateDate = myCreateDate;
        this.mMyLastUpdate = myLastUpdate;
        this.mDescription = description;
        this.mMadeBy = madeBy;
        this.mCategory = category;
        this.mShelfLife = shelfLife;
        this.mPackSize = packSize;
        this.mUnitOfMeasure = unitOfMeasure;
        this.mPackPriceAverage = mPackPriceAverage;
        this.mCreatedBy = createdBy;
        this.mFbStorageImageUri = fbStorageImageUri;
        this.mCommCreateDate = commCreateDate;
        this.mCommLastUpdate = commLastUpdate;
        this.mProductCommId = productCommId;
        this.mFbProductReferenceKey = fbProductReferenceKey;
    }


    // HashMap for FireBase community products information Map
    public Map<String, Object> commProductToMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put(Constants.PRODUCT_COMM_DESCRIPTION_KEY, mDescription);
        result.put(Constants.PRODUCT_COMM_MADE_BY_KEY, mMadeBy);
        result.put(Constants.PRODUCT_COMM_CATEGORY_KEY, mCategory);
        result.put(Constants.PRODUCT_COMM_SHELF_LIFE_KEY, mShelfLife);
        result.put(Constants.PRODUCT_COMM_PACK_SIZE_KEY, mPackSize);
        result.put(Constants.PRODUCT_COMM_UNIT_OF_MEASURE_KEY, mUnitOfMeasure);
        result.put(Constants.PRODUCT_COMM_PRICE_AVE_KEY, mPackPriceAverage);
        result.put(Constants.PRODUCT_COMM_CREATED_BY_KEY, mCreatedBy);
        result.put(Constants.PRODUCT_MY_FB_STORAGE_IMAGE_URI_KEY, mFbStorageImageUri);
        result.put(Constants.PRODUCT_COMM_CREATE_DATE_KEY, mCommCreateDate);
        result.put(Constants.PRODUCT_COMM_LAST_UPDATE_KEY, mCommLastUpdate);

        return result;
    }

    // HashMap for FireBase user products information Map
    public Map<String, Object> userFieldsToMap() {

        HashMap<String, Object> result = new HashMap<>();

        // All fields are required
        // Community product fields
        result.put(Constants.PRODUCT_COMM_DESCRIPTION_KEY, mDescription);
        result.put(Constants.PRODUCT_COMM_MADE_BY_KEY, mMadeBy);
        result.put(Constants.PRODUCT_COMM_CATEGORY_KEY, mCategory);
        result.put(Constants.PRODUCT_COMM_SHELF_LIFE_KEY, mShelfLife);
        result.put(Constants.PRODUCT_COMM_PACK_SIZE_KEY, mPackSize);
        result.put(Constants.PRODUCT_COMM_UNIT_OF_MEASURE_KEY, mUnitOfMeasure);
        result.put(Constants.PRODUCT_COMM_PRICE_AVE_KEY, mPackPriceAverage);
        result.put(Constants.PRODUCT_COMM_CREATED_BY_KEY, mCreatedBy);
        result.put(Constants.PRODUCT_MY_FB_STORAGE_IMAGE_URI_KEY, mFbStorageImageUri);
        result.put(Constants.PRODUCT_COMM_CREATE_DATE_KEY, mCommCreateDate);
        result.put(Constants.PRODUCT_COMM_LAST_UPDATE_KEY, mCommLastUpdate);

        // My product specific fields
        result.put(Constants.PRODUCT_MY_FB_USED_PRODUCT_KEY, mFbUsedProductsUserKey);
        result.put(Constants.PRODUCT_MY_RETAILER_KEY, mRetailer);
        result.put(Constants.PRODUCT_MY_LOCATION_ROOM_KEY, mLocationRoom);
        result.put(Constants.PRODUCT_MY_LOCATION_IN_ROOM_KEY, mLocationInRoom);
        result.put(Constants.PRODUCT_MY_PACK_PRICE_KEY, mPackPrice);
        result.put(Constants.PRODUCT_MY_LOCAL_IMAGE_URI_KEY, mLocalImageUri);
        result.put(Constants.PRODUCT_MY_CREATE_DATE_KEY, mMyCreateDate);
        result.put(Constants.PRODUCT_MY_LAST_UPDATE_KEY, mMyLastUpdate);
        result.put(Constants.PRODUCT_MY_FB_REFERENCE_KEY, mFbProductReferenceKey);

        return result;
    }

    @Override
    public String toString() {
        return
                this.mProductMyId + ", " +
                this.mFbUsedProductsUserKey + ", " +
                this.mRetailer + ", " +
                this.mLocationRoom + ", " +
                this.mLocationInRoom + ", " +
                this.mPackPrice + ", " +
                this.mLocalImageUri + ", " +
                this.mMyCreateDate + ", " +
                this.mMyLastUpdate + ", " +
                this.mDescription + ", " +
                this.mMadeBy + ", " +
                this.mCategory + ", " +
                this.mShelfLife + ", " +
                this.mPackSize + ", " +
                this.mUnitOfMeasure + ", " +
                this.mPackPriceAverage + ", " +
                this.mCreatedBy + ", " +
                this.mFbStorageImageUri + ", " +
                this.mCommCreateDate + ", " +
                this.mCommLastUpdate + ", " +
                this.mProductCommId + ", " +
                this.mFbProductReferenceKey;
    }

    /* Getters and setters */
    public int getProductMyId() {
        return this.mProductMyId;
    }

    public void setProductMyId(int productMyId) {
        this.mProductMyId = productMyId;
    }

    public String getFbUsedProductsUserKey() {
        return mFbUsedProductsUserKey;
    }

    public void setFbUsedProductsUserKey(String fbUsedProductsUserKey) {
        this.mFbUsedProductsUserKey = fbUsedProductsUserKey;
    }

    public String getRetailer() {
        return mRetailer;
    }

    public void setRetailer(String retailer) {
        this.mRetailer = retailer;
    }

    public String getLocationRoom() {
        return mLocationRoom;
    }

    public void setLocationRoom(String locationRoom) {
        this.mLocationRoom = locationRoom;
    }

    public String getLocationInRoom() {
        return mLocationInRoom;
    }

    public void setLocationInRoom(String locationInRoom) {
        this.mLocationInRoom = locationInRoom;
    }

    public double getPackPrice() {
        return mPackPrice;
    }

    public void setPackPrice(double packPrice) {
        this.mPackPrice = packPrice;
    }

    public String getLocalImageUri() {
        return mLocalImageUri;
    }

    public void setLocalImageUri(String localImageUri) {
        this.mLocalImageUri = localImageUri;
    }

    public long getMyCreateDate() {
        return mMyCreateDate;
    }

    public void setMyCreateDate(long myCreateDate) {
        this.mMyCreateDate = myCreateDate;
    }

    public long getMyLastUpdate() {
        return mMyLastUpdate;
    }

    public void setMyLastUpdate(long myLastUpdate) {
        mMyLastUpdate = myLastUpdate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getMadeBy() {
        return mMadeBy;
    }

    public void setMadeBy(String madeBy) {
        this.mMadeBy = madeBy;
    }

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        this.mCategory = category;
    }

    public int getShelfLife() {
        return mShelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.mShelfLife = shelfLife;
    }

    public int getPackSize() {
        return mPackSize;
    }

    public void setPackSize(int packSize) {
        this.mPackSize = packSize;
    }

    public int getUnitOfMeasure() {
        return mUnitOfMeasure;
    }

    public void setUnitOfMeasure(int unitOfMeasure) {
        this.mUnitOfMeasure = unitOfMeasure;
    }

    public double getPackPriceAverage() {
        return mPackPriceAverage;
    }

    public void setPackPriceAverage(double packPriceAverage) {
        this.mPackPriceAverage = packPriceAverage;
    }

    public String getCreatedBy() {
        return mCreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.mCreatedBy = createdBy;
    }

    public String getFbStorageImageUri() {
        return mFbStorageImageUri;
    }

    public void setFbStorageImageUri(String fbStorageImageUri) {
        this.mFbStorageImageUri = fbStorageImageUri;
    }

    public long getCommCreateDate() {
        return mCommCreateDate;
    }

    public void setCommCreateDate(long createDate) {
        this.mCommCreateDate = createDate;
    }

    public long getCommLastUpdate() {
        return mCommLastUpdate;
    }

    public void setCommLastUpdate(long commLastUpdate) {
        this.mCommLastUpdate = commLastUpdate;
    }

    public int getProductCommId() {
        return this.mProductCommId;
    }

    public void setProductCommId(int productCommId) {
        this.mProductCommId = productCommId;
    }

    public String getFbProductReferenceKey() {
        return mFbProductReferenceKey;
    }

    public void setFbProductReferenceKey(String fbProductReferenceKey) {
        this.mFbProductReferenceKey = fbProductReferenceKey;
    }
}
