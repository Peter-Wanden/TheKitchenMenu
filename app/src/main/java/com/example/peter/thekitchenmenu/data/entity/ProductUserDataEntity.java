package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.common.internal.Objects;

import java.util.Calendar;
import java.util.UUID;

import static com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity.TABLE_USERS_PRODUCT_DATA;

@Entity(tableName = TABLE_USERS_PRODUCT_DATA)
public final class ProductUserDataEntity {

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
    private final String id;

    @NonNull
    @ColumnInfo(name = PRODUCT_ID)
    private final String productId;

    @ColumnInfo(name = RETAILER)
    private final String retailer;

    @ColumnInfo(name = LOCATION_ROOM)
    private final String locationRoom;

    @ColumnInfo(name = LOCATION_IN_ROOM)
    private final String locationInRoom;

    @ColumnInfo(name = PRICE)
    private final double price;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    @Ignore
    private ProductEntity product;

    // Used by room and for copying
    public ProductUserDataEntity(@NonNull String id,
                                 @NonNull String productId,
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
    public ProductUserDataEntity(@NonNull String productId,
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

    @Override
    public boolean equals(@Nullable Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductUserDataEntity entity = (ProductUserDataEntity) o;

        return Objects.equal(id,                entity.id)              &&
               Objects.equal(productId,         entity.productId)       &&
               Objects.equal(retailer,          entity.retailer)        &&
               Objects.equal(locationRoom,      entity.locationRoom)    &&
               Objects.equal(locationInRoom,    entity.locationInRoom)  &&
               Objects.equal(price,             entity.price)           &&
               Objects.equal(createDate,        entity.createDate)      &&
               Objects.equal(lastUpdate,        entity.lastUpdate);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
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

    @Ignore
    public ProductEntity getProduct() {
        return product;
    }

    @Ignore
    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}
