package com.example.peter.thekitchenmenu.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.peter.thekitchenmenu.app.Constants;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the Product model. A POJO made flexible with Firebase and Room annotations along with
 * a fully parcelable implementation.
 */
@Entity(tableName = Constants.TABLE_PRODUCT_MY)
public class Product implements Parcelable {

    @Exclude // Excludes field from Firebase, as is only required for Room.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_ID)
    private int id;

    // Community product fields.
    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_DESCRIPTION)
    private String mDescription;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_FIELD_MADE_BY)
    private String mMadeBy;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_FIELD_CATEGORY)
    private int mCategory;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_SHELF_LIFE)
    private int mShelfLife;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_PACK_SIZE)
    private int mPackSize;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_UNIT_OF_MEASURE)
    private int mUnitOfMeasure;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_PRICE_AVE)
    private double mPackPriceAverage;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_CREATED_BY)
    private String mCreatedBy;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_CREATE_DATE)
    private long mCommCreateDate;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_COMM_LAST_UPDATE)
    private long mCommLastUpdate;

    // 'My' product information.
    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_LOCAL_IMAGE_URI)
    private String mLocalImageUri = "";

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_FB_STORAGE_IMAGE_URI)
    @android.support.annotation.NonNull
    private String mFbStorageImageUri = "";

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_LOCATION_ROOM)
    private String mLocationRoom;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_LOCATION_IN_ROOM)
    private String mLocationInRoom;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_RETAILER)
    private String mRetailer;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_PACK_PRICE)
    private double mPackPrice;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_FB_REFERENCE_KEY)
    private String mFbProductReferenceKey;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_FB_USED_PRODUCT_KEY)
    private String mFbUsedProductsUserKey;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_CREATE_DATE)
    private long mMyCreateDate;

    @ColumnInfo(name = Constants.TABLE_PRODUCT_MY_LAST_UPDATE)
    private long mMyLastUpdate;

    /* Empty constructor as required by Firebase. */
    public Product() {
    }

    /* Constructor for Firebase*/
    public Product(String fbProductCommunityKey,
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
                   String createdBy,
                   long createDateComm,
                   long lastUpdateComm,
                   long createDateMy,
                   long lastUpdateMy) {

        this.mFbProductReferenceKey = fbProductCommunityKey;
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
        this.mCommCreateDate = createDateComm;
        this.mCommLastUpdate = lastUpdateComm;
        this.mMyCreateDate = createDateMy;
        this.mMyLastUpdate = lastUpdateMy;
    }

    /* Constructor for Room */
    public Product(int id,
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
                   String createdBy,
                   long createDateComm,
                   long lastUpdateComm,
                   long createDateMy,
                   long lastUpdateMy) {

        this.id = id;
        this.mCategory = category;
        this.mCreatedBy = createdBy;
        this.mDescription = description;
        this.mFbProductReferenceKey = fbProductReferenceKey;
        this.mFbStorageImageUri = fbStorageImageUri.toString();
        this.mFbUsedProductsUserKey = fbUsedProductsUserKey;
        this.mLocalImageUri = localImageUri.toString();
        this.mLocationRoom = locationRoom;
        this.mLocationInRoom = locationInRoom;
        this.mMadeBy = madeBy;
        this.mPackPriceAverage = mPackPriceAverage;
        this.mPackPrice = packPrice;
        this.mPackSize = packSize;
        this.mRetailer = retailer;
        this.mShelfLife = shelfLife;
        this.mUnitOfMeasure = unitOfMeasure;

        // TODO - Implement the below
        this.mCommCreateDate = createDateComm;
        this.mCommLastUpdate = lastUpdateComm;
        this.mMyCreateDate = createDateMy;
        this.mMyLastUpdate = lastUpdateMy;
    }


    private Product(Parcel in) {
        id = in.readInt();
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
        mCommCreateDate = in.readLong();
        mCommLastUpdate = in.readLong();
        mMyCreateDate = in.readLong();
        mMyLastUpdate = in.readLong();
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

        parcel.writeInt(id);
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
        parcel.writeLong(mCommCreateDate);
        parcel.writeLong(mCommLastUpdate);
        parcel.writeLong(mMyCreateDate);
        parcel.writeLong(mMyLastUpdate);
    }

    @Exclude // HashMap for FireBase community products information Map
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

    @Exclude // HashMap for FireBase user products information Map
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
        result.put(Constants.PRODUCT_MY_FB_REFERENCE_KEY, mFbProductReferenceKey);
        result.put(Constants.PRODUCT_MY_FB_USED_PRODUCT_KEY, mFbUsedProductsUserKey);
        result.put(Constants.PRODUCT_MY_RETAILER_KEY, mRetailer);
        result.put(Constants.PRODUCT_MY_LOCATION_ROOM_KEY, mLocationRoom);
        result.put(Constants.PRODUCT_MY_LOCATION_IN_ROOM_KEY, mLocationInRoom);
        result.put(Constants.PRODUCT_MY_PACK_PRICE_KEY, mPackPrice);
        result.put(Constants.PRODUCT_MY_LOCAL_IMAGE_URI_KEY, mLocalImageUri);
        result.put(Constants.PRODUCT_MY_CREATE_DATE_KEY, mMyCreateDate);
        result.put(Constants.PRODUCT_MY_LAST_UPDATE_KEY, mMyLastUpdate);

        return result;
    }

    @Override
    public String toString() {
        return "\n " +
                "ID: " + this.id + "\n" +
                "mPackSize: " + this.mPackSize + "\n" +
                "mUnitOfMeasure: " + this.mUnitOfMeasure + "\n" +
                "mFbUsedProductsUserKey: " + this.mFbUsedProductsUserKey + "\n" +
                "mRetailer: " + this.mRetailer + "\n" +
                "mDescription: " + this.mDescription + "\n" +
                "mMadeBy: " + this.mMadeBy + "\n" +
                "mPackPrice: " + this.mPackPrice + "\n" +
                "mPackPriceAverage: " + this.mPackPriceAverage + "\n" +
                "mLocalImageUri: " + this.mLocalImageUri + "\n" +
                "mCreatedBy: " + this.mCreatedBy + "\n" +
                "mFbStorageImageUri: " + this.mFbStorageImageUri + "\n" +
                "mLocationRoom: " + this.mLocationRoom + "\n" +
                "mFbProductReferenceKey: " + this.mFbProductReferenceKey + "\n" +
                "mCategory: " + this.mCategory + "\n" +
                "mShelfLife: " + this.mShelfLife + "\n" +
                "mLocationInRoom: " + this.mLocationInRoom + "\n" +
                "mCommCreateDate: " + this.mCommCreateDate + "\n" +
                "mCommLastUpdate: " + this.mCommLastUpdate + "\n" +
                "mMyCreateDate: " + this.mMyCreateDate + "\n" +
                "mMyLastUpdate: " + this.mMyLastUpdate;
    }

    /* Getters and setters */
    @Exclude
    public int getId() {
        return this.id;
    }

    @Exclude
    public void setId(int id) {
        this.id = id;
    }

    public String getFbProductReferenceKey() {
        return mFbProductReferenceKey;
    }

    public void setFbProductReferenceKey(String fbProductReferenceKey) {
        this.mFbProductReferenceKey = fbProductReferenceKey;
    }

    public String getFbUsedProductsUserKey() {
        return mFbUsedProductsUserKey;
    }

    public void setFbUsedProductsUserKey(String fbUsedProductsUserKey) {
        this.mFbUsedProductsUserKey = fbUsedProductsUserKey;
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
}
