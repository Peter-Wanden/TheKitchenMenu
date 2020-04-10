package com.example.peter.thekitchenmenu.data.primitivemodel.product;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.google.android.gms.common.internal.Objects;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nonnull;

@Entity(tableName = ProductEntity.TABLE_PRODUCT)
public final class ProductEntity implements Parcelable, PrimitiveModel {

    public static final String TABLE_PRODUCT = "product";
    public static final String DATA_ID = "dataId";
    public static final String DESCRIPTION = "description";
    public static final String SHOPPING_LIST_ITEM_NAME = "shoppingListItemName";
    private static final String CATEGORY = "category";
    private static final String NUMBER_OF_ITEMS = "numberOfItems";
    private static final String SHELF_LIFE = "shelfLife";
    private static final String BASE_UNITS = "baseUnits";
    private static final String SUBTYPE = "subtype";
    private static final String CREATED_BY = "createdBy";
    private static final String WEB_IMAGE_URL = "webImageUrl";
    private static final String REMOTE_SMALL_IMAGE_URI = "remoteSmallImageUri";
    private static final String REMOTE_MEDIUM_IMAGE_URI = "remoteMediumImageUri";
    private static final String REMOTE_LARGE_IMAGE_URI = "remoteLargeImageUri";
    private static final String CREATE_DATE = "productCreateDate";
    private static final String LAST_UPDATE = "productLastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @Nonnull
    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @Nonnull
    @ColumnInfo(name = SHOPPING_LIST_ITEM_NAME)
    private final String shoppingListItemName;

    @ColumnInfo(name = CATEGORY)
    private final int category;

    @ColumnInfo(name = SHELF_LIFE)
    private final int shelfLife;

    @ColumnInfo(name = NUMBER_OF_ITEMS)
    private final int numberOfItems;

    @ColumnInfo(name = BASE_UNITS)
    private final double baseUnits;

    @ColumnInfo(name = SUBTYPE)
    private final int subtype;

    @ColumnInfo(name = CREATED_BY)
    private final String createdBy;

    @Nullable
    @ColumnInfo(name = WEB_IMAGE_URL)
    private final String webImageUrl;

    @Nullable
    @ColumnInfo(name = REMOTE_SMALL_IMAGE_URI)
    private final String remoteSmallImageUri;

    @Nullable
    @ColumnInfo(name = REMOTE_MEDIUM_IMAGE_URI)
    private final String remoteMediumImageUri;

    @Nullable
    @ColumnInfo(name = REMOTE_LARGE_IMAGE_URI)
    private final String remoteLargeImageUri;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public ProductEntity(@Nonnull String dataId,
                         @Nonnull String description,
                         @Nonnull String shoppingListItemName,
                         int category,
                         int shelfLife,
                         int numberOfItems,
                         double baseUnits,
                         int subtype,
                         @Nonnull String createdBy,
                         @Nullable String webImageUrl,
                         @Nullable String remoteSmallImageUri,
                         @Nullable String remoteMediumImageUri,
                         @Nullable String remoteLargeImageUri,
                         long createDate,
                         long lastUpdate) {

        this.dataId = dataId;
        this.description = description;
        this.shoppingListItemName = shoppingListItemName;
        this.category = category;
        this.numberOfItems = numberOfItems;
        this.shelfLife = shelfLife;
        this.baseUnits = baseUnits;
        this.subtype = subtype;
        this.createdBy = createdBy;
        this.webImageUrl = webImageUrl;
        this.remoteSmallImageUri = remoteSmallImageUri;
        this.remoteMediumImageUri = remoteMediumImageUri;
        this.remoteLargeImageUri = remoteLargeImageUri;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    /**
     * Use this constructor when creating a new product entity
     *
     * @param description          manufacturers or retailers exact description of the product
     * @param shoppingListItemName name used for this product on a shopping list
     * @param category             an integer that relates back to a category
     * @param shelfLife            an integer that relates back to a period of time
     * @param numberOfProducts     defines the number of products when sold as a multi-pack
     * @param baseUnits            the products size in base units
     * @param unitOfMeasureSubtype an integer defining the products {@link MeasurementSubtype}
     * @param createdBy            the users UID
     * @param webImageUrl          a URL to an image that is not stored by TKM
     * @param remoteSmallImageUri  URL to a small version of a product image
     * @param remoteMediumImageUri URL to a medium sized version of a product image
     * @param remoteLargeImageUri  URL to a large version of a product image
     */
    @Ignore
    public static ProductEntity createProduct(@Nonnull String description,
                                              @Nonnull String shoppingListItemName,
                                              int category,
                                              int shelfLife,
                                              int numberOfProducts,
                                              double baseUnits,
                                              int unitOfMeasureSubtype,
                                              @Nonnull String createdBy, // Change to app.Constants.getUserId()
                                              @Nullable String webImageUrl,
                                              @Nullable String remoteSmallImageUri,
                                              @Nullable String remoteMediumImageUri,
                                              @Nullable String remoteLargeImageUri) {
        return new ProductEntity(
                UUID.randomUUID().toString(),
                description,
                shoppingListItemName,
                category,
                shelfLife,
                numberOfProducts,
                baseUnits,
                unitOfMeasureSubtype,
                createdBy,
                webImageUrl,
                remoteSmallImageUri,
                remoteMediumImageUri,
                remoteLargeImageUri,
                getDate(),
                getDate());
    }

    /**
     * Use this constructor when updating (copying) a product
     *
     * @param id                   the id of the product being copied
     * @param description          manufacturers or retailers exact description of the product
     * @param shoppingListItemName name used for this product on a shopping list
     * @param category             an integer that relates back to a category
     * @param shelfLife            an integer that relates back to a period of time
     * @param numberOfProducts     defines the number of products when sold as a multi-pack
     * @param baseUnits            the products size in base units
     * @param unitOfMeasureSubtype an integer defining the products {@link MeasurementSubtype}
     * @param createdBy            the Google Firebase provided UID
     * @param webImageUrl          URL to an image that is not stored by TKM
     * @param remoteSmallImageUri  URL to a small version of a product image
     * @param remoteMediumImageUri URL to a medium sized version of a product image
     * @param remoteLargeImageUri  URL to a large version of a product image
     */
    @Ignore
    public static ProductEntity updateProduct(@Nonnull String id,
                                              @Nonnull String description,
                                              @Nonnull String shoppingListItemName,
                                              int category,
                                              int shelfLife,
                                              int numberOfProducts,
                                              double baseUnits,
                                              int unitOfMeasureSubtype,
                                              @Nonnull String createdBy,
                                              @Nullable String webImageUrl,
                                              @Nullable String remoteSmallImageUri,
                                              @Nullable String remoteMediumImageUri,
                                              @Nullable String remoteLargeImageUri,
                                              long createDate) {
        return new ProductEntity(
                id,
                description,
                shoppingListItemName,
                category,
                numberOfProducts,
                shelfLife,
                baseUnits,
                unitOfMeasureSubtype,
                createdBy,
                webImageUrl,
                remoteSmallImageUri,
                remoteMediumImageUri,
                remoteLargeImageUri,
                createDate,
                getDate());
    }

    private static long getDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public boolean equals(@Nullable Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity entity = (ProductEntity) o;

        return Objects.equal(dataId, entity.dataId) &&
                Objects.equal(description, entity.description) &&
                Objects.equal(shoppingListItemName, entity.shoppingListItemName) &&
                Objects.equal(category, entity.category) &&
                Objects.equal(shelfLife, entity.shelfLife) &&
                Objects.equal(numberOfItems, entity.numberOfItems) &&
                Objects.equal(baseUnits, entity.baseUnits) &&
                Objects.equal(subtype, entity.subtype) &&
                Objects.equal(createdBy, entity.createdBy) &&
                Objects.equal(webImageUrl, entity.webImageUrl) &&
                Objects.equal(remoteSmallImageUri, entity.remoteSmallImageUri) &&
                Objects.equal(remoteMediumImageUri, entity.remoteMediumImageUri) &&
                Objects.equal(remoteLargeImageUri, entity.remoteLargeImageUri) &&
                Objects.equal(createDate, entity.createDate) &&
                Objects.equal(lastUpdate, entity.lastUpdate);
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "\nid=" + dataId +
                "\ndescription='" + description + '\'' +
                "\nshoppingListItemName='" + shoppingListItemName + '\'' +
                "\ncategory=" + category +
                "\nshelfLife=" + shelfLife +
                "\nnumberOfItems=" + numberOfItems +
                "\nbaseUnits=" + baseUnits +
                "\nsubtype=" + subtype +
                "\ncreatedBy='" + createdBy + '\'' +
                "\nwebImageUrl='" + webImageUrl + '\'' +
                "\nremoteSmallImageUri='" + remoteSmallImageUri + '\'' +
                "\nremoteMediumImageUri='" + remoteMediumImageUri + '\'' +
                "\nremoteLargeImageUri='" + remoteLargeImageUri + '\'' +
                "\ncreateDate=" + createDate +
                "\nlastUpdate=" + lastUpdate + '\'' +
                '}';
    }

    protected ProductEntity(Parcel in) {
        dataId = java.util.Objects.requireNonNull(in.readString(),
                "id cannot be null");
        description = java.util.Objects.requireNonNull(in.readString(),
                "description cannot be null");
        shoppingListItemName = java.util.Objects.requireNonNull(in.readString(),
                "shoppingListItemName cannot be null");
        category = in.readInt();
        shelfLife = in.readInt();
        numberOfItems = in.readInt();
        baseUnits = in.readDouble();
        subtype = in.readInt();
        createdBy = in.readString();
        webImageUrl = in.readString();
        remoteSmallImageUri = in.readString();
        remoteMediumImageUri = in.readString();
        remoteLargeImageUri = in.readString();
        createDate = in.readLong();
        lastUpdate = in.readLong();
    }

    public static final Creator<ProductEntity> CREATOR = new Creator<ProductEntity>() {
        @Override
        public ProductEntity createFromParcel(Parcel in) {
            return new ProductEntity(in);
        }

        @Override
        public ProductEntity[] newArray(int size) {
            return new ProductEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dataId);
        parcel.writeString(description);
        parcel.writeString(shoppingListItemName);
        parcel.writeInt(category);
        parcel.writeInt(shelfLife);
        parcel.writeInt(numberOfItems);
        parcel.writeDouble(baseUnits);
        parcel.writeInt(subtype);
        parcel.writeString(createdBy);
        parcel.writeString(webImageUrl);
        parcel.writeString(remoteSmallImageUri);
        parcel.writeString(remoteMediumImageUri);
        parcel.writeString(remoteLargeImageUri);
        parcel.writeLong(createDate);
        parcel.writeLong(lastUpdate);
    }

    @Nonnull
    @Override
    public String getDataId() {
        return dataId;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
    public String getShoppingListItemName() {
        return shoppingListItemName;
    }

    public int getCategory() {
        return category;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public double getBaseUnits() {
        return baseUnits;
    }

    public int getSubtype() {
        return subtype;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    @Nullable
    public String getWebImageUrl() {
        return webImageUrl;
    }

    @Nullable
    public String getRemoteSmallImageUri() {
        return remoteSmallImageUri;
    }

    @Nullable
    public String getRemoteMediumImageUri() {
        return remoteMediumImageUri;
    }

    @Nullable
    public String getRemoteLargeImageUri() {
        return remoteLargeImageUri;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
