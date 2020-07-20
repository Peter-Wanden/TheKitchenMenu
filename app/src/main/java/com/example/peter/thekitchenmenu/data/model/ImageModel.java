package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.common.BaseObservable;

import javax.annotation.Nonnull;

public class ImageModel extends BaseObservable {

    private static final String TAG = "tkm - ImageModel";

    public static final String FULL_SIZE_IMAGE_FILE_PREFIX = "IE_FULL_";
    public static final String LARGE_IMAGE_FILE_PREFIX = "IE_LARGE_";
    public static final String MEDIUM_IMAGE_FILE_PREFIX = "IE_MEDIUM_";
    public static final String SMALL_IMAGE_FILE_PREFIX = "IE_SMALL_";

    private String remoteLargeImageUri;
    private String localLargeImageUri;

    private String remoteMediumImageUri;
    private String localMediumImageUri;

    private String remoteSmallImageUri;
    private String localSmallImageUri;

    private String webImageUrl;

    public ImageModel(){}

    public ImageModel(String remoteLargeImageUri,
                      String localLargeImageUri,
                      String remoteMediumImageUri,
                      String localMediumImageUri,
                      String remoteSmallImageUri,
                      String localSmallImageUri,
                      String webImageUrl) {
        this.remoteLargeImageUri = remoteLargeImageUri;
        this.localLargeImageUri = localLargeImageUri;
        this.remoteMediumImageUri = remoteMediumImageUri;
        this.localMediumImageUri = localMediumImageUri;
        this.remoteSmallImageUri = remoteSmallImageUri;
        this.localSmallImageUri = localSmallImageUri;
        this.webImageUrl = webImageUrl;
    }

    public String getRemoteLargeImageUri() {
        return remoteLargeImageUri;
    }

    public void setRemoteLargeImageUri(String remoteLargeImageUri) {
        this.remoteLargeImageUri = remoteLargeImageUri;
    }

    public String getLocalLargeImageUri() {
        return localLargeImageUri;
    }

    public void setLocalLargeImageUri(String localLargeImageUri) {
        this.localLargeImageUri = localLargeImageUri;
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

    public String getWebImageUrl() {
        return webImageUrl;
    }

    public void setWebImageUrl(String webImageUrl) {
        this.webImageUrl = webImageUrl;
    }

    @Nonnull
    @Override
    public String toString() {
        return "tkm-ImageModel{" +
                "\nremoteLargeImageUrl="  + remoteLargeImageUri +
                "\nremoteMediumImageUri=" + remoteMediumImageUri +
                "\nremoteSmallImageUri="  + remoteSmallImageUri +
                "\nlocalLargeImageUri="   + localLargeImageUri +
                "\nlocalMediumImageUri="  + localMediumImageUri +
                "\nlocalSmallImageUri="   + localSmallImageUri +
                "\nwebImageUrl="          + webImageUrl +
                '}';
    }
}
