package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.util.Strings;

import java.util.Calendar;
import java.util.UUID;

import static com.example.peter.thekitchenmenu.data.entity.UsedProductEntity.TABLE_USED_PRODUCTS;

@Entity(tableName = TABLE_USED_PRODUCTS)
public final class UsedProductEntity {

    public static final String TAG = "UsedProductEntity";

    public static final String TABLE_USED_PRODUCTS = "usedProducts";
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
    private final String price;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    // Required by room, do not use
    public UsedProductEntity(@NonNull String id,
                             @NonNull String productId,
                             @Nullable String retailer,
                             @Nullable String locationRoom,
                             @Nullable String locationInRoom,
                             @Nullable String price,
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

    @Ignore
    public static UsedProductEntity updateUsedProduct(@NonNull String usedProductId,
                                                      @NonNull String productId,
                                                      @Nullable String retailer,
                                                      @Nullable String locationRoom,
                                                      @Nullable String locationInRoom,
                                                      @Nullable String price,
                                                      long createDate) {
        return new UsedProductEntity(
                usedProductId,
                productId,
                retailer,
                locationRoom,
                locationInRoom,
                price,
                createDate,
                getDate());
    }

    @Ignore
    public static UsedProductEntity createNewUsedProduct(@NonNull String productId,
                                                         @Nullable String retailer,
                                                         @Nullable String locationRoom,
                                                         @Nullable String locationInRoom,
                                                         @Nullable String price) {
        return new UsedProductEntity(
                UUID.randomUUID().toString(),
                productId,
                retailer,
                locationRoom,
                locationInRoom,
                price,
                getDate(),
                getDate());
    }

    private static long getDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public boolean equals(@Nullable Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsedProductEntity entity = (UsedProductEntity) o;

        return Objects.equal(id,                entity.id)              &&
               Objects.equal(productId,         entity.productId)       &&
               Objects.equal(retailer,          entity.retailer)        &&
               Objects.equal(locationRoom,      entity.locationRoom)    &&
               Objects.equal(locationInRoom,    entity.locationInRoom)  &&
               Objects.equal(price,             entity.price)           &&
               Objects.equal(createDate,        entity.createDate)      &&
               Objects.equal(lastUpdate,        entity.lastUpdate);
    }

    public boolean isEmpty() {
        return Strings.isEmptyOrWhitespace(retailer) &&
                Strings.isEmptyOrWhitespace(locationRoom) &&
                Strings.isEmptyOrWhitespace(locationInRoom) &&
                Strings.isEmptyOrWhitespace(price);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UsedProductEntity{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", retailer='" + retailer + '\'' +
                ", locationRoom='" + locationRoom + '\'' +
                ", locationInRoom='" + locationInRoom + '\'' +
                ", price=" + price +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
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

    public String getPrice() {
        return price;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
