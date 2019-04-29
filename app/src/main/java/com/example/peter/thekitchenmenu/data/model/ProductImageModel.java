package com.example.peter.thekitchenmenu.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class ProductImageModel extends BaseObservable {

    private static final String TAG = "ProductImageModel";

    private String remoteImageUri = "";
    private String localImageUri = "";

    @Bindable
    public String getRemoteImageUri() {

        return remoteImageUri;
    }

    public void setRemoteImageUri(String remoteImageUri) {

        this.remoteImageUri = remoteImageUri;
        notifyPropertyChanged(BR.remoteImageUri);
    }

    @Bindable
    public String getLocalImageUri() {

        return localImageUri;
    }

    public void setLocalImageUri(String localImageUri) {

        this.localImageUri = localImageUri;
        notifyPropertyChanged(BR.localImageUri);
    }
}
