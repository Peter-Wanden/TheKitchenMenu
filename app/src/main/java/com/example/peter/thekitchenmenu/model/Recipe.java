package com.example.peter.thekitchenmenu.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "Recipes")
public class Recipe implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mRecipeId;

    @ColumnInfo(name = "Title")
    private String mTitle;

    @ColumnInfo(name = "Description")
    private String mDescription;

    @ColumnInfo(name = "Category")
    private int mCategory;

    @ColumnInfo(name = "Servings")
    private int mServings;

    @ColumnInfo(name = "Sittings")
    private int mSittings;

    @Ignore
    /* Constructors */
    public Recipe(String title,
                  String description, int category, int servings, int sittings) {

        this.mTitle = title;
        this.mDescription = description;
        this.mCategory = category;
        this.mServings = servings;
        this.mSittings = sittings;
    }

    public Recipe(int recipeId, String title,
                  String description, int category, int servings, int sittings) {

        this.mRecipeId = recipeId;
        this.mTitle = title;
        this.mDescription = description;
        this.mCategory = category;
        this.mServings = servings;
        this.mSittings = sittings;
    }


    protected Recipe(Parcel in) {

        mRecipeId = in.readInt();
        mTitle = in.readString();
        mDescription = in.readString();
        mCategory = in.readInt();
        mServings = in.readInt();
        mSittings = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(mRecipeId);
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeInt(mCategory);
        parcel.writeInt(mServings);
        parcel.writeInt(mSittings);
    }

    /* Getters and setters */
    public int getRecipeId() {return mRecipeId;}
    public void setRecipeId(int recipeId) {mRecipeId = recipeId;}

    public String getTitle() {return mTitle;}
    public void setTitle(String title) {mTitle = title;}

    public String getDescription() {return mDescription;}
    public void setDescription(String description) {mDescription = description;}

    public int getCategory() {return mCategory;}
    public void setCategory(int category) {mCategory = category;}

    public int getServings() {return mServings;}
    public void setServings(int servings) {mServings = servings;}

    public int getSittings() {return mSittings;}
    public void setSittings(int sittings) {mSittings = sittings;}
}
