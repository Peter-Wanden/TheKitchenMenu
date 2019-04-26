package com.example.peter.thekitchenmenu.data.model;

public class ProductImageModel {

    private static final String TAG = "ProductImageModel";

    private String remoteImageUri;
    private String localImageUri;

    public String getRemoteImageUri() {
        return remoteImageUri;
    }

    public void setRemoteImageUri(String remoteImageUri) {
        this.remoteImageUri = remoteImageUri;
    }

    public String getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(String localImageUri) {
        this.localImageUri = localImageUri;
    }
}
