package com.example.peter.thekitchenmenu.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.entity.UsersProductData;

import static com.example.peter.thekitchenmenu.app.Constants.*;
import static com.example.peter.thekitchenmenu.data.entity.Product.*;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.*;

import java.util.HashMap;
import java.util.Map;

public class ProductModel implements Parcelable {

    // UsersProductData fields.
    private int userProductDataId;
    // private String remoteProdMyRefKey; // TODO - find the root of this var
    private String remoteUsedProductId;
    private String retailer;
    private String locationRoom;
    private String locationInRoom;
    private double price;
    private String localImageUri;
    private long usersProductDataCreateDate;
    private long usersProductDataLastUpdate;

    // Product fields.
    private int productId;
    private String remoteProductId;
    private String description;
    private String madeBy;
    private int category;
    private int shelfLife;
    private double baseSiUnits;
    private int unitOfMeasureSubType;
    private double packAvePrice;
    private String createdBy;
    private String remoteImageUri;
    private long productCreateDate;
    private long productLastUpdate;

    public ProductModel(){}

    // For merging data models
    public ProductModel(UsersProductData usersProductData, Product product){
        this.userProductDataId = usersProductData.getId();
        this.remoteProductId = usersProductData.getRemoteProductId();
        this.remoteUsedProductId = usersProductData.getRemoteIdUsedProduct();
        this.retailer = usersProductData.getRetailer();
        this.locationRoom = usersProductData.getLocationRoom();
        this.locationInRoom = usersProductData.getLocationInRoom();
        this.price = usersProductData.getPrice();
        this.localImageUri = usersProductData.getLocalImageUri();
        this.usersProductDataCreateDate = usersProductData.getCreateDate();
        this.usersProductDataLastUpdate = usersProductData.getLastUpdate();

        this.productId = product.getId();
        this.remoteProductId = product.getRemoteProductId();
        this.description = product.getDescription();
        this.madeBy = product.getMadeBy();
        this.category = product.getCategory();
        this.shelfLife = product.getShelfLife();
        this.baseSiUnits = product.getBaseSiUnits();
        this.unitOfMeasureSubType = product.getUnitOfMeasureSubType();
        this.packAvePrice = product.getPackAvePrice();
        this.createdBy = product.getCreatedBy();
        this.remoteImageUri = product.getRemoteImageUri();
        this.productCreateDate = product.getCreateDate();
        this.productLastUpdate = product.getLastUpdate();
    }

    // For creating a view model of a Product only
    public ProductModel(Product product) {
        this.userProductDataId = DEFAULT_PROD_MY_ID;
        this.remoteProductId = DEFAULT_REMOTE_REF_ID;
        this.remoteUsedProductId = DEFAULT_REMOTE_USED_PRODUCT_ID;
        this.retailer = DEFAULT_PRODUCT_RETAILER;
        this.locationRoom = DEFAULT_PRODUCT_LOC;
        this.locationInRoom = DEFAULT_PRODUCT_LOC_IN_ROOM;
        this.price = DEFAULT_PRODUCT_PRICE;
        this.localImageUri = DEFAULT_LOCAL_IMAGE_URI;
        this.usersProductDataCreateDate = DEFAULT_MY_CREATE_DATE;
        this.usersProductDataLastUpdate = DEFAULT_MY_LAST_UPDATE;

        this.productId = product.getId();
        this.remoteProductId = product.getRemoteProductId();
        this.description = product.getDescription();
        this.madeBy = product.getMadeBy();
        this.category = product.getCategory();
        this.shelfLife = product.getShelfLife();
        this.baseSiUnits = product.getBaseSiUnits();
        this.unitOfMeasureSubType = product.getUnitOfMeasureSubType();
        this.packAvePrice = product.getPackAvePrice();
        this.createdBy = product.getCreatedBy();
        this.remoteImageUri = product.getRemoteImageUri();
        this.productCreateDate = product.getCreateDate();
        this.productLastUpdate = product.getLastUpdate();
    }

    public ProductModel(
            int userProductDataId,
            String remoteUserProductId,
            String remoteUsedProductId,
            String retailer,
            String locationRoom,
            String locationInRoom,
            double price,
            String localImageUri,
            long usersProductDataCreateDate,
            long usersProductDataLastUpdate,
            int productId,
            String remoteProductId,
            String description,
            String madeBy,
            int category,
            int shelfLife,
            double baseSiUnits,
            int unitOfMeasureSubType,
            double packAvePrice,
            String createdBy,
            String remoteImageUri,
            long productCreateDate,
            long productLastUpdate) {

        this.userProductDataId = userProductDataId;
        this.remoteProductId = remoteUserProductId;
        this.remoteUsedProductId = remoteUsedProductId;
        this.retailer = retailer;
        this.locationRoom = locationRoom;
        this.locationInRoom = locationInRoom;
        this.price = price;
        this.localImageUri = localImageUri;
        this.usersProductDataCreateDate = usersProductDataCreateDate;
        this.usersProductDataLastUpdate = usersProductDataLastUpdate;
        this.productId = productId;
        this.remoteProductId = remoteProductId;
        this.description = description;
        this.madeBy = madeBy;
        this.category = category;
        this.shelfLife = shelfLife;
        this.baseSiUnits = baseSiUnits;
        this.unitOfMeasureSubType = unitOfMeasureSubType;
        this.packAvePrice = packAvePrice;
        this.createdBy = createdBy;
        this.remoteImageUri = remoteImageUri;
        this.productCreateDate = productCreateDate;
        this.productLastUpdate = productLastUpdate;
    }

    protected ProductModel(Parcel in) {
        userProductDataId = in.readInt();
        remoteProductId = in.readString();
        remoteUsedProductId = in.readString();
        retailer = in.readString();
        locationRoom = in.readString();
        locationInRoom = in.readString();
        price = in.readDouble();
        localImageUri = in.readString();
        usersProductDataCreateDate = in.readLong();
        usersProductDataLastUpdate = in.readLong();
        productId = in.readInt();
        remoteProductId = in.readString();
        description = in.readString();
        madeBy = in.readString();
        category = in.readInt();
        shelfLife = in.readInt();
        baseSiUnits = in.readDouble();
        unitOfMeasureSubType = in.readInt();
        packAvePrice = in.readDouble();
        createdBy = in.readString();
        remoteImageUri = in.readString();
        productCreateDate = in.readLong();
        productLastUpdate = in.readLong();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    // HashMap for FireBase community products information Map
    public Map<String, Object> commProductToMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put(DESCRIPTION, description);
        result.put(MADE_BY, madeBy);
        result.put(CATEGORY, category);
        result.put(SHELF_LIFE, shelfLife);
        result.put(BASE_SI_UNITS, baseSiUnits);
        result.put(UNIT_OF_MEASURE_SUB_TYPE, unitOfMeasureSubType);
        result.put(PROD_COMM_PRICE_AVE, packAvePrice);
        result.put(CREATED_BY, createdBy);
        result.put(REMOTE_IMAGE_URI, remoteImageUri);
        result.put(Product.CREATE_DATE, productCreateDate);
        result.put(Product.LAST_UPDATE, productLastUpdate);

        return result;
    }

    // HashMap for FireBase user products information Map
    public Map<String, Object> userFieldsToMap() {
        HashMap<String, Object> result = new HashMap<>();

        // All fields are required
        result.put(DESCRIPTION, description);
        result.put(MADE_BY, madeBy);
        result.put(CATEGORY, category);
        result.put(SHELF_LIFE, shelfLife);
        result.put(BASE_SI_UNITS, baseSiUnits);
        result.put(UNIT_OF_MEASURE_SUB_TYPE, unitOfMeasureSubType);
        result.put(PROD_COMM_PRICE_AVE, packAvePrice);
        result.put(CREATED_BY, createdBy);
        result.put(REMOTE_IMAGE_URI, remoteImageUri);
        result.put(Product.CREATE_DATE, productCreateDate);
        result.put(Product.LAST_UPDATE, productLastUpdate);

        // My product_uneditable specific fields
        result.put(REMOTE_USED_PRODUCT_ID, remoteUsedProductId);
        result.put(RETAILER, retailer);
        result.put(LOCATION_ROOM, locationRoom);
        result.put(LOCATION_IN_ROOM, locationInRoom);
        result.put(PRICE, price);
        result.put(LOCAL_IMAGE_URI, localImageUri);
        result.put(UsersProductData.CREATE_DATE, usersProductDataCreateDate);
        result.put(UsersProductData.LAST_UPDATE, usersProductDataLastUpdate);
        result.put(REMOTE_PRODUCT_ID, remoteProductId);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userProductDataId);
        parcel.writeString(remoteProductId);
        parcel.writeString(remoteUsedProductId);
        parcel.writeString(retailer);
        parcel.writeString(locationRoom);
        parcel.writeString(locationInRoom);
        parcel.writeDouble(price);
        parcel.writeString(localImageUri);
        parcel.writeLong(usersProductDataCreateDate);
        parcel.writeLong(usersProductDataLastUpdate);
        parcel.writeInt(productId);
        parcel.writeString(remoteProductId);
        parcel.writeString(description);
        parcel.writeString(madeBy);
        parcel.writeInt(category);
        parcel.writeInt(shelfLife);
        parcel.writeDouble(baseSiUnits);
        parcel.writeInt(unitOfMeasureSubType);
        parcel.writeDouble(packAvePrice);
        parcel.writeString(createdBy);
        parcel.writeString(remoteImageUri);
        parcel.writeLong(productCreateDate);
        parcel.writeLong(productLastUpdate);
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "userProductDataId=" + userProductDataId +
                ", remoteProdMyRefKey='" + remoteProductId + '\'' +
                ", remoteUsedProductId='" + remoteUsedProductId + '\'' +
                ", retailer='" + retailer + '\'' +
                ", locationRoom='" + locationRoom + '\'' +
                ", locationInRoom='" + locationInRoom + '\'' +
                ", price=" + price +
                ", localImageUri='" + localImageUri + '\'' +
                ", usersProductDataCreateDate=" + usersProductDataCreateDate +
                ", usersProductDataLastUpdate=" + usersProductDataLastUpdate +
                ", productId=" + productId +
                ", remoteProductId='" + remoteProductId + '\'' +
                ", description='" + description + '\'' +
                ", madeBy='" + madeBy + '\'' +
                ", category=" + category +
                ", shelfLife=" + shelfLife +
                ", baseSiUnits=" + baseSiUnits +
                ", unitOfMeasureSubType=" + unitOfMeasureSubType +
                ", packAvePrice=" + packAvePrice +
                ", createdBy='" + createdBy + '\'' +
                ", remoteImageUri='" + remoteImageUri + '\'' +
                ", productCreateDate=" + productCreateDate +
                ", productLastUpdate=" + productLastUpdate +
                '}';
    }

    /* Getters and setters */
    public int getUserProductDataId() {
        return this.userProductDataId;
    }

    public void setUserProductDataId(int productMyId) {
        this.userProductDataId = productMyId;
    }

    public String getRemoteUsedProductId() {
        return remoteUsedProductId;
    }

    // TODO - this doesn't look right
    public void setFbUsedProductsUserKey(String fbUsedProductsUserKey) {
        this.remoteUsedProductId = fbUsedProductsUserKey;
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

    public String getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(String localImageUri) {
        this.localImageUri = localImageUri;
    }

    public long getUsersProductDataCreateDate() {
        return usersProductDataCreateDate;
    }

    public void setUsersProductDataCreateDate(long myCreateDate) {
        this.usersProductDataCreateDate = myCreateDate;
    }

    public long getUsersProductDataLastUpdate() {
        return usersProductDataLastUpdate;
    }

    public void setUsersProductDataLastUpdate(long usersProductDataLastUpdate) {
        this.usersProductDataLastUpdate = usersProductDataLastUpdate;
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

    public String getRemoteImageUri() {
        return remoteImageUri;
    }

    public void setRemoteImageUri(String remoteImageUri) {
        this.remoteImageUri = remoteImageUri;
    }

    public long getProductCreateDate() {
        return productCreateDate;
    }

    public void setProductCreateDate(long createDate) {
        this.productCreateDate = createDate;
    }

    public long getProductLastUpdate() {
        return productLastUpdate;
    }

    public void setProductLastUpdate(long productLastUpdate) {
        this.productLastUpdate = productLastUpdate;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getRemoteProductId() {
        return remoteProductId;
    }

    public void setRemoteProductId(String remoteProductId) {
        this.remoteProductId = remoteProductId;
    }
}
