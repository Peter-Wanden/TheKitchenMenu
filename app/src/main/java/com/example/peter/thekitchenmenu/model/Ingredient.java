package com.example.peter.thekitchenmenu.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.util.TableInfo;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.peter.thekitchenmenu.app.Constants;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Ingredient",

        foreignKeys = {
            @ForeignKey(entity = Recipe.class,
                parentColumns = "id",
                childColumns = "Recipe_Id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),

            @ForeignKey(entity = Product.class,
                parentColumns = "id",
                childColumns = "Product_Id",
                onDelete = ForeignKey.NO_ACTION)},

                indices = {@Index("Recipe_Id"),@Index("Product_Id")})

public class Ingredient implements Parcelable{

    @PrimaryKey
    @ColumnInfo(name = "Id")
    private int mId;

    @ColumnInfo(name = "Recipe_Id")
    private int mRecipeId;

    @ColumnInfo(name = "Product_Id")
    private int mProductId;

    @ColumnInfo(name = "OneMg_Is_N_Grams")
    private double mOneMgIsNGrams;

    @ColumnInfo(name = "Quantity_Per_Serving")
    private double mQuantityPerServing;

    @ColumnInfo(name = "Freeze")
    private boolean mFreeze;

    /* Constructor */
    public Ingredient(int id,
                      int recipeId,
                      int productId,
                      double oneMgIsNGrams,
                      double quantityPerServing,
                      boolean freeze) {

        this.mId = id;
        this.mRecipeId = recipeId;
        this.mProductId = productId;
        this.mOneMgIsNGrams = oneMgIsNGrams;
        this.mQuantityPerServing = quantityPerServing;
        this.mFreeze = freeze;
    }

    protected Ingredient(Parcel in) {
        mId = in.readInt();
        mRecipeId = in.readInt();
        mProductId = in.readInt();
        mOneMgIsNGrams = in.readDouble();
        mQuantityPerServing = in.readDouble();
        mFreeze = in.readByte() != 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mId);
        dest.writeInt(mRecipeId);
        dest.writeInt(mProductId);
        dest.writeDouble(mOneMgIsNGrams);
        dest.writeDouble(mQuantityPerServing);
        dest.writeByte((byte) (mFreeze ? 1 : 0));
    }

    /* Getters and setters */
    public int getId() {return mId;}

    public void setIngredientId(int Id) {this.mId = Id;}

    public int getRecipeId() {return mRecipeId;}

    public void setRecipeId(int RecipeId) {this.mRecipeId = RecipeId;}

    public int getProductId() {return mProductId;}

    public void setProductId(int productId) {mProductId = productId;}

    public double getOneMgIsNGrams() {return mOneMgIsNGrams;}

    public void setOneMgIsNGrams(double OneMgIsNGrams) {this.mOneMgIsNGrams = OneMgIsNGrams;}

    public double getQuantityPerServing() {return mQuantityPerServing;}

    public void setQuantityPerServing(double QuantityPerServing) {
        this.mQuantityPerServing = QuantityPerServing;}

    public boolean isFreeze() {return mFreeze;}

    public void setFreeze(boolean Freeze) {this.mFreeze = Freeze;}
}
