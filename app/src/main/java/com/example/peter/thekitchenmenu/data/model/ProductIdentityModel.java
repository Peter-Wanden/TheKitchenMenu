package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.BR;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ProductIdentityModel extends BaseObservable {

    private static final String TAG = "tkm - ProductIdentityModel";

    private String description = "";
    private String shoppingListItemName = "";
    private int category;
    private int shelfLife;

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
