package com.example.peter.thekitchenmenu.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.example.peter.thekitchenmenu.data.entity.ProductEntity.TABLE_PRODUCT;

@Entity(tableName = TABLE_PRODUCT)
public class ProductEntity implements Parcelable {

    public static final String TAG = "ProductEntity";

    public static final String TABLE_PRODUCT = "product_uneditable";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String MADE_BY = "madeBy";
    public static final String CATEGORY = "category";
    public static final String NUMBER_OF_ITEMS = "number_of_items";
    public static final String SHELF_LIFE = "shelfLife";
    public static final String BASE_SI_UNITS = "baseSiUnits";
    public static final String UNIT_OF_MEASURE_SUB_TYPE = "unitOfMeasureSubType";
    public static final String PROD_COMM_PRICE_AVE = "packAvePrice";
    public static final String CREATED_BY = "createdBy";
    public static final String WEB_IMAGE_URL = "webImageUrl";
    public static final String REMOTE_SMALL_IMAGE_URI = "remoteSmallImageUri";
    public static final String REMOTE_MEDIUM_IMAGE_URI = "remoteMediumImageUri";
    public static final String REMOTE_LARGE_IMAGE_URI = "remoteLargeImageUri";
    public static final String CREATE_DATE = "productCreateDate";
    public static final String LAST_UPDATE = "productLastUpdate";
    public static final String REMOTE_PRODUCT_ID = "remoteProductId";

    @Exclude // Excludes field from Firebase, as is only required for Room.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private int id;

    @ColumnInfo(name = DESCRIPTION)
    private String description;

    @ColumnInfo(name = MADE_BY)
    private String madeBy;

    @ColumnInfo(name = CATEGORY)
    private int category;

    @ColumnInfo(name = SHELF_LIFE)
    private int shelfLife;

    @ColumnInfo(name = NUMBER_OF_ITEMS)
    private int numberOfItems;

    @ColumnInfo(name = BASE_SI_UNITS)
    private double baseSiUnits;

    @ColumnInfo(name = UNIT_OF_MEASURE_SUB_TYPE)
    private int unitOfMeasureSubType;

    @ColumnInfo(name = PROD_COMM_PRICE_AVE)
    private double packAvePrice;

    @ColumnInfo(name = CREATED_BY)
    private String createdBy;

    @ColumnInfo(name = WEB_IMAGE_URL)
    @NotNull
    private String webImageUrl = "";

    @ColumnInfo(name = REMOTE_SMALL_IMAGE_URI)
    private String remoteSmallImageUri = "";

    @ColumnInfo(name = REMOTE_MEDIUM_IMAGE_URI)
    private String remoteMediumImageUri = "";

    @ColumnInfo(name = REMOTE_LARGE_IMAGE_URI)
    private String remoteLargeImageUri = "";

    @ColumnInfo(name = CREATE_DATE)
    private long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private long lastUpdate;

    @ColumnInfo(name = REMOTE_PRODUCT_ID)
    private String remoteProductId;

    @Ignore
    public ProductEntity(){} /* Required by Firebase. */

    public ProductEntity(int id,
                         String description,
                         String madeBy,
                         int category,
                         int shelfLife,
                         int numberOfItems,
                         double baseSiUnits,
                         int unitOfMeasureSubType,
                         double packAvePrice,
                         String createdBy,
                         String webImageUrl,
                         String remoteSmallImageUri,
                         String remoteMediumImageUri,
                         String remoteLargeImageUri,
                         long createDate,
                         long lastUpdate,
                         String remoteProductId) {

        this.id = id;
        this.description = description;
        this.madeBy = madeBy;
        this.category = category;
        this.numberOfItems = numberOfItems;
        this.shelfLife = shelfLife;
        this.baseSiUnits = baseSiUnits;
        this.unitOfMeasureSubType = unitOfMeasureSubType;
        this.packAvePrice = packAvePrice;
        this.createdBy = createdBy;
        this.webImageUrl = webImageUrl;
        this.remoteSmallImageUri = remoteSmallImageUri;
        this.remoteMediumImageUri = remoteMediumImageUri;
        this.remoteLargeImageUri = remoteLargeImageUri;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.remoteProductId = remoteProductId;
    }

    @Ignore
    public ProductEntity(Parcel in) {
        id = in.readInt();
        description = in.readString();
        madeBy = in.readString();
        category = in.readInt();
        numberOfItems = in.readInt();
        shelfLife = in.readInt();
        baseSiUnits = in.readDouble();
        unitOfMeasureSubType = in.readInt();
        packAvePrice = in.readDouble();
        createdBy = in.readString();
        webImageUrl = in.readString();
        remoteSmallImageUri = in.readString();
        remoteMediumImageUri = in.readString();
        remoteLargeImageUri = in.readString();
        createDate = in.readLong();
        lastUpdate = in.readLong();
        remoteProductId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeString(madeBy);
        dest.writeInt(category);
        dest.writeInt(numberOfItems);
        dest.writeInt(shelfLife);
        dest.writeDouble(baseSiUnits);
        dest.writeInt(unitOfMeasureSubType);
        dest.writeDouble(packAvePrice);
        dest.writeString(createdBy);
        dest.writeString(webImageUrl);
        dest.writeString(remoteSmallImageUri);
        dest.writeString(remoteMediumImageUri);
        dest.writeString(remoteLargeImageUri);
        dest.writeLong(createDate);
        dest.writeLong(lastUpdate);
        dest.writeString(remoteProductId);
    }

    @Override
    public int describeContents() {
        return 0;
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

    @Exclude // HashMap for FireBase community product_uneditable information Map
    public Map<String, Object> commProductToMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put(DESCRIPTION, description);
        result.put(MADE_BY, madeBy);
        result.put(CATEGORY, category);
        result.put(NUMBER_OF_ITEMS, numberOfItems);
        result.put(SHELF_LIFE, shelfLife);
        result.put(BASE_SI_UNITS, baseSiUnits);
        result.put(UNIT_OF_MEASURE_SUB_TYPE, unitOfMeasureSubType);
        result.put(PROD_COMM_PRICE_AVE, packAvePrice);
        result.put(CREATED_BY, createdBy);
        result.put(WEB_IMAGE_URL, webImageUrl);
        result.put(REMOTE_SMALL_IMAGE_URI, remoteSmallImageUri);
        result.put(REMOTE_MEDIUM_IMAGE_URI, remoteMediumImageUri);
        result.put(REMOTE_LARGE_IMAGE_URI, remoteLargeImageUri);
        result.put(CREATE_DATE, createDate);
        result.put(LAST_UPDATE, lastUpdate);

        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    public void setBaseSiUnits(double baseSiUnits) {
        this.baseSiUnits = baseSiUnits;
    }

    public int getUnitOfMeasureSubType() {
        return unitOfMeasureSubType;
    }

    public void setUnitOfMeasureSubType(int unitOfMeasureSubType) {
        this.unitOfMeasureSubType = unitOfMeasureSubType;
    }

    public double getPackAvePrice() {
        return packAvePrice;
    }

    public void setPackAvePrice(double packAvePrice) {
        this.packAvePrice = packAvePrice;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getWebImageUrl() {
        return webImageUrl;
    }

    public void setWebImageUrl(String webImageUrl) {
        this.webImageUrl = webImageUrl;
    }

    public String getRemoteSmallImageUri() {
        return remoteSmallImageUri;
    }

    public void setRemoteSmallImageUri(String remoteSmallImageUri) {
        this.remoteSmallImageUri = remoteSmallImageUri;
    }

    public String getRemoteMediumImageUri() {
        return remoteMediumImageUri;
    }

    public void setRemoteMediumImageUri(String remoteMediumImageUri) {
        this.remoteMediumImageUri = remoteMediumImageUri;
    }

    public String getRemoteLargeImageUri() {
        return remoteLargeImageUri;
    }

    public void setRemoteLargeImageUri(@NonNull String remoteLargeImageUri) {
        this.remoteLargeImageUri = remoteLargeImageUri;
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

    public String getRemoteProductId() {
        return remoteProductId;
    }

    public void setRemoteProductId(String fbProductReferenceKey) {
        this.remoteProductId = fbProductReferenceKey;
    }
}
