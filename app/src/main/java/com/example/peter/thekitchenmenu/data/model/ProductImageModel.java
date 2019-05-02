package com.example.peter.thekitchenmenu.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class ProductImageModel extends BaseObservable {

    private static final String TAG = "ProductImageModel";

    private String remoteImageUri = ""; // tkm remote server image location
    private String remoteImageThumbUri = "";
    private String webImageUrl = "";    // user selected web image location
    private String localImageUri = "";  // Camera or local gallery file location
    private String localImageThumbUri = "";

    @Bindable
    public String getRemoteImageUri() {

        return remoteImageUri;
    }

    public void setRemoteImageUri(String remoteImageUri) {

        this.remoteImageUri = remoteImageUri;
        notifyPropertyChanged(BR.remoteImageUri);
    }

    @Bindable
    public String getRemoteImageThumbUri() {

        return remoteImageThumbUri;
    }

    public void setRemoteImageThumbUri(String remoteImageThumbUri) {

        this.remoteImageThumbUri = remoteImageThumbUri;
        notifyPropertyChanged(BR.remoteImageThumbUri);
    }

    @Bindable
    public String getWebImageUrl() {

        return webImageUrl;
    }

    public void setWebImageUrl(String webImageUrl) {

        this.webImageUrl = webImageUrl;
        notifyPropertyChanged(BR.webImageUrl);
    }

    @Bindable
    public String getLocalImageUri() {

        return localImageUri;
    }

    public void setLocalImageUri(String localImageUri) {

        this.localImageUri = localImageUri;
        notifyPropertyChanged(BR.localImageUri);
    }

    @Bindable
    public String getLocalImageThumbUri() {

        return localImageThumbUri;
    }

    public void setLocalImageThumbUri(String localImageThumbUri) {

        this.localImageThumbUri = localImageThumbUri;
        notifyPropertyChanged(BR.localImageThumbUri);
    }
}
