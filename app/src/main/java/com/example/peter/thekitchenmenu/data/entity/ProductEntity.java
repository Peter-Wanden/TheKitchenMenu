package com.example.peter.thekitchenmenu.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.google.android.gms.common.internal.Objects;

import java.util.Calendar;
import java.util.UUID;

@Entity(tableName = ProductEntity.TABLE_PRODUCT)
public final class ProductEntity implements Parcelable, TkmEntity {

    public static final String TAG = "tkm-ProductEntity";

    public static final String TABLE_PRODUCT = "product";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String SHOPPING_LIST_ITEM_NAME = "shoppingListItemName";
    private static final String CATEGORY = "category";
    private static final String NUMBER_OF_PRODUCTS = "number_of_products";
    private static final String SHELF_LIFE = "shelfLife";
    private static final String BASE_UNITS = "baseUnits";
    private static final String UNIT_OF_MEASURE_SUB_TYPE = "unitOfMeasureSubtype";
    private static final String CREATED_BY = "createdBy";
    private static final String WEB_IMAGE_URL = "webImageUrl";
    private static final String REMOTE_SMALL_IMAGE_URI = "remoteSmallImageUri";
    private static final String REMOTE_MEDIUM_IMAGE_URI = "remoteMediumImageUri";
    private static final String REMOTE_LARGE_IMAGE_URI = "remoteLargeImageUri";
    private static final String CREATE_DATE = "productCreateDate";
    private static final String LAST_UPDATE = "productLastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @NonNull
    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @NonNull
    @ColumnInfo(name = SHOPPING_LIST_ITEM_NAME)
    private final String shoppingListItemName;

    @ColumnInfo(name = CATEGORY)
    private final int category;

    @ColumnInfo(name = SHELF_LIFE)
    private final int shelfLife;

    @ColumnInfo(name = NUMBER_OF_PRODUCTS)
    private final int numberOfProducts;

    @ColumnInfo(name = BASE_UNITS)
    private final double baseUnits;

    @ColumnInfo(name = UNIT_OF_MEASURE_SUB_TYPE)
    private final int unitOfMeasureSubtype;

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

    public ProductEntity(@NonNull String id,
                         @NonNull String description,
                         @NonNull String shoppingListItemName,
                         int category,
                         int shelfLife,
                         int numberOfProducts,
                         double baseUnits,
                         int unitOfMeasureSubtype,
                         @NonNull String createdBy,
                         @Nullable String webImageUrl,
                         @Nullable String remoteSmallImageUri,
                         @Nullable String remoteMediumImageUri,
                         @Nullable String remoteLargeImageUri,
                         long createDate,
                         long lastUpdate) {

        this.id = id;
        this.description = description;
        this.shoppingListItemName = shoppingListItemName;
        this.category = category;
        this.numberOfProducts = numberOfProducts;
        this.shelfLife = shelfLife;
        this.baseUnits = baseUnits;
        this.unitOfMeasureSubtype = unitOfMeasureSubtype;
        this.createdBy = createdBy;
        this.webImageUrl = webImageUrl;
        this.remoteSmallImageUri = remoteSmallImageUri;
        this.remoteMediumImageUri = remoteMediumImageUri;
        this.remoteLargeImageUri = remoteLargeImageUri;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Ignore
    public static ProductEntity shallowCopy(ProductEntity productEntity) {
        return new ProductEntity(
                productEntity.getId(),
                productEntity.getDescription(),
                productEntity.getShoppingListItemName(),
                productEntity.getCategory(),
                productEntity.getShelfLife(),
                productEntity.getNumberOfProducts(),
                productEntity.getBaseUnits(),
                productEntity.getUnitOfMeasureSubtype(),
                productEntity.getCreatedBy(),
                productEntity.getWebImageUrl(),
                productEntity.getRemoteSmallImageUri(),
                productEntity.getRemoteMediumImageUri(),
                productEntity.getRemoteLargeImageUri(),
                productEntity.getCreateDate(),
                productEntity.getLastUpdate());
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
    public static ProductEntity createProduct(@NonNull String description,
                                              @NonNull String shoppingListItemName,
                                              int category,
                                              int shelfLife,
                                              int numberOfProducts,
                                              double baseUnits,
                                              int unitOfMeasureSubtype,
                                              @NonNull String createdBy, // Change to app.Constants.getUserId()
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
    public static ProductEntity updateProduct(@NonNull String id,
                                              @NonNull String description,
                                              @NonNull String shoppingListItemName,
                                              int category,
                                              int shelfLife,
                                              int numberOfProducts,
                                              double baseUnits,
                                              int unitOfMeasureSubtype,
                                              @NonNull String createdBy,
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

        return Objects.equal(id, entity.id) &&
                Objects.equal(description, entity.description) &&
                Objects.equal(shoppingListItemName, entity.shoppingListItemName) &&
                Objects.equal(category, entity.category) &&
                Objects.equal(shelfLife, entity.shelfLife) &&
                Objects.equal(numberOfProducts, entity.numberOfProducts) &&
                Objects.equal(baseUnits, entity.baseUnits) &&
                Objects.equal(unitOfMeasureSubtype, entity.unitOfMeasureSubtype) &&
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
                "\nid=" + id +
                "\ndescription='" + description + '\'' +
                "\nshoppingListItemName='" + shoppingListItemName + '\'' +
                "\ncategory=" + category +
                "\nshelfLife=" + shelfLife +
                "\nnumberOfProducts=" + numberOfProducts +
                "\nbaseUnits=" + baseUnits +
                "\nunitOfMeasureSubtype=" + unitOfMeasureSubtype +
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
        id = java.util.Objects.requireNonNull(in.readString(),
                "id cannot be null");
        description = java.util.Objects.requireNonNull(in.readString(),
                "description cannot be null");
        shoppingListItemName = java.util.Objects.requireNonNull(in.readString(),
                "shoppingListItemName cannot be null");
        category = in.readInt();
        shelfLife = in.readInt();
        numberOfProducts = in.readInt();
        baseUnits = in.readDouble();
        unitOfMeasureSubtype = in.readInt();
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
        parcel.writeString(id);
        parcel.writeString(description);
        parcel.writeString(shoppingListItemName);
        parcel.writeInt(category);
        parcel.writeInt(shelfLife);
        parcel.writeInt(numberOfProducts);
        parcel.writeDouble(baseUnits);
        parcel.writeInt(unitOfMeasureSubtype);
        parcel.writeString(createdBy);
        parcel.writeString(webImageUrl);
        parcel.writeString(remoteSmallImageUri);
        parcel.writeString(remoteMediumImageUri);
        parcel.writeString(remoteLargeImageUri);
        parcel.writeLong(createDate);
        parcel.writeLong(lastUpdate);
    }

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getShoppingListItemName() {
        return shoppingListItemName;
    }

    public int getCategory() {
        return category;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public double getBaseUnits() {
        return baseUnits;
    }

    public int getUnitOfMeasureSubtype() {
        return unitOfMeasureSubtype;
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
