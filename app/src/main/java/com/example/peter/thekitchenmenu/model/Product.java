package com.example.peter.thekitchenmenu.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "Products")
public class Product implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mProductId;

    @ColumnInfo(name = "Description")
    private String mDescription;

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

    @Ignore
    /* Constructor */
    public Product(String description, String retailer, int unitOfMeasure,
                   int packSize, int shelfLife, String locationRoom, String locationInRoom,
                   int category, double packPrice) {

        this.mDescription = description;
        this.mRetailer = retailer;
        this.mUnitOfMeasure = unitOfMeasure;
        this.mPackSize = packSize;
        this.mShelfLife = shelfLife;
        this.mLocationRoom = locationRoom;
        this.mLocationInRoom = locationInRoom;
        this.mCategory = category;
        this.mPackPrice = packPrice;

    }

    /* Constructor for the database*/
    public Product(int productId, String description, String retailer, int unitOfMeasure,
                   int packSize, int shelfLife, String locationRoom, String locationInRoom,
                   int category, double packPrice) {

        this.mProductId = productId;
        this.mDescription = description;
        this.mRetailer = retailer;
        this.mUnitOfMeasure = unitOfMeasure;
        this.mPackSize = packSize;
        this.mShelfLife = shelfLife;
        this.mLocationRoom = locationRoom;
        this.mLocationInRoom = locationInRoom;
        this.mCategory = category;
        this.mPackPrice = packPrice;
    }


    private Product(Parcel in) {
        mProductId = in.readInt();
        mDescription = in.readString();
        mRetailer = in.readString();
        mUnitOfMeasure = in.readInt();
        mPackSize = in.readInt();
        mShelfLife = in.readInt();
        mLocationRoom = in.readString();
        mLocationInRoom = in.readString();
        mCategory = in.readInt();
        mPackPrice = in.readDouble();
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
        parcel.writeString(mDescription);
        parcel.writeString(mRetailer);
        parcel.writeInt(mUnitOfMeasure);
        parcel.writeInt(mPackSize);
        parcel.writeInt(mShelfLife);
        parcel.writeString(mLocationRoom);
        parcel.writeString(mLocationInRoom);
        parcel.writeInt(mCategory);
        parcel.writeDouble(mPackPrice);
    }

    public int getProductId() {return mProductId;}
    public void setProductId(int productId) {mProductId = productId;}

    public String getDescription() {return mDescription;}
    public void setDescription(String description) {mDescription = description;}

    public String getRetailer() {return mRetailer;}
    public void setRetailer(String retailer) {mRetailer = retailer;}

    public int getUnitOfMeasure() {return mUnitOfMeasure;}
    public void setUnitOfMeasure(int unitOfMeasure) {mUnitOfMeasure = unitOfMeasure;}

    public int getPackSize() {return mPackSize;}
    public void setPackSize(int packSize) {mPackSize = packSize;}

    public int getShelfLife() {return mShelfLife;}
    public void setShelfLife(int shelfLife) {mShelfLife = shelfLife;}

    public String getLocationRoom() {return mLocationRoom;}
    public void setLocationRoom(String locationRoom) {mLocationRoom = locationRoom;}

    public String getLocationInRoom() {return mLocationInRoom;}
    public void setLocationInRoom(String locationInRoom) {mLocationInRoom = locationInRoom;}

    public int getCategory() {return mCategory;}
    public void setCategory(int category) {mCategory = category;}

    public double getPackPrice() {return mPackPrice;}
    public void setPackPrice(double packPrice) {mPackPrice = packPrice;}
}
