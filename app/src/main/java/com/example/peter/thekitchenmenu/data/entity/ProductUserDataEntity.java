package com.example.peter.thekitchenmenu.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.UUID;

import static com.example.peter.thekitchenmenu.data.entity.ProductEntity.REMOTE_PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity.TABLE_USERS_PRODUCT_DATA;

@Entity(tableName = TABLE_USERS_PRODUCT_DATA)
public class ProductUserDataEntity {

    public static final String TAG = "ProductUserDataEntity";

    public static final String TABLE_USERS_PRODUCT_DATA = "usersProductData";
    public static final String ID = "id";
    public static final String PRODUCT_ID = "productId";
    public static final String RETAILER = "retailer";
    public static final String LOCATION_ROOM = "locationRoom";
    public static final String LOCATION_IN_ROOM = "locationInRoom";
    public static final String PRICE = "price";
    public static final String CREATE_DATE = "usersProductDataCreateDate";
    public static final String LAST_UPDATE = "usersProductDataLastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private String id;

    @ColumnInfo(name = PRODUCT_ID)
    private String productId;

    @ColumnInfo(name = RETAILER)
    private String retailer;

    @ColumnInfo(name = LOCATION_ROOM)
    private String locationRoom;

    @ColumnInfo(name = LOCATION_IN_ROOM)
    private String locationInRoom;

    @ColumnInfo(name = PRICE)
    private double price;

    @ColumnInfo(name = CREATE_DATE)
    private long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private long lastUpdate;

    // Used by room and for copying
    public ProductUserDataEntity(String id,
                                 String productId,
                                 String retailer,
                                 String locationRoom,
                                 String locationInRoom,
                                 double price,
                                 long createDate,
                                 long lastUpdate) {

        this.id = id;
        this.productId = productId;
        this.retailer = retailer;
        this.locationRoom = locationRoom;
        this.locationInRoom = locationInRoom;
        this.price = price;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    // Use this constructor to create a new ProductUserDataEntity
    @Ignore
    public ProductUserDataEntity(String productId,
                                 String retailer,
                                 String locationRoom,
                                 String locationInRoom,
                                 double price) {

        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.retailer = retailer;
        this.locationRoom = locationRoom;
        this.locationInRoom = locationInRoom;
        this.price = price;
        this.createDate = getDate();
        this.lastUpdate = getDate();
    }

    private long getDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getRetailer() {
        return retailer;
    }

    public String getLocationRoom() {
        return locationRoom;
    }

    public String getLocationInRoom() {
        return locationInRoom;
    }

    public double getPrice() {
        return price;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
