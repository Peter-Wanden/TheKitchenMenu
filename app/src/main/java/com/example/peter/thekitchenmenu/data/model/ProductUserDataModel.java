package com.example.peter.thekitchenmenu.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.peter.thekitchenmenu.BR;

public class ProductUserDataModel extends BaseObservable {

    private static final String TAG = "ProductUserDataModel";

    private String retailer = "";
    private double price = 0.;
    private String locationRoom = "";
    private String locationInRoom = "";

    @Bindable
    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
        notifyPropertyChanged(BR.retailer);
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String getLocationRoom() {
        return locationRoom;
    }

    public void setLocationRoom(String locationRoom) {
        this.locationRoom = locationRoom;
        notifyPropertyChanged(BR.locationRoom);
    }

    @Bindable
    public String getLocationInRoom() {
        return locationInRoom;
    }

    public void setLocationInRoom(String locationInRoom) {
        this.locationInRoom = locationInRoom;
        notifyPropertyChanged(BR.locationInRoom);
    }
}
