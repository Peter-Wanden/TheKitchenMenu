package com.example.peter.thekitchenmenu.ui.imageeditor;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.ui.ObservableAndroidViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.peter.thekitchenmenu.app.Constants.FILE_PROVIDER_AUTHORITY;
import static com.example.peter.thekitchenmenu.data.model.ImageModel.*;

public class ImageEditorViewModel extends ObservableAndroidViewModel {

    private static final String TAG = "tkm-ImageEditorVM";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_IMPORT = 2;

    private Application appContext;
    private final MutableLiveData<ImageModel> existingImageModel = new MutableLiveData<>();
    private ImageModel updatedImageModel = new ImageModel();

    private static final long MAX_FILE_AGE = 60000 * 5; // five minutes TODO Change to 3 days
    private File fullSizeImageFile = null;
    private File smallImageFile = null;
    private File mediumImageFile = null;
    private File largeImageFile = null;

    private final ObservableBoolean canTakePictures = new ObservableBoolean(false);

    private final SingleLiveEvent<Uri> getImageFromCameraEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> getImageFromGalleryEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchBrowserEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> cropFullSizeImageEvent = new SingleLiveEvent<>();

    public ImageEditorViewModel(@NonNull Application application) {
        super(application);

        appContext = application;
        cleanUpCacheDirectory();
        checkIfCanTakePictures();
    }

    void onConfigurationChange() {
        if (updatedImageModel.getLocalLargeImageUri() != null)
            existingImageModel.setValue(updatedImageModel);
    }

    public MutableLiveData<ImageModel> getExistingImageModel() {
        return existingImageModel;
    }

    ImageModel getUpdatedImageModel() {
        return updatedImageModel;
    }

    private void cleanUpCacheDirectory() {
        File[] cacheDirectoryFileList = appContext.getCacheDir().listFiles();

        if (cacheDirectoryFileList != null && cacheDirectoryFileList.length > 0)
            filterImageEditorFiles(cacheDirectoryFileList);
    }

    private void filterImageEditorFiles(File[] cacheDirectoryFileList) {
        List<File> imageEditorCacheFileList = new ArrayList<>();

        for (File cachedFile : cacheDirectoryFileList) {
            if (cachedFileBelongsToImageEditor(cachedFile))
                imageEditorCacheFileList.add(cachedFile);
        }
        deleteOutOfDateFiles(imageEditorCacheFileList);
    }

    private boolean cachedFileBelongsToImageEditor(File file) {
        String filename = file.getName();

        return filename.startsWith(FULL_SIZE_IMAGE_FILE_PREFIX) ||
                filename.startsWith(LARGE_IMAGE_FILE_PREFIX) ||
                filename.startsWith(MEDIUM_IMAGE_FILE_PREFIX) ||
                filename.startsWith(SMALL_IMAGE_FILE_PREFIX) ||
                filename.startsWith("cropped");
    }

    private void deleteOutOfDateFiles(List<File> imageEditorCacheFileList) {
        long timeNow = System.currentTimeMillis();

        for (File file : imageEditorCacheFileList) {
            long lastModified = file.lastModified();
            long fileAge = timeNow - lastModified;

            if (fileAge > MAX_FILE_AGE) {
                try {
                    boolean fileIsDeleted = file.delete();
                    if (!fileIsDeleted) Log.e(
                            TAG, "deleteOutOfDateFiles: Can't delete file:" + file.getName());
                } catch (SecurityException e) {
                    Log.e(TAG, "deleteOutOfDateFiles: security exception: " + e);
                }
            }
        }
    }

    private void checkIfCanTakePictures() {
        boolean hasCamera = appContext.getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean hasCameraApp = takePictureIntent.resolveActivity(
                appContext.getPackageManager()) != null;

        canTakePictures.set(hasCamera && hasCameraApp);
    }

    public ObservableBoolean getCanTakePictures() {
        return canTakePictures;
    }

    public void getImageFromCamera() {
        fullSizeImageFile = createImageFile(FULL_SIZE_IMAGE_FILE_PREFIX);
        getImageFromCameraEvent.setValue(getFullSizeImagePublicUri());
    }

    SingleLiveEvent<Uri> getImageFromCameraEvent() {
        return getImageFromCameraEvent;
    }

    private Uri getFullSizeImagePublicUri() {

        return FileProvider.getUriForFile(
                appContext,
                FILE_PROVIDER_AUTHORITY,
                fullSizeImageFile);
    }

    File getFullSizeImageFile() {
        return fullSizeImageFile;
    }

    private File createImageFile(String filePrefix) {
        File newImageFile = null;

        try {
            newImageFile = BitmapUtils.createTemporaryImageFile(appContext, filePrefix);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newImageFile;
    }

    public void getImageFromGallery() {
        getImageFromGalleryEvent.call();
    }

    SingleLiveEvent<Void> getImageFromGalleryEvent() {
        return getImageFromGalleryEvent;
    }

    SingleLiveEvent<Void> cropFullSizeImageEvent() {
        return cropFullSizeImageEvent;
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
            cropFullSizeImageEvent().call();

        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED)
            BitmapUtils.deleteImageFile(appContext, fullSizeImageFile.getAbsolutePath());

        else if (requestCode == REQUEST_IMAGE_IMPORT && resultCode == RESULT_OK && data != null) {
            Uri galleryImageUri = data.getData();
            importGalleryImage(galleryImageUri);
        }
        else if (requestCode == REQUEST_IMAGE_IMPORT && resultCode == RESULT_CANCELED)
            BitmapUtils.deleteImageFile(appContext, fullSizeImageFile.getAbsolutePath());

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri croppedImageUri = result.getUri();
                importAndProcessCroppedImage(croppedImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, "onActivityResult: ", error);
            }
        }
    }

    private void importGalleryImage(Uri galleryImageUri) {
        Bitmap importedImage = getBitmapFromImageUri(galleryImageUri);
        fullSizeImageFile = createImageFile(FULL_SIZE_IMAGE_FILE_PREFIX);

        if (importedImage != null && bitmapSavedToCacheFile(importedImage, fullSizeImageFile))
            cropFullSizeImageEvent.call();
    }

    private Bitmap getBitmapFromImageUri(Uri uriOfImageToImport) {
        try {
            return MediaStore.Images.Media.getBitmap(
                    appContext.getContentResolver(),
                    uriOfImageToImport);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error: " + e.getMessage() + "Could not open URI: " +
                    uriOfImageToImport);

            return null;
        }
    }

    private void importAndProcessCroppedImage(Uri croppedImageUri) {
        Bitmap croppedBitmap = getBitmapFromImageUri(croppedImageUri);

        if (croppedBitmap != null)
            createImageFilesFromCroppedBitMap(croppedBitmap);
    }

    private boolean bitmapSavedToCacheFile(Bitmap bitmapToSave, File fileToSaveBitmapTo) {
        return BitmapUtils.saveBitmapToCache(bitmapToSave, fileToSaveBitmapTo);
    }

    private void createImageFilesFromCroppedBitMap(Bitmap croppedImageBitmap) {
        createTemporaryImageFiles();

        Bitmap smallBitmap = BitmapUtils.createScaledBitmap(croppedImageBitmap, 75, true);
        Bitmap mediumBitmap = BitmapUtils.createScaledBitmap(croppedImageBitmap, 290, true);
        Bitmap largeBitMap = BitmapUtils.createScaledBitmap(croppedImageBitmap, 400, true);

        if (smallImageFile != null) {
            boolean smallImageSaved = BitmapUtils.saveBitmapToCache(smallBitmap, smallImageFile);

            if (smallImageSaved)
                updatedImageModel.setLocalSmallImageUri(FileProvider.getUriForFile(
                        appContext,
                        FILE_PROVIDER_AUTHORITY,
                        smallImageFile).
                        toString());
        }

        if (mediumImageFile != null) {
            boolean mediumImageSaved = BitmapUtils.saveBitmapToCache(mediumBitmap, mediumImageFile);

            if (mediumImageSaved)
                updatedImageModel.setLocalMediumImageUri(FileProvider.getUriForFile(
                        appContext,
                        FILE_PROVIDER_AUTHORITY,
                        mediumImageFile).
                        toString());
        }

        if (largeImageFile != null) {
            boolean largeImageSaved = BitmapUtils.saveBitmapToCache(largeBitMap, largeImageFile);

            if (largeImageSaved)
                updatedImageModel.setLocalLargeImageUri(FileProvider.getUriForFile(
                        appContext,
                        FILE_PROVIDER_AUTHORITY,
                        largeImageFile).
                        toString());
        }

        BitmapUtils.deleteImageFile(appContext, fullSizeImageFile.getAbsolutePath());

        updateExistingImageModelWithNewValues();
    }

    private void updateExistingImageModelWithNewValues() {
        existingImageModel.setValue(updatedImageModel);
    }

    private void createTemporaryImageFiles() {
        smallImageFile = createImageFile(ImageModel.SMALL_IMAGE_FILE_PREFIX);
        if (smallImageFile != null)
            updatedImageModel.setLocalSmallImageUri(FileProvider.getUriForFile(
                    appContext,
                    FILE_PROVIDER_AUTHORITY,
                    smallImageFile).
                    toString());

        mediumImageFile = createImageFile(ImageModel.MEDIUM_IMAGE_FILE_PREFIX);
        if (mediumImageFile != null)
            updatedImageModel.setLocalMediumImageUri(FileProvider.getUriForFile(
                    appContext,
                    FILE_PROVIDER_AUTHORITY,
                    mediumImageFile).
                    toString());

        largeImageFile = createImageFile(ImageModel.LARGE_IMAGE_FILE_PREFIX);
        if (largeImageFile != null)
            updatedImageModel.setLocalLargeImageUri(FileProvider.getUriForFile(
                    appContext,
                    FILE_PROVIDER_AUTHORITY,
                    largeImageFile).
                    toString());
    }

    public void launchBrowser() {
        launchBrowserEvent.call();
    }

    SingleLiveEvent<Void> launchBrowserEvent() {
        return launchBrowserEvent;
    }
}