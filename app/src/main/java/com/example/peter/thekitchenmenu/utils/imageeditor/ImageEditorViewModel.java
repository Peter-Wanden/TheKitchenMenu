package com.example.peter.thekitchenmenu.utils.imageeditor;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.imageeditor.LastImageUpdated;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

public class ImageEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ImageEditorViewModel";

    private final ObservableBoolean deviceHasCamera = new ObservableBoolean(false);

    // TODO - Clear out the storage directory for new products
    private final MutableLiveData<ImageModel> existingImageModel = new MutableLiveData<>();
    private ImageModel updatedImageModel = new ImageModel();
    private final MutableLiveData<Boolean> imageModelIsValid = new MutableLiveData<>();

    // SingleLiveEvent - see https://github.com/googlesamples/android-architecture
    private final SingleLiveEvent<Void> checkIfHardwareHasCameraEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchCameraEvent = new SingleLiveEvent<>();
    private static final int CAMERA_IMAGE_RESULT_OK = -1;

    private final SingleLiveEvent<Uri> cropFullSizeImageEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> deleteFullSizeImage = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> launchGalleryEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchBrowserEvent = new SingleLiveEvent<>();

    private Uri cameraImageFileUri = null;
    private String cameraImageFilePath = null;
    private LastImageUpdated lastImageUpdated = LastImageUpdated.NO_IMAGE;
    private boolean newImageDataAvailable = false;

    public ImageEditorViewModel(@NonNull Application application) {
        super(application);

        checkIfHardwareHasCamera();
    }

    public MutableLiveData<ImageModel> getExistingImageModel() {
        return existingImageModel;
    }

    public ImageModel getUpdatedImageModel() {
        return updatedImageModel;
    }

    public void checkIfHardwareHasCamera() {
        checkIfHardwareHasCameraEvent.call();
    }

    public SingleLiveEvent<Void> checkIfHardwareHasCameraEvent() {
        return checkIfHardwareHasCameraEvent;
    }

    public ObservableBoolean getDeviceHasCamera() {
        return deviceHasCamera;
    }

    public void launchCamera() {
        launchCameraEvent.call();
    }

    public SingleLiveEvent<Void> launchCameraEvent() {
        return launchCameraEvent;
    }

    public Uri getCameraImageFileUri() {
        return cameraImageFileUri;
    }

    public void setCameraImageFileUri(Uri cameraImageFileUri) {
        this.cameraImageFileUri = cameraImageFileUri;
    }

    public String getCameraImageFilePath() {
        return cameraImageFilePath;
    }

    public void setCameraImageFilePath(String cameraImageFilePath) {
        this.cameraImageFilePath = cameraImageFilePath;
    }

    public void launchGallery() {
        launchGalleryEvent.call();
    }

    public SingleLiveEvent<Void> launchGalleryEvent() {
        return launchGalleryEvent;
    }

    public void cameraImageResult(int cameraImageResult) {

        if (cameraImageResult == CAMERA_IMAGE_RESULT_OK) cropFullSizeImageEvent.call();
        else deleteFullSizeImage.call();
    }

    public SingleLiveEvent<Uri> cropFullSizeImageEvent() {
        return cropFullSizeImageEvent;
    }

    public SingleLiveEvent<Void> deleteFullSizeImageEvent() {
        return deleteFullSizeImage;
    }

    public void launchBrowser() {
        launchBrowserEvent.call();
    }

    public SingleLiveEvent<Void> launchBrowserEvent() {
        return launchBrowserEvent;
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

    public void setLocalImageUris(String localSmallImageUri,
                                  String localMediumImageUri,
                                  String localLargeImageUri) {

        updatedImageModel.setLocalSmallImageUri(localSmallImageUri);
        updatedImageModel.setLocalMediumImageUri(localMediumImageUri);
        updatedImageModel.setLocalLargeImageUri(localLargeImageUri);

        getExistingImageModel().setValue(updatedImageModel);
    }
}
