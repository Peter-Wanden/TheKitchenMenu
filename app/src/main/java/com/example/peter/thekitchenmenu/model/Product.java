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
    private String mFbProductReferenceId;

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
                   String fbProductReferenceId,
                   String retailer,
                   int unitOfMeasure,
                   int packSize,
                   int shelfLife,
                   String locationRoom,
                   String locationInRoom,
                   int category,
                   double packPrice,
                   Uri localImageUri,
                   Uri fbStorageImageUri,
                   String createdBy) {

        this.mDescription = description;
        this.mMadeBy = madeBy;
        this.mFbProductReferenceId = fbProductReferenceId;
        this.mRetailer = retailer;
        this.mUnitOfMeasure = unitOfMeasure;
        this.mPackSize = packSize;
        this.mShelfLife = shelfLife;
        this.mLocationRoom = locationRoom;
        this.mLocationInRoom = locationInRoom;
        this.mCategory = category;
        this.mPackPrice = packPrice;
        this.mLocalImageUri = localImageUri.toString();
        this.mFbStorageImageUri = fbStorageImageUri.toString();
        this.mCreatedBy = createdBy;
    }

    /* Empty constructor as required by Firebase */
    public Product(){};

    /* Constructor for the local database*/
    public Product(int productId,
                   String fbProductReferenceId,
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
                   Uri localImageUri,
                   Uri fbStorageImageUri,
                   String createdBy) {

        this.mProductId = productId;
        this.mFbProductReferenceId = fbProductReferenceId;
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
        this.mLocalImageUri = localImageUri.toString();
        this.mFbStorageImageUri = fbStorageImageUri.toString();
        this.mCreatedBy = createdBy;
    }


    private Product(Parcel in) {
        mProductId = in.readInt();
        mFbProductReferenceId = in.readString();
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
        parcel.writeString(mFbProductReferenceId);
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
        result.put(Constants.PRODUCT_BASE_PACK_SIZE_KEY, mPackPrice);
        result.put(Constants.PRODUCT_BASE_UNIT_OF_MEASURE_KEY, mUnitOfMeasure);
        result.put(Constants.PRODUCT_BASE_PRICE_AVE_KEY, mPackPrice);
        result.put(Constants.PRODUCT_BASE_CREATED_BY_KEY, mCreatedBy);

        return result;
    }

    /* Getters and setters */
    public int getProductId() {return mProductId;}
    public void setProductId(int productId) {this.mProductId = productId;}

    public String getFbProductReferenceId() {return mFbProductReferenceId;}
    public void setFbProductReferenceId(String fbProductReferenceId) {this.mFbProductReferenceId = fbProductReferenceId;}

    public String getDescription() {return mDescription;}
    public void setDescription(String description) {this.mDescription = description;}

    public String getMadeBy() {return mMadeBy;}
    public void setMadeBy(String madeBy) {this.mMadeBy = madeBy;}

    public String getRetailer() {return mRetailer;}
    public void setRetailer(String retailer) {this.mRetailer = retailer;}

    public int getUnitOfMeasure() {return mUnitOfMeasure;}
    public void setUnitOfMeasure(int unitOfMeasure) {this.mUnitOfMeasure = unitOfMeasure;}

    public int getPackSize() {return mPackSize;}
    public void setPackSize(int packSize) {this.mPackSize = packSize;}

    public int getShelfLife() {return mShelfLife;}
    public void setShelfLife(int shelfLife) {this.mShelfLife = shelfLife;}

    public String getLocationRoom() {return mLocationRoom;}
    public void setLocationRoom(String locationRoom) {this.mLocationRoom = locationRoom;}

    public String getLocationInRoom() {return mLocationInRoom;}
    public void setLocationInRoom(String locationInRoom) {this.mLocationInRoom = locationInRoom;}

    public int getCategory() {return mCategory;}
    public void setCategory(int category) {this.mCategory = category;}

    public double getPackPrice() {return mPackPrice;}
    public void setPackPrice(double packPrice) {this.mPackPrice = packPrice;}

    public String getLocalImageUri() {return mLocalImageUri;}
    public void setLocalImageUri(String localImageUri) {this.mLocalImageUri = localImageUri;}

    public String getFbStorageImageUri() {return mFbStorageImageUri;}
    public void setFbStorageImageUri(String fbStorageImageUri)
    {this.mFbStorageImageUri = fbStorageImageUri;}

    public String getCreatedBy() {return mCreatedBy;}
    public void setCreatedBy(String createdBy) {this.mCreatedBy = createdBy;}
}
