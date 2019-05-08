package com.example.peter.thekitchenmenu.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class ImageModel extends BaseObservable {

    private static final String TAG = "ImageModel";

    private String remoteLargeImageUri = ""; // remote server image location
    private String remoteMediumImageUri = "";
    private String remoteSmallImageUri = "";
    private String webImageUrl = "";    // user selected web image location

    private String localLargeImageUri = "";  // Camera or local gallery file location
    private String localMediumImageUri = "";
    private String localSmallImageUri = "";

    @Bindable
    public String getRemoteLargeImageUri() {
        return remoteLargeImageUri;
    }

    public void setRemoteLargeImageUri(String remoteLargeImageUri) {

        this.remoteLargeImageUri = remoteLargeImageUri;
        notifyPropertyChanged(BR.remoteLargeImageUri);
    }

    public String getRemoteMediumImageUri() {
        return remoteMediumImageUri;
    }

    public void setRemoteMediumImageUri(String remoteMediumImageUri) {
        this.remoteMediumImageUri = remoteMediumImageUri;
    }

    public String getRemoteSmallImageUri() {
        return remoteSmallImageUri;
    }

    public void setRemoteSmallImageUri(String remoteSmallImageUri) {
        this.remoteSmallImageUri = remoteSmallImageUri;
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
    public String getLocalLargeImageUri() {
        return localLargeImageUri;
    }

    public void setLocalLargeImageUri(String localLargeImageUri) {
        this.localLargeImageUri = localLargeImageUri;
        notifyPropertyChanged(BR.localLargeImageUri);
    }

    public String getLocalMediumImageUri() {
        return localMediumImageUri;
    }

    public void setLocalMediumImageUri(String localMediumImageUri) {
        this.localMediumImageUri = localMediumImageUri;
    }

    public String getLocalSmallImageUri() {
        return localSmallImageUri;
    }

    public void setLocalSmallImageUri(String localSmallImageUri) {
        this.localSmallImageUri = localSmallImageUri;
    }
}
