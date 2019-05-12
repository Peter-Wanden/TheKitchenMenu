package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.imageeditor.LastImageUpdated;

public class ImageEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ImageEditorViewModel";

    private boolean deviceHasCamera = false;

    // TODO - Clear out the storage directory for new products
    private MutableLiveData<ImageModel> imageModel = new MutableLiveData<>();
    private ImageModel newImageModel = new ImageModel();

    private MutableLiveData<Boolean> imageModelIsValid = new MutableLiveData<>();

    private LastImageUpdated lastImageUpdated = LastImageUpdated.NO_IMAGE;
    private boolean newImageDataAvailable = false;

    // SingleLiveEvent - see https://github.com/googlesamples/android-architecture
    private final SingleLiveEvent<Void> checkCameraHardware = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchCameraEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchGalleryEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchBrowserEvent = new SingleLiveEvent<>();

    public ImageEditorViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ImageModel> getImageModel() {
        return imageModel;
    }

    public ImageModel getNewImageModel() {
        return newImageModel;
    }

    public void setNewImageModel(ImageModel newImageModel) {
        this.newImageModel = newImageModel;
    }

    public MutableLiveData<Boolean> getImageModelIsValid() {
        return imageModelIsValid;
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

    public boolean isDeviceHasCamera() {
        return deviceHasCamera;
    }

        public void setDeviceHasCamera(boolean deviceHasCamera) {

        this.deviceHasCamera = deviceHasCamera;
        Log.d(TAG, "tkm - setDeviceHasCamera: " + deviceHasCamera);
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

    public void launchBrowser() {
        launchBrowserEvent.call();
    }

    public SingleLiveEvent<Void> getLaunchBrowserEvent() {
        return launchBrowserEvent;
    }

    public void setLocalImageUris(String localSmallImageUri,
                                  String localMediumImageUri,
                                  String localLargeImageUri) {

        newImageModel.setLocalSmallImageUri(localSmallImageUri);
        newImageModel.setLocalMediumImageUri(localMediumImageUri);
        newImageModel.setLocalLargeImageUri(localLargeImageUri);

        getImageModel().setValue(newImageModel);
    }
}
