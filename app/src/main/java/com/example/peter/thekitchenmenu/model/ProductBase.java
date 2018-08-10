package com.example.peter.thekitchenmenu.model;

import com.example.peter.thekitchenmenu.app.Constants;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the basic information about a product.
 * For user specific information see the ProductUser.cass.
 */
@IgnoreExtraProperties
public class ProductBase {

    private String description;
    private String madeBy;
    private int category;
    private int shelfLife;
    private int packSize;
    private int unitOfMeasure;
    private double packPrice;
    private String createdBy;

    public ProductBase(){}

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.PRODUCT_BASE_DESCRIPTION_KEY, description);
        result.put(Constants.PRODUCT_BASE_MADE_BY_KEY, madeBy);
        result.put(Constants.PRODUCT_BASE_CATEGORY_KEY, category);
        result.put(Constants.PRODUCT_BASE_SHELF_LIFE_KEY, shelfLife);
        result.put(Constants.PRODUCT_BASE_PACK_SIZE_KEY, packSize);
        result.put(Constants.PRODUCT_BASE_UNIT_OF_MEASURE_KEY, unitOfMeasure);
        result.put(Constants.PRODUCT_BASE_PRICE_AVE_KEY, packPrice);
        result.put(Constants.PRODUCT_BASE_CREATED_BY_KEY, createdBy);

        return result;
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

    public int getShelfLife() {return shelfLife;}

    public void setShelfLife(int shelfLife) {this.shelfLife = shelfLife;}

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

    public double getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(double packPrice) {
        this.packPrice = packPrice;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
