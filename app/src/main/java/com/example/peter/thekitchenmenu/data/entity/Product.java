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

import static com.example.peter.thekitchenmenu.data.entity.Product.TABLE_PRODUCT;

@Entity(tableName = TABLE_PRODUCT)
public class Product implements Parcelable {

    public static final String TAG = "Product";

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
    public static final String REMOTE_IMAGE_URI = "remoteImageUri";
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

    @ColumnInfo(name = NUMBER_OF_ITEMS)
    private int numberOfItems;

    @ColumnInfo(name = SHELF_LIFE)
    private int shelfLife;

    @ColumnInfo(name = BASE_SI_UNITS)
    private double baseSiUnits;

    @ColumnInfo(name = UNIT_OF_MEASURE_SUB_TYPE)
    private int unitOfMeasureSubType;

    @ColumnInfo(name = PROD_COMM_PRICE_AVE)
    private double packAvePrice;

    @ColumnInfo(name = CREATED_BY)
    private String createdBy;

    @ColumnInfo(name = REMOTE_IMAGE_URI)
    private String remoteImageUri = "";

    @ColumnInfo(name = CREATE_DATE)
    private long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private long lastUpdate;

    @ColumnInfo(name = REMOTE_PRODUCT_ID)
    private String remoteProductId;

    @Ignore
    public Product(){} /* Required by Firebase. */

    public Product(int id,
                   String description,
                   String madeBy,
                   int category,
                   int shelfLife,
                   int numberOfItems,
                   double baseSiUnits,
                   int unitOfMeasureSubType,
                   double packAvePrice,
                   String createdBy,
                   @NonNull String remoteImageUri,
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
        this.remoteImageUri = remoteImageUri;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.remoteProductId = remoteProductId;
    }

    @Ignore
    public Product(Parcel in) {
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
        remoteImageUri = in.readString();
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
        dest.writeString(remoteImageUri);
        dest.writeLong(createDate);
        dest.writeLong(lastUpdate);
        dest.writeString(remoteProductId);
    }

    @Override
    public int describeContents() {
        return 0;
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
        result.put(REMOTE_IMAGE_URI, remoteImageUri);
        result.put(CREATE_DATE, createDate);
        result.put(LAST_UPDATE, lastUpdate);

        return result;
    }

    @Override
    public String toString() {
        return "Product { \n" +
                "\nid=" + id +
                "\n description='" + description + '\'' +
                "\n madeBy='" + madeBy + '\'' +
                "\n category=" + category + '\'' +
                "\n number_of_items=" + numberOfItems + '\'' +
                "\n shelfLife=" + shelfLife + '\'' +
                "\n baseSiUnits=" + baseSiUnits +
                "\n unitOfMeasureSubType=" + unitOfMeasureSubType + '\'' +
                "\n packAvePrice=" + packAvePrice +
                "\n createdBy='" + createdBy + '\'' +
                "\n remoteImageUri='" + remoteImageUri + '\'' +
                "\n createDate=" + createDate +
                "\n lastUpdate=" + lastUpdate +
                "\n remoteProductId='" + remoteProductId + '\'' +
                '}';
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

    @NonNull
    public String getRemoteImageUri() {
        return remoteImageUri;
    }

    public void setRemoteImageUri(@NonNull String remoteImageUri) {
        this.remoteImageUri = remoteImageUri;
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
