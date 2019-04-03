package com.example.peter.thekitchenmenu.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.example.peter.thekitchenmenu.data.entity.ProductEntity.REMOTE_PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity.TABLE_USERS_PRODUCT_DATA;

@Entity(tableName = TABLE_USERS_PRODUCT_DATA)
public class ProductUserDataEntity implements Parcelable {

    public static final String TAG = "ProductUserDataEntity";

    public static final String TABLE_USERS_PRODUCT_DATA = "usersProductData";
    public static final String ID = "id";
    public static final String PRODUCT_ID = "productId";
    public static final String REMOTE_USED_PRODUCT_ID = "remoteIdUsedProduct";
    public static final String RETAILER = "retailer";
    public static final String LOCATION_ROOM = "locationRoom";
    public static final String LOCATION_IN_ROOM = "locationInRoom";
    public static final String PRICE = "price";
    public static final String LOCAL_IMAGE_URI = "localImageUri";
    public static final String CREATE_DATE = "usersProductDataCreateDate";
    public static final String LAST_UPDATE = "usersProductDataLastUpdate";

    @Exclude // Exclude field from Firebase, as is only required for Room.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private int id;

    // Relates to the community_product table, field _id.
    @Exclude // Exclude field from Firebase, as is only required for Room.
    @ColumnInfo(name = PRODUCT_ID)
    private int productId;

    @ColumnInfo(name = REMOTE_USED_PRODUCT_ID)
    private String remoteIdUsedProduct;

    @ColumnInfo(name = REMOTE_PRODUCT_ID)
    private String remoteProductId;

    @ColumnInfo(name = RETAILER)
    private String retailer;

    @ColumnInfo(name = LOCATION_ROOM)
    private String locationRoom;

    @ColumnInfo(name = LOCATION_IN_ROOM)
    private String locationInRoom;

    @ColumnInfo(name = PRICE)
    private double price;

    @ColumnInfo(name = LOCAL_IMAGE_URI)
    @NonNull
    private String localImageUri = "";

    @ColumnInfo(name = CREATE_DATE)
    private long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private long lastUpdate;

    /* Empty constructor as required by Firebase. */
    @Ignore
    public ProductUserDataEntity(){}

    public ProductUserDataEntity(int id,
                                 int productId,
                                 String remoteIdUsedProduct,
                                 String remoteProductId,
                                 String retailer,
                                 String locationRoom,
                                 String locationInRoom,
                                 double price,
                                 String localImageUri,
                                 long createDate,
                                 long lastUpdate) {

        this.id = id;
        this.productId = productId;
        this.remoteIdUsedProduct = remoteIdUsedProduct;
        this.remoteProductId = remoteProductId;
        this.retailer = retailer;
        this.locationRoom = locationRoom;
        this.locationInRoom = locationInRoom;
        this.price = price;
        this.localImageUri = localImageUri;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Ignore
    public ProductUserDataEntity(Parcel in) {
        id = in.readInt();
        productId = in.readInt();
        remoteIdUsedProduct = in.readString();
        remoteProductId = in.readString();
        retailer = in.readString();
        locationRoom = in.readString();
        locationInRoom = in.readString();
        price = in.readDouble();
        localImageUri = in.readString();
        createDate = in.readLong();
        lastUpdate = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(productId);
        parcel.writeString(remoteIdUsedProduct);
        parcel.writeString(remoteProductId);
        parcel.writeString(retailer);
        parcel.writeString(locationRoom);
        parcel.writeString(locationInRoom);
        parcel.writeDouble(price);
        parcel.writeString(localImageUri);
        parcel.writeLong(createDate);
        parcel.writeLong(lastUpdate);
    }

    @Exclude
    public Map<String, Object> productMyToMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put(REMOTE_USED_PRODUCT_ID, remoteIdUsedProduct);
        result.put(RETAILER, retailer);
        result.put(LOCATION_ROOM, locationRoom);
        result.put(LOCATION_IN_ROOM, locationInRoom);
        result.put(PRICE, price);
        result.put(LOCAL_IMAGE_URI, localImageUri);
        result.put(CREATE_DATE, createDate);
        result.put(LAST_UPDATE, lastUpdate);

        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductUserDataEntity> CREATOR = new Creator<ProductUserDataEntity>() {
        @Override
        public ProductUserDataEntity createFromParcel(Parcel in) {
            return new ProductUserDataEntity(in);
        }

        @Override
        public ProductUserDataEntity[] newArray(int size) {
            return new ProductUserDataEntity[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getRemoteIdUsedProduct() {
        return remoteIdUsedProduct;
    }

    public void setRemoteIdUsedProduct(String remoteIdUsedProduct) {
        this.remoteIdUsedProduct = remoteIdUsedProduct;
    }

    public String getRemoteProductId() {
        return remoteProductId;
    }

    public void setRemoteProductId(String remoteProductId) {
        this.remoteProductId = remoteProductId;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NonNull
    public String getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(@NonNull String localImageUri) {
        this.localImageUri = localImageUri;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
