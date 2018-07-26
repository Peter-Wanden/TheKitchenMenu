package com.example.peter.thekitchenmenu.model;

import android.arch.persistence.room.ColumnInfo;

public class IngredientAndProduct {

    @ColumnInfo(name = "productId")
    private int mId;

    @ColumnInfo(name = "productDescription")
    private String mDescription;

    @ColumnInfo(name = "UoM")
    private int UoM;

    @ColumnInfo(name = "retailer")
    private String mRetailer;

    @ColumnInfo(name = "ingredientId")
    private int mIngredientId;

    @ColumnInfo(name = "oneMgIsNGrams")
    private double mOneMgIsNGrams;

    @ColumnInfo(name = "quantityPerServing")
    private double mQuantityPerServing;

    @ColumnInfo(name = "freeze")
    private boolean mFreeze;

    /* Constructor */
    public IngredientAndProduct(int id,
                                String description,
                                int UoM,
                                String retailer,
                                int ingredientId,
                                double oneMgIsNGrams,
                                double quantityPerServing,
                                boolean freeze) {
        this.mId = id;
        this.mDescription = description;
        this.UoM = UoM;
        this.mRetailer = retailer;
        this.mIngredientId = ingredientId;
        this.mOneMgIsNGrams = oneMgIsNGrams;
        this.mQuantityPerServing = quantityPerServing;
        this.mFreeze = freeze;

    }

    public int getId() {return mId;}

    public String getDescription() {return mDescription;}

    public int getUoM() {return UoM;}

    public String getRetailer() {return mRetailer;}

    public int getIngredientId() {return mIngredientId;}

    public double getOneMgIsNGrams() {return mOneMgIsNGrams;}

    public double getQuantityPerServing() {return mQuantityPerServing;}

    public boolean getFreeze() {return mFreeze;}
}
