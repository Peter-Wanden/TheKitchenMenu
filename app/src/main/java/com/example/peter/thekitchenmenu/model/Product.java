package com.example.peter.thekitchenmenu.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private long mProductId;
    private String mDescription;
    private String mRetailer;
    private int mUnitOfMeasure;
    private int mPackSize;
    private int mShelfLife;
    private String mLocationRoom;
    private String mLocationInRoom;
    private int mCategory;
    private double mPackPrice;

    public Product(long productId, String description, String retailer, int unitOfMeasure,
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
        mProductId = in.readLong();
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
        parcel.writeLong(mProductId);
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

    public long getProductId() {return mProductId;}
    public void setProductId(long productId) {mProductId = productId;}

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
