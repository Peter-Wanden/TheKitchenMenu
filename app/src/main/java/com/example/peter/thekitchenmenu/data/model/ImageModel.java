package com.example.peter.thekitchenmenu.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class ImageModel extends BaseObservable {

    private static final String TAG = "ImageModel";

    public static final String FULL_SIZE_IMAGE_FILE_PREFIX = "IE_FULL_";
    private String localFullSizeImageUri = "";

    public static final String LARGE_IMAGE_FILE_PREFIX = "IE_LARGE_";
    private String remoteLargeImageUri = "";
    private String localLargeImageUri = "";

    public static final String MEDIUM_IMAGE_FILE_PREFIX = "IE_MEDIUM_";
    private String remoteMediumImageUri = "";
    private String localMediumImageUri = "";

    public static final String SMALL_IMAGE_FILE_PREFIX = "IE_SMALL_";
    private String remoteSmallImageUri = "";
    private String localSmallImageUri = "";

    private String webImageUrl = "";

    public String getLocalFullSizeImageUri() {
        return localFullSizeImageUri;
    }

    @Bindable
    public String getRemoteLargeImageUri() {
        return remoteLargeImageUri;
    }

    public void setRemoteLargeImageUri(String remoteLargeImageUri) {

        this.remoteLargeImageUri = remoteLargeImageUri;
        notifyPropertyChanged(BR.remoteLargeImageUri);
    }

    @Bindable
    public String getLocalLargeImageUri() {
        return localLargeImageUri;
    }

    public void setLocalLargeImageUri(String localLargeImageUri) {
        this.localLargeImageUri = localLargeImageUri;
        notifyPropertyChanged(BR.localLargeImageUri);
    }


    public String getRemoteMediumImageUri() {
        return remoteMediumImageUri;
    }

    public void setRemoteMediumImageUri(String remoteMediumImageUri) {
        this.remoteMediumImageUri = remoteMediumImageUri;
    }

    public String getLocalMediumImageUri() {
        return localMediumImageUri;
    }

    public void setLocalMediumImageUri(String localMediumImageUri) {
        this.localMediumImageUri = localMediumImageUri;
    }


    public String getRemoteSmallImageUri() {
        return remoteSmallImageUri;
    }

    public void setRemoteSmallImageUri(String remoteSmallImageUri) {
        this.remoteSmallImageUri = remoteSmallImageUri;
    }

    public String getLocalSmallImageUri() {
        return localSmallImageUri;
    }

    public void setLocalSmallImageUri(String localSmallImageUri) {
        this.localSmallImageUri = localSmallImageUri;
    }


    @Bindable
    public String getWebImageUrl() {
        return webImageUrl;
    }

    public void setWebImageUrl(String webImageUrl) {
        this.webImageUrl = webImageUrl;
        notifyPropertyChanged(BR.webImageUrl);
    }
}
