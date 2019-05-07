package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.data.model.ProductImageModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.imageeditor.ImageEditorHandler;
import com.example.peter.thekitchenmenu.utils.imageeditor.LastImageUpdated;
import com.example.peter.thekitchenmenu.utils.imageeditor.ProductImageEditorHandler;

public class ImageEditorViewModel extends ObservableViewModel {

    private boolean deviceHasCamera = false;
    private boolean hasCameraPermissions = false;

    private ProductImageModel imageModel = new ProductImageModel();
    private MutableLiveData<Boolean> imageModelIsValid = new MutableLiveData<>();
    private ImageEditorHandler imageEditorHandler = new ProductImageEditorHandler();

    private LastImageUpdated lastImageUpdated = LastImageUpdated.NO_IMAGE;
    private boolean newImageDataAvailable = false;

    // SingleLiveEvent - see https://github.com/googlesamples/android-architecture
    private final SingleLiveEvent<Void> checkCameraHardware = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchCameraEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchGalleryEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> rotateImageEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchBrowserEvent = new SingleLiveEvent<>();

    private String temporaryImagePath;

    public ImageEditorViewModel(@NonNull Application application) {
        super(application);
    }

    public ProductImageModel getImageModel() {
        return imageModel;
    }

    public MutableLiveData<Boolean> getImageModelIsValid() {
        return imageModelIsValid;
    }

    public ImageEditorHandler getImageEditorHandler() {
        return imageEditorHandler;
    }

    public LastImageUpdated getLastImageUpdated() {
        return lastImageUpdated;
    }

    public void setLastImageUpdated(LastImageUpdated lastImageUpdated) {
        this.lastImageUpdated = lastImageUpdated;
    }

    public void setNewImageDataAvailable(boolean newImageDataAvailable) {
        this.newImageDataAvailable = newImageDataAvailable;
    }

    public void setDeviceHasCamera(boolean deviceHasCamera) {

        this.deviceHasCamera = deviceHasCamera;
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
}
