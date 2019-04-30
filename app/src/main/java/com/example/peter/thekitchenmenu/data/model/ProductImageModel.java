package com.example.peter.thekitchenmenu.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class ProductImageModel extends BaseObservable {

    private static final String TAG = "ProductImageModel";

    private String remoteImageUri = ""; // tkm remote server image location
    private String remoteThumbUri = "";
    private String webImageUrl = "";    // user selected web image location
    private String localImageUri = "";  // Camera or local gallery file location
    private String localThumbUri = "";

    @Bindable
    public String getRemoteImageUri() {

        return remoteImageUri;
    }

    public void setRemoteImageUri(String remoteImageUri) {

        this.remoteImageUri = remoteImageUri;
        notifyPropertyChanged(BR.remoteImageUri);
    }

    @Bindable
    public String getRemoteThumbUri() {

        return remoteThumbUri;
    }

    public void setRemoteThumbUri(String remoteThumbUri) {

        this.remoteThumbUri = remoteThumbUri;
        notifyPropertyChanged(BR.remoteThumbUri);
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
    public String getLocalThumbUri() {

        return localThumbUri;
    }

    public void setLocalThumbUri(String localThumbUri) {

        this.localThumbUri = localThumbUri;
        notifyPropertyChanged(BR.localThumbUri);
    }
}
