package com.example.peter.thekitchenmenu.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.entity.UsersProductData;

import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PROD_MY_ID;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_REMOTE_REF_ID;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_FB_USED_PRODUCT_ID;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PRODUCT_RETAILER;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PRODUCT_LOC;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PRODUCT_LOC_IN_ROOM;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_PRODUCT_PRICE;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_LOCAL_IMAGE_URI;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_MY_CREATE_DATE;
import static com.example.peter.thekitchenmenu.app.Constants.DEFAULT_MY_LAST_UPDATE;
import static com.example.peter.thekitchenmenu.data.entity.Product.CATEGORY;
import static com.example.peter.thekitchenmenu.data.entity.Product.CREATED_BY;
import static com.example.peter.thekitchenmenu.data.entity.Product.DESCRIPTION;
import static com.example.peter.thekitchenmenu.data.entity.Product.MADE_BY;
import static com.example.peter.thekitchenmenu.data.entity.Product.PACK_SIZE;
import static com.example.peter.thekitchenmenu.data.entity.Product.PROD_COMM_PRICE_AVE;
import static com.example.peter.thekitchenmenu.data.entity.Product.REMOTE_PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.Product.SHELF_LIFE;
import static com.example.peter.thekitchenmenu.data.entity.Product.UNIT_OF_MEASURE;
import static com.example.peter.thekitchenmenu.data.entity.Product.REMOTE_IMAGE_URI;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.LOCAL_IMAGE_URI;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.LOCATION_IN_ROOM;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.LOCATION_ROOM;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.PRICE;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.RETAILER;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.REMOTE_USED_PRODUCT_ID;

import java.util.HashMap;
import java.util.Map;

public class ProductModel implements Parcelable {

    // UsersProductData fields.
    private int prodMyId;
    private String remoteProdMyRefKey;
    private String fbUsedProdUserKey;
    private String retailer;
    private String locationRoom;
    private String locationInRoom;
    private double packPrice;
    private String localImageUri;
    private long createDate;
    private long myLastUpdate;

    // ProductComm fields.
    private int prodCommId;
    private String remoteProdCommRefKey;
    private String description;
    private String madeBy;
    private int category;
    private int shelfLife;
    private int packSize;
    private int unitOfMeasure;
    private double packAvePrice;
    private String createdBy;
    private String fbStorageImageUri;
    private long commCreateDate;
    private long commLastUpdate;

    // TODO - create getters and setters for the different ID's and references.

    public ProductModel(){}

    // Constructor for merging data models
    public ProductModel(UsersProductData usersProductData, Product product){
        this.prodMyId = usersProductData.getId();
        this.remoteProdMyRefKey = usersProductData.getRemoteProductId();
        this.fbUsedProdUserKey = usersProductData.getRemoteIdUsedProduct();
        this.retailer = usersProductData.getRetailer();
        this.locationRoom = usersProductData.getLocationRoom();
        this.locationInRoom = usersProductData.getLocationInRoom();
        this.packPrice = usersProductData.getPrice();
        this.localImageUri = usersProductData.getLocalImageUri();
        this.createDate = usersProductData.getCreateDate();
        this.myLastUpdate = usersProductData.getLastUpdate();

        this.prodCommId = product.getId();
        this.remoteProdCommRefKey = product.getRemoteProductId();
        this.description = product.getDescription();
        this.madeBy = product.getMadeBy();
        this.category = product.getCategory();
        this.shelfLife = product.getShelfLife();
        this.packSize = product.getPackSize();
        this.unitOfMeasure = product.getUnitOfMeasure();
        this.packAvePrice = product.getPackAvePrice();
        this.createdBy = product.getCreatedBy();
        this.fbStorageImageUri = product.getRemoteImageUri();
        this.commCreateDate = product.getCreateDate();
        this.commLastUpdate = product.getLastUpdate();
    }

    // Constructor for creating a view model of a Product only
    public ProductModel(Product product) {
        this.prodMyId = DEFAULT_PROD_MY_ID;
        this.remoteProdMyRefKey = DEFAULT_REMOTE_REF_ID;
        this.fbUsedProdUserKey = DEFAULT_FB_USED_PRODUCT_ID;
        this.retailer = DEFAULT_PRODUCT_RETAILER;
        this.locationRoom = DEFAULT_PRODUCT_LOC;
        this.locationInRoom = DEFAULT_PRODUCT_LOC_IN_ROOM;
        this.packPrice = DEFAULT_PRODUCT_PRICE;
        this.localImageUri = DEFAULT_LOCAL_IMAGE_URI;
        this.createDate = DEFAULT_MY_CREATE_DATE;
        this.myLastUpdate = DEFAULT_MY_LAST_UPDATE;

        this.prodCommId = product.getId();
        this.remoteProdCommRefKey = product.getRemoteProductId();
        this.description = product.getDescription();
        this.madeBy = product.getMadeBy();
        this.category = product.getCategory();
        this.shelfLife = product.getShelfLife();
        this.packSize = product.getPackSize();
        this.unitOfMeasure = product.getUnitOfMeasure();
        this.packAvePrice = product.getPackAvePrice();
        this.createdBy = product.getCreatedBy();
        this.fbStorageImageUri = product.getRemoteImageUri();
        this.commCreateDate = product.getCreateDate();
        this.commLastUpdate = product.getLastUpdate();
    }

    public ProductModel(
            int productMyId,
            String fbProductReferenceKey,
            String fbUsedProductsUserKey,
            String retailer,
            String locationRoom,
            String locationInRoom,
            double packPrice,
            String localImageUri,
            long myCreateDate,
            long myLastUpdate,
            int prodCommId,
            String remoteRefId,
            String description,
            String madeBy,
            int category,
            int shelfLife,
            int packSize,
            int unitOfMeasure,
            double packAvePrice,
            String createdBy,
            String fbStorageImageUri,
            long commCreateDate,
            long commLastUpdate) {

        this.prodMyId = productMyId;
        this.fbUsedProdUserKey = fbUsedProductsUserKey;
        this.retailer = retailer;
        this.locationRoom = locationRoom;
        this.locationInRoom = locationInRoom;
        this.packPrice = packPrice;
        this.localImageUri = localImageUri;
        this.createDate = myCreateDate;
        this.myLastUpdate = myLastUpdate;
        this.description = description;
        this.madeBy = madeBy;
        this.category = category;
        this.shelfLife = shelfLife;
        this.packSize = packSize;
        this.unitOfMeasure = unitOfMeasure;
        this.packAvePrice = packAvePrice;
        this.createdBy = createdBy;
        this.fbStorageImageUri = fbStorageImageUri;
        this.commCreateDate = commCreateDate;
        this.commLastUpdate = commLastUpdate;
        this.prodCommId = prodCommId;
        this.remoteProdMyRefKey = fbProductReferenceKey;
    }

    protected ProductModel(Parcel in) {
        prodMyId = in.readInt();
        remoteProdMyRefKey = in.readString();
        fbUsedProdUserKey = in.readString();
        retailer = in.readString();
        locationRoom = in.readString();
        locationInRoom = in.readString();
        packPrice = in.readDouble();
        localImageUri = in.readString();
        createDate = in.readLong();
        myLastUpdate = in.readLong();
        prodCommId = in.readInt();
        remoteProdCommRefKey = in.readString();
        description = in.readString();
        madeBy = in.readString();
        category = in.readInt();
        shelfLife = in.readInt();
        packSize = in.readInt();
        unitOfMeasure = in.readInt();
        packAvePrice = in.readDouble();
        createdBy = in.readString();
        fbStorageImageUri = in.readString();
        commCreateDate = in.readLong();
        commLastUpdate = in.readLong();
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
        result.put(PACK_SIZE, packSize);
        result.put(UNIT_OF_MEASURE, unitOfMeasure);
        result.put(PROD_COMM_PRICE_AVE, packAvePrice);
        result.put(CREATED_BY, createdBy);
        result.put(REMOTE_IMAGE_URI, fbStorageImageUri);
        result.put(Product.CREATE_DATE, commCreateDate);
        result.put(Product.LAST_UPDATE, commLastUpdate);

        return result;
    }

    // HashMap for FireBase user products information Map
    public Map<String, Object> userFieldsToMap() {

        HashMap<String, Object> result = new HashMap<>();

        // All fields are required
        // Community product fields
        result.put(DESCRIPTION, description);
        result.put(MADE_BY, madeBy);
        result.put(CATEGORY, category);
        result.put(SHELF_LIFE, shelfLife);
        result.put(PACK_SIZE, packSize);
        result.put(UNIT_OF_MEASURE, unitOfMeasure);
        result.put(PROD_COMM_PRICE_AVE, packAvePrice);
        result.put(CREATED_BY, createdBy);
        result.put(REMOTE_IMAGE_URI, fbStorageImageUri);
        result.put(Product.CREATE_DATE, commCreateDate);
        result.put(Product.LAST_UPDATE, commLastUpdate);

        // My product specific fields
        result.put(REMOTE_USED_PRODUCT_ID, fbUsedProdUserKey);
        result.put(RETAILER, retailer);
        result.put(LOCATION_ROOM, locationRoom);
        result.put(LOCATION_IN_ROOM, locationInRoom);
        result.put(PRICE, packPrice);
        result.put(LOCAL_IMAGE_URI, localImageUri);
        result.put(CREATE_DATE, createDate);
        result.put(LAST_UPDATE, myLastUpdate);
        result.put(REMOTE_PRODUCT_ID, remoteProdMyRefKey);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(prodMyId);
        parcel.writeString(remoteProdMyRefKey);
        parcel.writeString(fbUsedProdUserKey);
        parcel.writeString(retailer);
        parcel.writeString(locationRoom);
        parcel.writeString(locationInRoom);
        parcel.writeDouble(packPrice);
        parcel.writeString(localImageUri);
        parcel.writeLong(createDate);
        parcel.writeLong(myLastUpdate);
        parcel.writeInt(prodCommId);
        parcel.writeString(remoteProdCommRefKey);
        parcel.writeString(description);
        parcel.writeString(madeBy);
        parcel.writeInt(category);
        parcel.writeInt(shelfLife);
        parcel.writeInt(packSize);
        parcel.writeInt(unitOfMeasure);
        parcel.writeDouble(packAvePrice);
        parcel.writeString(createdBy);
        parcel.writeString(fbStorageImageUri);
        parcel.writeLong(commCreateDate);
        parcel.writeLong(commLastUpdate);
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "prodMyId=" + prodMyId +
                ", remoteProdMyRefKey='" + remoteProdMyRefKey + '\'' +
                ", fbUsedProdUserKey='" + fbUsedProdUserKey + '\'' +
                ", retailer='" + retailer + '\'' +
                ", locationRoom='" + locationRoom + '\'' +
                ", locationInRoom='" + locationInRoom + '\'' +
                ", packPrice=" + packPrice +
                ", localImageUri='" + localImageUri + '\'' +
                ", createDate=" + createDate +
                ", myLastUpdate=" + myLastUpdate +
                ", prodCommId=" + prodCommId +
                ", remoteProdCommRefKey='" + remoteProdCommRefKey + '\'' +
                ", description='" + description + '\'' +
                ", madeBy='" + madeBy + '\'' +
                ", category=" + category +
                ", shelfLife=" + shelfLife +
                ", packSize=" + packSize +
                ", unitOfMeasure=" + unitOfMeasure +
                ", packAvePrice=" + packAvePrice +
                ", createdBy='" + createdBy + '\'' +
                ", fbStorageImageUri='" + fbStorageImageUri + '\'' +
                ", commCreateDate=" + commCreateDate +
                ", commLastUpdate=" + commLastUpdate +
                '}';
    }

    /* Getters and setters */
    public int getProductMyId() {
        return this.prodMyId;
    }

    public void setProductMyId(int productMyId) {
        this.prodMyId = productMyId;
    }

    public String getFbUsedProductsUserKey() {
        return fbUsedProdUserKey;
    }

    public void setFbUsedProductsUserKey(String fbUsedProductsUserKey) {
        this.fbUsedProdUserKey = fbUsedProductsUserKey;
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

    public double getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(double packPrice) {
        this.packPrice = packPrice;
    }

    public String getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(String localImageUri) {
        this.localImageUri = localImageUri;
    }

    public long getMyCreateDate() {
        return createDate;
    }

    public void setMyCreateDate(long myCreateDate) {
        this.createDate = myCreateDate;
    }

    public long getMyLastUpdate() {
        return myLastUpdate;
    }

    public void setMyLastUpdate(long myLastUpdate) {
        this.myLastUpdate = myLastUpdate;
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

    public int getPackSize() {
        return packSize;
    }

    public void setPackSize(int packSize) {
        this.packSize = packSize;
    }

    public int getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(int unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
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

    public String getFbStorageImageUri() {
        return fbStorageImageUri;
    }

    public void setFbStorageImageUri(String fbStorageImageUri) {
        this.fbStorageImageUri = fbStorageImageUri;
    }

    public long getCommCreateDate() {
        return commCreateDate;
    }

    public void setCommCreateDate(long createDate) {
        this.commCreateDate = createDate;
    }

    public long getCommLastUpdate() {
        return commLastUpdate;
    }

    public void setCommLastUpdate(long commLastUpdate) {
        this.commLastUpdate = commLastUpdate;
    }

    public int getProductCommId() {
        return this.prodCommId;
    }

    public void setProductCommId(int productCommId) {
        this.prodCommId = productCommId;
    }

    public String getFbProductReferenceKey() {
        return remoteProdMyRefKey;
    }

    public void setFbProductReferenceKey(String fbProductReferenceKey) {
        this.remoteProdMyRefKey = fbProductReferenceKey;
    }
}
