package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ProductIdentityModel extends BaseObservable {

    private static final String TAG = "ProductIdentityModel";

    private String description;
    private String madeBy;
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
}
