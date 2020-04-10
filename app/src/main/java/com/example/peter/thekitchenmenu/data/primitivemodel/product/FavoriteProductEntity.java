package com.example.peter.thekitchenmenu.data.primitivemodel.product;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.util.Strings;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity.PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.primitivemodel.product.FavoriteProductEntity.TABLE_FAVORITE_PRODUCTS;

@Entity(tableName = TABLE_FAVORITE_PRODUCTS,
        indices = {@Index(value = {PRODUCT_ID}, unique = true)})
public final class FavoriteProductEntity implements PrimitiveModel {

    public static final String TAG = "FavoriteProductEntity";

    public static final String TABLE_FAVORITE_PRODUCTS = "favoriteProducts";
    public static final String DATA_ID = "id";
    public static final String PRODUCT_ID = "productId";
    private static final String RETAILER = "retailer";
    private static final String LOCATION_ROOM = "locationRoom";
    private static final String LOCATION_IN_ROOM = "locationInRoom";
    private static final String PRICE = "price";
    private static final String CREATE_DATE = "usersProductDataCreateDate";
    private static final String LAST_UPDATE = "usersProductDataLastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @Nonnull
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

    public FavoriteProductEntity(@Nonnull String dataId,
                                 @Nonnull String productId,
                                 @Nullable String retailer,
                                 @Nullable String locationRoom,
                                 @Nullable String locationInRoom,
                                 @Nullable String price,
                                 long createDate,
                                 long lastUpdate) {

        this.dataId = dataId;
        this.productId = productId;
        this.retailer = retailer;
        this.locationRoom = locationRoom;
        this.locationInRoom = locationInRoom;
        this.price = price;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Ignore
    public static FavoriteProductEntity updateFavoriteProduct(@Nonnull String favoriteProductId,
                                                              @Nonnull String productId,
                                                              @Nullable String retailer,
                                                              @Nullable String locationRoom,
                                                              @Nullable String locationInRoom,
                                                              @Nullable String price,
                                                              long createDate) {
        return new FavoriteProductEntity(
                favoriteProductId,
                productId,
                retailer,
                locationRoom,
                locationInRoom,
                price,
                createDate,
                getDate());
    }

    @Ignore
    public static FavoriteProductEntity createFavoriteProduct(@Nonnull String productId,
                                                              @Nullable String retailer,
                                                              @Nullable String locationRoom,
                                                              @Nullable String locationInRoom,
                                                              @Nullable String price) {
        return new FavoriteProductEntity(
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
        FavoriteProductEntity entity = (FavoriteProductEntity) o;

        return Objects.equal(dataId, entity.dataId) &&
                Objects.equal(productId, entity.productId) &&
                Objects.equal(retailer, entity.retailer) &&
                Objects.equal(locationRoom, entity.locationRoom) &&
                Objects.equal(locationInRoom, entity.locationInRoom) &&
                Objects.equal(price, entity.price) &&
                Objects.equal(createDate, entity.createDate) &&
                Objects.equal(lastUpdate, entity.lastUpdate);
    }

    public boolean isEmpty() {
        return Strings.isEmptyOrWhitespace(retailer) &&
                Strings.isEmptyOrWhitespace(locationRoom) &&
                Strings.isEmptyOrWhitespace(locationInRoom) &&
                Strings.isEmptyOrWhitespace(price);
    }

    @Override
    public String toString() {
        return "FavoriteProductEntity{" +
                "id='" + dataId + '\'' +
                ", productId='" + productId + '\'' +
                ", retailer='" + retailer + '\'' +
                ", locationRoom='" + locationRoom + '\'' +
                ", locationInRoom='" + locationInRoom + '\'' +
                ", price=" + price +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Nonnull
    @Override
    public String getDataId() {
        return dataId;
    }

    @Nonnull
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
