package com.example.peter.thekitchenmenu.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_CATEGORY;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_CREATED_BY;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_CREATE_DATE;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_DESC;
import static com.example.peter.thekitchenmenu.data.entity.DmProdComm.PROD_COMM_ID;
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

/**
 * This is the Product model. A POJO made flexible with Firebase and Room annotations along with
 * a fully parcelable implementation.
 */
public class Product implements Parcelable {

    @Exclude // Excludes field from Firebase, as is only required for Room.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PROD_COMM_ID)
    private int id;

    // Community product fields.
    @ColumnInfo(name = PROD_COMM_DESC)
    private String mDescription;

    @ColumnInfo(name = PROD_COMM_MADE_BY)
    private String mMadeBy;

    @ColumnInfo(name = PROD_COMM_CATEGORY)
    private int mCategory;

    @ColumnInfo(name = PROD_COMM_SHELF_LIFE)
    private int mShelfLife;

    @ColumnInfo(name = PROD_COMM_PACK_SIZE)
    private int mPackSize;

    @ColumnInfo(name = PROD_COMM_UNIT_OF_MEASURE)
    private int mUnitOfMeasure;

    @ColumnInfo(name = PROD_COMM_PRICE_AVE)
    private double mPackAvePrice;

    @ColumnInfo(name = PROD_COMM_CREATED_BY)
    private String mCreatedBy;

    @ColumnInfo(name = PROD_COMM_CREATE_DATE)
    private long mCommCreateDate;

    @ColumnInfo(name = PROD_COMM_LAST_UPDATE)
    private long mCommLastUpdate;

    @ColumnInfo(name = PROD_COMM_REMOTE_IMAGE_URI)
    @NonNull
    private String mFbStorageImageUri = "";

    @ColumnInfo(name = PROD_MY_RETAILER)
    private String mRetailer;

    @ColumnInfo(name = PROD_COMM_REMOTE_REF_ID)
    private String mFbProductReferenceKey;

    @ColumnInfo(name = PROD_MY_USED_REMOTE_ID)
    private String mFbUsedProductsUserKey;

    @ColumnInfo(name = PROD_MY_LOC_ROOM)
    private String mLocationRoom;

    @ColumnInfo(name = PROD_MY_LOC_IN_ROOM)
    private String mLocationInRoom;

    @ColumnInfo(name = PROD_MY_PACK_PRICE)
    private double mPackPrice;

    // 'My' product information.
    @ColumnInfo(name = PROD_MY_LOCAL_IMAGE_URI)
    private String mLocalImageUri = "";

    @ColumnInfo(name = PROD_MY_CREATE_DATE)
    private long mMyCreateDate;

    @ColumnInfo(name = PROD_MY_LAST_UPDATE)
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
                   double mPackAvePrice,
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
        this.mPackAvePrice = mPackAvePrice;
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
                   double mPackAvePrice,
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
        this.mPackAvePrice = mPackAvePrice;
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
        mPackAvePrice = in.readDouble();
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
        parcel.writeDouble(mPackAvePrice);
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

    @Exclude // HashMap for FireBase user products information Map
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
        result.put(PROD_COMM_REMOTE_REF_ID, mFbProductReferenceKey);
        result.put(PROD_MY_USED_REMOTE_ID, mFbUsedProductsUserKey);
        result.put(PROD_MY_RETAILER, mRetailer);
        result.put(PROD_MY_LOC_ROOM, mLocationRoom);
        result.put(PROD_MY_LOC_IN_ROOM, mLocationInRoom);
        result.put(PROD_MY_PACK_PRICE, mPackPrice);
        result.put(PROD_MY_LOCAL_IMAGE_URI, mLocalImageUri);
        result.put(PROD_MY_CREATE_DATE, mMyCreateDate);
        result.put(PROD_MY_LAST_UPDATE, mMyLastUpdate);

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
                "mPackAvePrice: " + this.mPackAvePrice + "\n" +
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

    public double getPackAvePrice() {
        return mPackAvePrice;
    }

    public void setPackAvePrice(double packAvePrice) {
        this.mPackAvePrice = packAvePrice;
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
