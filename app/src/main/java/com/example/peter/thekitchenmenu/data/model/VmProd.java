package com.example.peter.thekitchenmenu.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;

import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PROD_MY_ID;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_REMOTE_REF_ID;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_FB_USED_PRODUCT_ID;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PRODUCT_RETAILER;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PRODUCT_LOC;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PRODUCT_LOC_IN_ROOM;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PRODUCT_PRICE;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_LOCAL_IMAGE_URI;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_MY_CREATE_DATE;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_MY_LAST_UPDATE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_CATEGORY;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_CREATED_BY;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_CREATE_DATE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_DESC;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_LAST_UPDATE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_MADE_BY;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_PACK_SIZE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_PRICE_AVE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_REMOTE_REF_ID;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_SHELF_LIFE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_UNIT_OF_MEASURE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_REMOTE_IMAGE_URI;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.PROD_MY_CREATE_DATE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.PROD_MY_LAST_UPDATE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.PROD_MY_LOCAL_IMAGE_URI;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.PROD_MY_LOC_IN_ROOM;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.PROD_MY_LOC_ROOM;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.PROD_MY_PACK_PRICE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.PROD_MY_RETAILER;
import static com.example.peter.thekitchenmenu.data.entity.DmProdMy.PROD_MY_USED_REMOTE_ID;

import java.util.HashMap;
import java.util.Map;

public class VmProd implements Parcelable {

    // DmProdMy fields.
    private int mProdMyId;
    private String mRemoteProdMyRefKey;
    private String mFbUsedProdUserKey;
    private String mRetailer;
    private String mLocationRoom;
    private String mLocationInRoom;
    private double mPackPrice;
    private String mLocalImageUri;
    private long mMyCreateDate;
    private long mMyLastUpdate;

    // ProductComm fields.
    private int mProdCommId;
    private String mRemoteProdCommRefKey;
    private String mDescription;
    private String mMadeBy;
    private int mCategory;
    private int mShelfLife;
    private int mPackSize;
    private int mUnitOfMeasure;
    private double mPackAvePrice;
    private String mCreatedBy;
    private String mFbStorageImageUri;
    private long mCommCreateDate;
    private long mCommLastUpdate;

    // TODO - create getters and setters for the different ID's and references.

    public VmProd(){}

    // Constructor for merging data models
    public VmProd(DmProdMy dmProdMy, DmProdComm dmProdComm){
        this.mProdMyId = dmProdMy.getId();
        this.mRemoteProdMyRefKey = dmProdMy.getFbProductReferenceKey();
        this.mFbUsedProdUserKey = dmProdMy.getFbUsedProductsUserKey();
        this.mRetailer = dmProdMy.getRetailer();
        this.mLocationRoom = dmProdMy.getLocationRoom();
        this.mLocationInRoom = dmProdMy.getLocationInRoom();
        this.mPackPrice = dmProdMy.getPackPrice();
        this.mLocalImageUri = dmProdMy.getLocalImageUri();
        this.mMyCreateDate = dmProdMy.getMyCreateDate();
        this.mMyLastUpdate = dmProdMy.getMyLastUpdate();

        this.mProdCommId = dmProdComm.getId();
        this.mRemoteProdCommRefKey = dmProdComm.getFbProductReferenceKey();
        this.mDescription = dmProdComm.getDescription();
        this.mMadeBy = dmProdComm.getMadeBy();
        this.mCategory = dmProdComm.getCategory();
        this.mShelfLife = dmProdComm.getShelfLife();
        this.mPackSize = dmProdComm.getPackSize();
        this.mUnitOfMeasure = dmProdComm.getUnitOfMeasure();
        this.mPackAvePrice = dmProdComm.getPackAvePrice();
        this.mCreatedBy = dmProdComm.getCreatedBy();
        this.mFbStorageImageUri = dmProdComm.getFbStorageImageUri();
        this.mCommCreateDate = dmProdComm.getCommCreateDate();
        this.mCommLastUpdate = dmProdComm.getCommLastUpdate();
    }

    // Constructor for creating a view model of a DmProdComm only
    public VmProd(DmProdComm dmProdComm) {
        this.mProdMyId = DEFAULT_PROD_MY_ID;
        this.mRemoteProdMyRefKey = DEFAULT_REMOTE_REF_ID;
        this.mFbUsedProdUserKey = DEFAULT_FB_USED_PRODUCT_ID;
        this.mRetailer = DEFAULT_PRODUCT_RETAILER;
        this.mLocationRoom = DEFAULT_PRODUCT_LOC;
        this.mLocationInRoom = DEFAULT_PRODUCT_LOC_IN_ROOM;
        this.mPackPrice = DEFAULT_PRODUCT_PRICE;
        this.mLocalImageUri = DEFAULT_LOCAL_IMAGE_URI;
        this.mMyCreateDate = DEFAULT_MY_CREATE_DATE;
        this.mMyLastUpdate = DEFAULT_MY_LAST_UPDATE;

        this.mProdCommId = dmProdComm.getId();
        this.mRemoteProdCommRefKey = dmProdComm.getFbProductReferenceKey();
        this.mDescription = dmProdComm.getDescription();
        this.mMadeBy = dmProdComm.getMadeBy();
        this.mCategory = dmProdComm.getCategory();
        this.mShelfLife = dmProdComm.getShelfLife();
        this.mPackSize = dmProdComm.getPackSize();
        this.mUnitOfMeasure = dmProdComm.getUnitOfMeasure();
        this.mPackAvePrice = dmProdComm.getPackAvePrice();
        this.mCreatedBy = dmProdComm.getCreatedBy();
        this.mFbStorageImageUri = dmProdComm.getFbStorageImageUri();
        this.mCommCreateDate = dmProdComm.getCommCreateDate();
        this.mCommLastUpdate = dmProdComm.getCommLastUpdate();
    }

    public VmProd(
            int productMyId,
            String fbProductReferenceKey,
            String fbUsedProductsUserKey,
            String retailer,
            String locationRoom,
            String locationInRoom,
            double packPrice,
            String localImageUri,
            long myCreateDate,
            long myLastUpdate,
            int prodCommId,
            String remoteRefId,
            String description,
            String madeBy,
            int category,
            int shelfLife,
            int packSize,
            int unitOfMeasure,
            double mPackAvePrice,
            String createdBy,
            String fbStorageImageUri,
            long commCreateDate,
            long commLastUpdate) {

        this.mProdMyId = productMyId;
        this.mFbUsedProdUserKey = fbUsedProductsUserKey;
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
        this.mPackAvePrice = mPackAvePrice;
        this.mCreatedBy = createdBy;
        this.mFbStorageImageUri = fbStorageImageUri;
        this.mCommCreateDate = commCreateDate;
        this.mCommLastUpdate = commLastUpdate;
        this.mProdCommId = prodCommId;
        this.mRemoteProdMyRefKey = fbProductReferenceKey;
    }

    protected VmProd(Parcel in) {
        mProdMyId = in.readInt();
        mRemoteProdMyRefKey = in.readString();
        mFbUsedProdUserKey = in.readString();
        mRetailer = in.readString();
        mLocationRoom = in.readString();
        mLocationInRoom = in.readString();
        mPackPrice = in.readDouble();
        mLocalImageUri = in.readString();
        mMyCreateDate = in.readLong();
        mMyLastUpdate = in.readLong();
        mProdCommId = in.readInt();
        mRemoteProdCommRefKey = in.readString();
        mDescription = in.readString();
        mMadeBy = in.readString();
        mCategory = in.readInt();
        mShelfLife = in.readInt();
        mPackSize = in.readInt();
        mUnitOfMeasure = in.readInt();
        mPackAvePrice = in.readDouble();
        mCreatedBy = in.readString();
        mFbStorageImageUri = in.readString();
        mCommCreateDate = in.readLong();
        mCommLastUpdate = in.readLong();
    }

    public static final Creator<VmProd> CREATOR = new Creator<VmProd>() {
        @Override
        public VmProd createFromParcel(Parcel in) {
            return new VmProd(in);
        }

        @Override
        public VmProd[] newArray(int size) {
            return new VmProd[size];
        }
    };

    // HashMap for FireBase community products information Map
    public Map<String, Object> commProductToMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put(PROD_COMM_DESC, mDescription);
        result.put(PROD_COMM_MADE_BY, mMadeBy);
        result.put(PROD_COMM_CATEGORY, mCategory);
        result.put(PROD_COMM_SHELF_LIFE, mShelfLife);
        result.put(PROD_COMM_PACK_SIZE, mPackSize);
        result.put(PROD_COMM_UNIT_OF_MEASURE, mUnitOfMeasure);
        result.put(PROD_COMM_PRICE_AVE, mPackAvePrice);
        result.put(PROD_COMM_CREATED_BY, mCreatedBy);
        result.put(PROD_COMM_REMOTE_IMAGE_URI, mFbStorageImageUri);
        result.put(PROD_COMM_CREATE_DATE, mCommCreateDate);
        result.put(PROD_COMM_LAST_UPDATE, mCommLastUpdate);

        return result;
    }

    // HashMap for FireBase user products information Map
    public Map<String, Object> userFieldsToMap() {

        HashMap<String, Object> result = new HashMap<>();

        // All fields are required
        // Community product fields
        result.put(PROD_COMM_DESC, mDescription);
        result.put(PROD_COMM_MADE_BY, mMadeBy);
        result.put(PROD_COMM_CATEGORY, mCategory);
        result.put(PROD_COMM_SHELF_LIFE, mShelfLife);
        result.put(PROD_COMM_PACK_SIZE, mPackSize);
        result.put(PROD_COMM_UNIT_OF_MEASURE, mUnitOfMeasure);
        result.put(PROD_COMM_PRICE_AVE, mPackAvePrice);
        result.put(PROD_COMM_CREATED_BY, mCreatedBy);
        result.put(PROD_COMM_REMOTE_IMAGE_URI, mFbStorageImageUri);
        result.put(PROD_COMM_CREATE_DATE, mCommCreateDate);
        result.put(PROD_COMM_LAST_UPDATE, mCommLastUpdate);

        // My product specific fields
        result.put(PROD_MY_USED_REMOTE_ID, mFbUsedProdUserKey);
        result.put(PROD_MY_RETAILER, mRetailer);
        result.put(PROD_MY_LOC_ROOM, mLocationRoom);
        result.put(PROD_MY_LOC_IN_ROOM, mLocationInRoom);
        result.put(PROD_MY_PACK_PRICE, mPackPrice);
        result.put(PROD_MY_LOCAL_IMAGE_URI, mLocalImageUri);
        result.put(PROD_MY_CREATE_DATE, mMyCreateDate);
        result.put(PROD_MY_LAST_UPDATE, mMyLastUpdate);
        result.put(PROD_COMM_REMOTE_REF_ID, mRemoteProdMyRefKey);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mProdMyId);
        parcel.writeString(mRemoteProdMyRefKey);
        parcel.writeString(mFbUsedProdUserKey);
        parcel.writeString(mRetailer);
        parcel.writeString(mLocationRoom);
        parcel.writeString(mLocationInRoom);
        parcel.writeDouble(mPackPrice);
        parcel.writeString(mLocalImageUri);
        parcel.writeLong(mMyCreateDate);
        parcel.writeLong(mMyLastUpdate);
        parcel.writeInt(mProdCommId);
        parcel.writeString(mRemoteProdCommRefKey);
        parcel.writeString(mDescription);
        parcel.writeString(mMadeBy);
        parcel.writeInt(mCategory);
        parcel.writeInt(mShelfLife);
        parcel.writeInt(mPackSize);
        parcel.writeInt(mUnitOfMeasure);
        parcel.writeDouble(mPackAvePrice);
        parcel.writeString(mCreatedBy);
        parcel.writeString(mFbStorageImageUri);
        parcel.writeLong(mCommCreateDate);
        parcel.writeLong(mCommLastUpdate);
    }

    @Override
    public String toString() {
        return
                this.mProdMyId + ", " +
                this.mFbUsedProdUserKey + ", " +
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
                this.mPackAvePrice + ", " +
                this.mCreatedBy + ", " +
                this.mFbStorageImageUri + ", " +
                this.mCommCreateDate + ", " +
                this.mCommLastUpdate + ", " +
                this.mProdCommId + ", " +
                this.mRemoteProdMyRefKey;
    }

    /* Getters and setters */
    public int getProductMyId() {
        return this.mProdMyId;
    }

    public void setProductMyId(int productMyId) {
        this.mProdMyId = productMyId;
    }

    public String getFbUsedProductsUserKey() {
        return mFbUsedProdUserKey;
    }

    public void setFbUsedProductsUserKey(String fbUsedProductsUserKey) {
        this.mFbUsedProdUserKey = fbUsedProductsUserKey;
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

    public double getPackAvePrice() {
        return mPackAvePrice;
    }

    public void setPackAvePrice(double packAvePrice) {
        this.mPackAvePrice = packAvePrice;
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
        return this.mProdCommId;
    }

    public void setProductCommId(int productCommId) {
        this.mProdCommId = productCommId;
    }

    public String getFbProductReferenceKey() {
        return mRemoteProdMyRefKey;
    }

    public void setFbProductReferenceKey(String fbProductReferenceKey) {
        this.mRemoteProdMyRefKey = fbProductReferenceKey;
    }
}
