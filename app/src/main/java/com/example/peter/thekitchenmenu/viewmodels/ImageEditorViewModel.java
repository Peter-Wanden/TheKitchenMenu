package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.model.ProductImageModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

public class ImageEditorViewModel extends ObservableViewModel {

    private ProductImageModel imageModel = new ProductImageModel();

    // SingleLiveEvent - see https://github.com/googlesamples/android-architecture
    private final SingleLiveEvent<Void> launchCameraEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchGalleryEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> rotateImageEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchBrowserEvent = new SingleLiveEvent<>();

    private String temporaryImagePath;
    private boolean imageHasChanged = false;

    public ImageEditorViewModel(@NonNull Application application) {
        super(application);
    }

    public ProductImageModel getImageModel() {
        return imageModel;
    }

    public void launchCamera() {
        launchCameraEvent.call();
    }

    public SingleLiveEvent<Void> getLaunchCameraEvent() {
        return launchCameraEvent;
    }

    public void launchGallery() {
        launchGalleryEvent.call();
    }

    public SingleLiveEvent<Void> getLaunchGalleryEvent() {
        return launchGalleryEvent;
    }

    public void rotateImage() {
        rotateImageEvent.call();
    }

    public SingleLiveEvent<Void> getRotateImageEvent() {
        return rotateImageEvent;
    }

    public void launchBrowser() {
        launchBrowserEvent.call();
    }

    public SingleLiveEvent<Void> getLaunchBrowserEvent() {
        return launchBrowserEvent;
    }

    public String getTemporaryImagePath() {
        return temporaryImagePath;
    }

    public void setTemporaryImagePath(String temporaryImagePath) {
        this.temporaryImagePath = temporaryImagePath;
    }

    public void setImageHasChanged(boolean imageHasChanged) {
        this.imageHasChanged = imageHasChanged;
    }
}
