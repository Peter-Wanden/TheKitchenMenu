package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.model.ProductImageModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

public class ImageEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ImageEditorViewModel";

    private ProductImageModel imageModel = new ProductImageModel();
    private final SingleLiveEvent<Void> launchGalleryEvent = new SingleLiveEvent<>();
    private String temporaryImagePath;

    public ImageEditorViewModel(@NonNull Application application) {
        super(application);
    }

    public ProductImageModel getImageModel() {
        return imageModel;
    }

    public void launchCamera() {
        Log.d(TAG, "tkm - launchCamera: detected");
    }

    public void launchGallery() {
        Log.d(TAG, "tkm - launchGallery: detected");
        launchGalleryEvent.call();
    }

    public SingleLiveEvent<Void> getLaunchGalleryEvent() {
        return launchGalleryEvent;
    }

    public void rotateImage() {
        Log.d(TAG, "rotateImage: detected");
    }

    public String getTemporaryImagePath() {
        return temporaryImagePath;
    }

    public void setTemporaryImagePath(String temporaryImagePath) {
        this.temporaryImagePath = temporaryImagePath;
    }
}
