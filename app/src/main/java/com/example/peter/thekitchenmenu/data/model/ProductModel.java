package com.example.peter.thekitchenmenu.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;

import static com.example.peter.thekitchenmenu.app.Constants.*;
import static com.example.peter.thekitchenmenu.data.entity.ProductEntity.*;
import static com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity.*;

import java.util.HashMap;
import java.util.Map;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

// TODO, split this class out into product data and users product data models
public class ProductModel extends BaseObservable implements Parcelable {

    private static final String TAG = "ProductModel";

    // ImageModel fields
    private String remoteImageUri;
    private String localImageUri;

    // ProductIdentityModel fields
    private String description;
    private String madeBy;
    private int category;
    private int shelfLife;

    // ProductMeasurementModel fields
    private double baseSiUnits;
    private int numberOfItems;
    private int unitOfMeasureSubType;

    // ProductUserDataModel fields.
    private int userProductDataId;
    private long usersProductDataCreateDate;
    private long usersProductDataLastUpdate;
    private String retailer;
    private double price;
    private String locationRoom;
    private String locationInRoom;

    // ProductEntity fields.
    private int localProductId;
    private String remoteProductId;
    private String remoteUsedProductId;
    private String createdBy;
    private long productCreateDate;
    private long productLastUpdate;


    private double packAvePrice;

    public ProductModel(){}

    // For merging data models
    public ProductModel(ProductUserDataEntity productUserDataEntity, ProductEntity productEntity){
        this.userProductDataId = productUserDataEntity.getId();
        this.remoteProductId = productUserDataEntity.getRemoteProductId();
        this.remoteUsedProductId = productUserDataEntity.getRemoteIdUsedProduct();
        this.retailer = productUserDataEntity.getRetailer();
        this.locationRoom = productUserDataEntity.getLocationRoom();
        this.locationInRoom = productUserDataEntity.getLocationInRoom();
        this.price = productUserDataEntity.getPrice();
        this.localImageUri = productUserDataEntity.getLocalImageUri();
        this.usersProductDataCreateDate = productUserDataEntity.getCreateDate();
        this.usersProductDataLastUpdate = productUserDataEntity.getLastUpdate();

        this.localProductId = productEntity.getId();
        this.remoteProductId = productEntity.getRemoteProductId();
        this.description = productEntity.getDescription();
        this.madeBy = productEntity.getMadeBy();
        this.category = productEntity.getCategory();
        this.shelfLife = productEntity.getShelfLife();
        this.numberOfItems = productEntity.getNumberOfItems();
        this.baseSiUnits = productEntity.getBaseSiUnits();
        this.unitOfMeasureSubType = productEntity.getUnitOfMeasureSubType();
        this.packAvePrice = productEntity.getPackAvePrice();
        this.createdBy = productEntity.getCreatedBy();
        this.remoteImageUri = productEntity.getRemoteLargeImageUri();
        this.productCreateDate = productEntity.getCreateDate();
        this.productLastUpdate = productEntity.getLastUpdate();
    }

    // For creating a view model of a ProductEntity only
    public ProductModel(ProductEntity productEntity) {
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

        this.localProductId = productEntity.getId();
        this.description = productEntity.getDescription();
        this.madeBy = productEntity.getMadeBy();
        this.category = productEntity.getCategory();
        this.shelfLife = productEntity.getShelfLife();
        this.numberOfItems = productEntity.getNumberOfItems();
        this.baseSiUnits = productEntity.getBaseSiUnits();
        this.unitOfMeasureSubType = productEntity.getUnitOfMeasureSubType();
        this.packAvePrice = productEntity.getPackAvePrice();
        this.createdBy = productEntity.getCreatedBy();
        this.remoteImageUri = productEntity.getRemoteLargeImageUri();
        this.productCreateDate = productEntity.getCreateDate();
        this.productLastUpdate = productEntity.getLastUpdate();
        this.remoteProductId = productEntity.getRemoteProductId();
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
            int localProductId,
            String remoteProductId,
            String description,
            String madeBy,
            int category,
            int shelfLife,
            int numberOfItems,
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
        this.localProductId = localProductId;
        this.remoteProductId = remoteProductId;
        this.description = description;
        this.madeBy = madeBy;
        this.category = category;
        this.shelfLife = shelfLife;
        this.numberOfItems = numberOfItems;
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
        localProductId = in.readInt();
        remoteProductId = in.readString();
        description = in.readString();
        madeBy = in.readString();
        category = in.readInt();
        shelfLife = in.readInt();
        numberOfItems = in.readInt();
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
        result.put(NUMBER_OF_ITEMS, numberOfItems);
        result.put(BASE_SI_UNITS, baseSiUnits);
        result.put(UNIT_OF_MEASURE_SUB_TYPE, unitOfMeasureSubType);
        result.put(PROD_COMM_PRICE_AVE, packAvePrice);
        result.put(CREATED_BY, createdBy);
        result.put(REMOTE_LARGE_IMAGE_URI, remoteImageUri);
        result.put(ProductEntity.CREATE_DATE, productCreateDate);
        result.put(ProductEntity.LAST_UPDATE, productLastUpdate);

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
        result.put(NUMBER_OF_ITEMS, numberOfItems);
        result.put(BASE_SI_UNITS, baseSiUnits);
        result.put(UNIT_OF_MEASURE_SUB_TYPE, unitOfMeasureSubType);
        result.put(PROD_COMM_PRICE_AVE, packAvePrice);
        result.put(CREATED_BY, createdBy);
        result.put(REMOTE_LARGE_IMAGE_URI, remoteImageUri);
        result.put(ProductEntity.CREATE_DATE, productCreateDate);
        result.put(ProductEntity.LAST_UPDATE, productLastUpdate);

        // My product_uneditable specific fields
        result.put(REMOTE_USED_PRODUCT_ID, remoteUsedProductId);
        result.put(RETAILER, retailer);
        result.put(LOCATION_ROOM, locationRoom);
        result.put(LOCATION_IN_ROOM, locationInRoom);
        result.put(PRICE, price);
        result.put(LOCAL_IMAGE_URI, localImageUri);
        result.put(ProductUserDataEntity.CREATE_DATE, usersProductDataCreateDate);
        result.put(ProductUserDataEntity.LAST_UPDATE, usersProductDataLastUpdate);
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
        parcel.writeInt(localProductId);
        parcel.writeString(remoteProductId);
        parcel.writeString(description);
        parcel.writeString(madeBy);
        parcel.writeInt(category);
        parcel.writeInt(shelfLife);
        parcel.writeInt(numberOfItems);
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
        return "tkm - ProductModel{" +
                "userProductDataId: '" + userProductDataId +
                ", remoteProdMyRefKey: '" + remoteProductId + '\'' +
                ", remoteUsedProductId: '" + remoteUsedProductId + '\'' +
                ", retailer: '" + retailer + '\'' +
                ", locationRoom: '" + locationRoom + '\'' +
                ", locationInRoom: '" + locationInRoom + '\'' +
                ", price: '" + price + '\'' +
                ", localImageUri: '" + localImageUri + '\'' +
                ", usersProductDataCreateDate: '" + usersProductDataCreateDate + '\'' +
                ", usersProductDataLastUpdate: '" + usersProductDataLastUpdate + '\'' +
                ", localProductId: '" + localProductId + '\'' +
                ", remoteProductId: '" + remoteProductId + '\'' +
                ", description: '" + description + '\'' +
                ", madeBy: '" + madeBy + '\'' +
                ", category: " + category + '\'' +
                ", shelfLife: " + shelfLife + '\'' +
                ", numberOfItems: '" + numberOfItems + '\'' +
                ", baseSiUnits: '" + baseSiUnits + '\'' +
                ", unitOfMeasureSubType: '" + unitOfMeasureSubType + '\'' +
                ", packAvePrice: '" + packAvePrice + '\'' +
                ", createdBy: '" + createdBy + '\'' +
                ", remoteImageUri: '" + remoteImageUri + '\'' +
                ", productCreateDate: '" + productCreateDate + '\'' +
                ", productLastUpdate: '" + productLastUpdate + '\'' +
                '}';
    }

    public void getValuesFromEntity(ProductEntity entity) {

        Log.d(TAG, "tkm - getValuesFromEntity: getting values");

        setDescription(entity.getDescription());
        setMadeBy(entity.getMadeBy());
        setCategory(entity.getCategory());
        setNumberOfItems(entity.getNumberOfItems());
        setShelfLife(entity.getShelfLife());
        setBaseSiUnits(entity.getBaseSiUnits());
        setUnitOfMeasureSubType(entity.getUnitOfMeasureSubType());
        setPackAvePrice(entity.getPackAvePrice());
        setCreatedBy(entity.getCreatedBy());
        setRemoteImageUri(entity.getRemoteLargeImageUri());
        setProductCreateDate(entity.getCreateDate());
        setProductLastUpdate(entity.getLastUpdate());
        setRemoteProductId(entity.getRemoteProductId());
    }

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

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
        notifyPropertyChanged(BR.madeBy);
    }

    @Bindable
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable
    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
        notifyPropertyChanged(BR.shelfLife);
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
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

    public void setProductCreateDate(long productCreateDate) {
        this.productCreateDate = productCreateDate;
    }

    public long getProductLastUpdate() {
        return productLastUpdate;
    }

    public void setProductLastUpdate(long productLastUpdate) {
        this.productLastUpdate = productLastUpdate;
    }

    public int getLocalProductId() {
        return this.localProductId;
    }

    public void setLocalProductId(int localProductId) {
        this.localProductId = localProductId;
    }

    public String getRemoteProductId() {
        return remoteProductId;
    }

    public void setRemoteProductId(String remoteProductId) {
        this.remoteProductId = remoteProductId;
    }
}
