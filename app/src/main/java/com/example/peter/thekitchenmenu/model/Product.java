package com.example.peter.thekitchenmenu.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.peter.thekitchenmenu.app.Constants;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Products")
public class Product implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Exclude // Excludes id field for Firebase
    private int mProductId;

    @ColumnInfo(name = "FBProductId")
    @Exclude
    private String mFbProductReferenceKey;

    @Exclude
    private String mFbUsedProductsUserKey;

    @ColumnInfo(name = "Description")
    private String mDescription;

    @ColumnInfo(name = "Made_By")
    private String mMadeBy;

    @ColumnInfo(name = "Retailer")
    private String mRetailer;

    @ColumnInfo(name = "Unit_of_Measure")
    private int mUnitOfMeasure;

    @ColumnInfo(name = "Pack_Size")
    private int mPackSize;

    @ColumnInfo(name = "Shelf_Life")
    private int mShelfLife;

    @ColumnInfo(name = "Location_Room")
    private String mLocationRoom;

    @ColumnInfo(name = "Location_in_Room")
    private String mLocationInRoom;

    @ColumnInfo(name = "Category")
    private int mCategory;

    @ColumnInfo(name = "Pack_Price")
    private double mPackPrice;

    @ColumnInfo
    private double mPackPriceAverage;

    @ColumnInfo(name = "Local_Image_Uri")
    @android.support.annotation.NonNull
    private String mLocalImageUri = "";

    @ColumnInfo(name = "Fb_Stroage_Image_Uri")
    private String mFbStorageImageUri = "";

    @ColumnInfo(name = "Created_by")
    private String mCreatedBy;

    @Ignore
    /* Constructor for Firebase */
    public Product(String description,
                   String madeBy,
                   String fbProductReferenceKey,
                   String fbUsedProductsUserKey,
                   String retailer,
                   int unitOfMeasure,
                   int packSize,
                   int shelfLife,
                   String locationRoom,
                   String locationInRoom,
                   int category,
                   double packPrice,
                   double packPriceAverage,
                   Uri localImageUri,
                   Uri fbStorageImageUri,
                   String createdBy) {

        this.mDescription = description;
        this.mMadeBy = madeBy;
        this.mFbProductReferenceKey = fbProductReferenceKey;
        this.mFbUsedProductsUserKey = fbUsedProductsUserKey;
        this.mRetailer = retailer;
        this.mUnitOfMeasure = unitOfMeasure;
        this.mPackSize = packSize;
        this.mShelfLife = shelfLife;
        this.mLocationRoom = locationRoom;
        this.mLocationInRoom = locationInRoom;
        this.mCategory = category;
        this.mPackPrice = packPrice;
        this.mPackPriceAverage = packPriceAverage;
        this.mLocalImageUri = localImageUri.toString();
        this.mFbStorageImageUri = fbStorageImageUri.toString();
        this.mCreatedBy = createdBy;
    }

    /* Empty constructor as required by Firebase */
    public Product() {
    }

    /* Constructor for the local database*/
    public Product(int productId,
                   String fbProductReferenceKey,
                   String fbUsedProductsUserKey,
                   String description,
                   String madeBy,
                   String retailer,
                   int unitOfMeasure,
                   int packSize,
                   int shelfLife,
                   String locationRoom,
                   String locationInRoom,
                   int category,
                   double packPrice,
                   double mPackPriceAverage,
                   Uri localImageUri,
                   Uri fbStorageImageUri,
                   String createdBy) {

        this.mProductId = productId;
        this.mFbProductReferenceKey = fbProductReferenceKey;
        this.mFbUsedProductsUserKey = fbUsedProductsUserKey;
        this.mDescription = description;
        this.mMadeBy = madeBy;
        this.mRetailer = retailer;
        this.mUnitOfMeasure = unitOfMeasure;
        this.mPackSize = packSize;
        this.mShelfLife = shelfLife;
        this.mLocationRoom = locationRoom;
        this.mLocationInRoom = locationInRoom;
        this.mCategory = category;
        this.mPackPrice = packPrice;
        this.mPackPriceAverage = mPackPriceAverage;
        this.mLocalImageUri = localImageUri.toString();
        this.mFbStorageImageUri = fbStorageImageUri.toString();
        this.mCreatedBy = createdBy;
    }


    private Product(Parcel in) {
        mProductId = in.readInt();
        mFbProductReferenceKey = in.readString();
        mFbUsedProductsUserKey = in.readString();
        mDescription = in.readString();
        mMadeBy = in.readString();
        mRetailer = in.readString();
        mUnitOfMeasure = in.readInt();
        mPackSize = in.readInt();
        mShelfLife = in.readInt();
        mLocationRoom = in.readString();
        mLocationInRoom = in.readString();
        mCategory = in.readInt();
        mPackPrice = in.readDouble();
        mPackPriceAverage = in.readDouble();
        mLocalImageUri = in.readString();
        mFbStorageImageUri = in.readString();
        mCreatedBy = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(mProductId);
        parcel.writeString(mFbProductReferenceKey);
        parcel.writeString(mFbUsedProductsUserKey);
        parcel.writeString(mDescription);
        parcel.writeString(mMadeBy);
        parcel.writeString(mRetailer);
        parcel.writeInt(mUnitOfMeasure);
        parcel.writeInt(mPackSize);
        parcel.writeInt(mShelfLife);
        parcel.writeString(mLocationRoom);
        parcel.writeString(mLocationInRoom);
        parcel.writeInt(mCategory);
        parcel.writeDouble(mPackPrice);
        parcel.writeDouble(mPackPriceAverage);
        parcel.writeString(mLocalImageUri);
        parcel.writeString(mFbStorageImageUri);
        parcel.writeString(mCreatedBy);
    }

    @Exclude
    public Map<String, Object> baseProductToMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put(Constants.PRODUCT_BASE_DESCRIPTION_KEY, mDescription);
        result.put(Constants.PRODUCT_BASE_MADE_BY_KEY, mMadeBy);
        result.put(Constants.PRODUCT_BASE_CATEGORY_KEY, mCategory);
        result.put(Constants.PRODUCT_BASE_SHELF_LIFE_KEY, mShelfLife);
        result.put(Constants.PRODUCT_BASE_PACK_SIZE_KEY, mPackSize);
        result.put(Constants.PRODUCT_BASE_UNIT_OF_MEASURE_KEY, mUnitOfMeasure);
        result.put(Constants.PRODUCT_BASE_PRICE_AVE_KEY, mPackPriceAverage);
        result.put(Constants.PRODUCT_BASE_CREATED_BY_KEY, mCreatedBy);
        result.put(Constants.PRODUCT_USER_FB_STORAGE_IMAGE_URI_KEY, mFbStorageImageUri);

        return result;
    }

    @Exclude
    public Map<String, Object> userFieldsToMap() {

        HashMap<String, Object> result = new HashMap<>();

        // All fields are required
        // Base fields
        result.put(Constants.PRODUCT_BASE_DESCRIPTION_KEY, mDescription);
        result.put(Constants.PRODUCT_BASE_MADE_BY_KEY, mMadeBy);
        result.put(Constants.PRODUCT_BASE_CATEGORY_KEY, mCategory);
        result.put(Constants.PRODUCT_BASE_SHELF_LIFE_KEY, mShelfLife);
        result.put(Constants.PRODUCT_BASE_PACK_SIZE_KEY, mPackSize);
        result.put(Constants.PRODUCT_BASE_UNIT_OF_MEASURE_KEY, mUnitOfMeasure);
        result.put(Constants.PRODUCT_BASE_PRICE_AVE_KEY, mPackPriceAverage);
        result.put(Constants.PRODUCT_BASE_CREATED_BY_KEY, mCreatedBy);
        result.put(Constants.PRODUCT_USER_FB_STORAGE_IMAGE_URI_KEY, mFbStorageImageUri);
        // UserFields
        result.put(Constants.PRODUCT_USER_FB_REFERENCE_KEY, mFbProductReferenceKey);
        result.put(Constants.PRODUCT_USER_FB_USED_USER_KEY, mFbUsedProductsUserKey);
        result.put(Constants.PRODUCT_USER_RETAILER_KEY, mRetailer);
        result.put(Constants.PRODUCT_USER_LOCATION_ROOM_KEY, mLocationRoom);
        result.put(Constants.PRODUCT_USER_LOCATION_IN_ROOM_KEY, mLocationInRoom);
        result.put(Constants.PRODUCT_USER_PACK_PRICE_KEY, mPackPrice);
        result.put(Constants.PRODUCT_USER_LOCAL_IMAGE_URI_KEY, mLocalImageUri);

        return result;
    }

    /* Getters and setters */
    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        this.mProductId = productId;
    }

    public String getFbProductReferenceKey() {
        return mFbProductReferenceKey;
    }

    public void setFbProductReferenceKey(String fbProductReferenceKey) {
        this.mFbProductReferenceKey
                = fbProductReferenceKey;
    }

    public String getFbUsedProductsUserKey() {
        return mFbUsedProductsUserKey;
    }

    public void setFbUsedProductsUserKey(String fbUsedProductsUserKey) {
        this.mFbUsedProductsUserKey
                = fbUsedProductsUserKey;
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

    public String getRetailer() {
        return mRetailer;
    }

    public void setRetailer(String retailer) {
        this.mRetailer = retailer;
    }

    public int getUnitOfMeasure() {
        return mUnitOfMeasure;
    }

    public void setUnitOfMeasure(int unitOfMeasure) {
        this.mUnitOfMeasure = unitOfMeasure;
    }

    public int getPackSize() {
        return mPackSize;
    }

    public void setPackSize(int packSize) {
        this.mPackSize = packSize;
    }

    public int getShelfLife() {
        return mShelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.mShelfLife = shelfLife;
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

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        this.mCategory = category;
    }

    public double getPackPrice() {
        return mPackPrice;
    }

    public void setPackPrice(double packPrice) {
        this.mPackPrice = packPrice;
    }

    public double getPackPriceAverage() {
        return mPackPriceAverage;
    }

    public void setPackPriceAverage (double packPriceAverage) {
        this.mPackPriceAverage = packPriceAverage;
    }

    public String getLocalImageUri() {
        return mLocalImageUri;
    }

    public void setLocalImageUri(String localImageUri) {
        this.mLocalImageUri = localImageUri;
    }

    public String getFbStorageImageUri() {
        return mFbStorageImageUri;
    }

    public void setFbStorageImageUri(String fbStorageImageUri) {
        this.mFbStorageImageUri = fbStorageImageUri;
    }

    public String getCreatedBy() {
        return mCreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.mCreatedBy = createdBy;
    }
}
