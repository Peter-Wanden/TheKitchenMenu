package com.example.peter.thekitchenmenu.data.model;

import android.util.Log;

import com.example.peter.thekitchenmenu.BR;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ProductIdentityModel extends BaseObservable {

    private static final String TAG = "ProductIdentityModel";

    private String description = "";
    private String shoppingListItemName = "";
    private int category;
    private int shelfLife;

    @Bindable
    public String getDescription() {
        Log.d(TAG, "tkm - getDescription: " + description);
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        Log.d(TAG, "tkm - setDescription: " + description);
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getShoppingListItemName() {
        return shoppingListItemName;
    }

    public void setShoppingListItemName(String shoppingListItemName) {
        this.shoppingListItemName = shoppingListItemName;
        notifyPropertyChanged(BR.shoppingListItemName);
    }

    @Bindable
    public int getCategory() {
        Log.d(TAG, "tkm - getCategory: " + category);
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
        Log.d(TAG, "tkm - setCategory: " + category);
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

    @NonNull
    @Override
    public String toString() {
        return "tkm - ProductIdentityModel values: " +
                "\ndescription: " + description +
                "\nshoppingListName: " + shoppingListItemName +
                "\ncategory: " + category +
                "\nshelfLife: " + shelfLife;
    }
}
