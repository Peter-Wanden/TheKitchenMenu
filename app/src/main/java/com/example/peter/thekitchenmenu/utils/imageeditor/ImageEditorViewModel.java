package com.example.peter.thekitchenmenu.utils.imageeditor;

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
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.peter.thekitchenmenu.app.Constants.FILE_PROVIDER_AUTHORITY;
import static com.example.peter.thekitchenmenu.data.model.ImageModel.*;
import static com.example.peter.thekitchenmenu.utils.imageeditor.BitmapUtils.createScaledBitmap;
import static com.example.peter.thekitchenmenu.utils.imageeditor.BitmapUtils.saveBitmapToCache;

public class ImageEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ImageEditorViewModel";

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_IMPORT = 2;

    private Application appContext;
    private final MutableLiveData<ImageModel> existingImageModel = new MutableLiveData<>();
    private ImageModel updatedImageModel = new ImageModel();

    private static final long MAX_FILE_AGE = 60000 * 5; // five minutes TODO Change to 3 days
    private final SingleLiveEvent<String> deleteFullSizeImage = new SingleLiveEvent<>();
    private File fullSizeImageFile = null;
    private File smallImageFile = null;
    private File mediumImageFile = null;
    private File largeImageFile = null;

    // SingleLiveEvent - see https://github.com/googlesamples/android-architecture
    private final ObservableBoolean canTakePictures = new ObservableBoolean(false);
    private final SingleLiveEvent<Void> getImageFromCameraEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> getImageFromGalleryEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> launchBrowserEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> cropFullSizeImageEvent = new SingleLiveEvent<>();

    public ImageEditorViewModel(@NonNull Application application) {
        super(application);

        appContext = application;
        cleanUpCacheDirectory();
        createTemporaryImageFiles();
        checkIfCanTakePictures();
    }

    public MutableLiveData<ImageModel> getExistingImageModel() {
        return existingImageModel;
    }

    public ImageModel getUpdatedImageModel() {
        return updatedImageModel;
    }

    private void cleanUpCacheDirectory() {

        File[] cacheDirectoryFileList = appContext.getCacheDir().listFiles();
        if (cacheDirectoryFileList.length > 0) processCacheDirectoryFiles(cacheDirectoryFileList);
    }

    private void processCacheDirectoryFiles(File[] cacheDirectoryFileList) {

        List<File> imageEditorCacheFileList = new ArrayList<>();

        for (File cachedFile : cacheDirectoryFileList) {

            Log.d(TAG, "tkm - processCacheDirectoryFiles: " + cachedFile.getName());

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
                filename.startsWith(SMALL_IMAGE_FILE_PREFIX);
    }

    private void deleteOutOfDateFiles(List<File> imageEditorCacheFileList) {

        long timeNow = System.currentTimeMillis();

        for (File file : imageEditorCacheFileList) {

            long lastModified = file.lastModified();
            long fileAge = timeNow - lastModified;

            if (fileAge > MAX_FILE_AGE) {

                if (file.delete())
                    Log.d(TAG, "tkm - deleteOutOfDateFiles: file deleted: " + file.getName());
            }
        }
    }

    public File getFullSizeImageFile() {
        return fullSizeImageFile;
    }

    private void createTemporaryImageFiles() {

        fullSizeImageFile = createImageFile(ImageModel.FULL_SIZE_IMAGE_FILE_PREFIX);

        smallImageFile = createImageFile(ImageModel.SMALL_IMAGE_FILE_PREFIX);
        if (smallImageFile != null) {

            updatedImageModel.setLocalSmallImageUri(FileProvider.getUriForFile(
                    appContext,
                    FILE_PROVIDER_AUTHORITY,
                    smallImageFile).toString());
        }

        mediumImageFile = createImageFile(ImageModel.MEDIUM_IMAGE_FILE_PREFIX);
        if (mediumImageFile != null) {

            updatedImageModel.setLocalMediumImageUri(FileProvider.getUriForFile(
                    appContext,
                    FILE_PROVIDER_AUTHORITY,
                    mediumImageFile).toString());
        }

        largeImageFile = createImageFile(ImageModel.LARGE_IMAGE_FILE_PREFIX);
        if (largeImageFile != null) {

            updatedImageModel.setLocalLargeImageUri(FileProvider.getUriForFile(
                            appContext,
                            FILE_PROVIDER_AUTHORITY,
                            largeImageFile).toString());
        }
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

    private void checkIfCanTakePictures() {

        boolean hasCamera = appContext.getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean hasCameraApp = takePictureIntent.resolveActivity(
                appContext.getPackageManager()) != null;

        canTakePictures.set(hasCamera && hasCameraApp);
    }

    public void getImageFromCamera() {
        getImageFromCameraEvent.call();
    }

    public SingleLiveEvent<Void> getImageFromCameraEvent() {
        return getImageFromCameraEvent;
    }

    public void getImageFromGallery() {
        getImageFromGalleryEvent.call();
    }

    public SingleLiveEvent<Void> getImageFromGalleryEvent() {
        return getImageFromGalleryEvent;
    }

    public void cameraImageResult(int cameraImageResult) {

        if (cameraImageResult == RESULT_OK) {
            cropFullSizeImageEvent.call();
        }
    }

    public SingleLiveEvent<Void> cropFullSizeImageEvent() {
        return cropFullSizeImageEvent;
    }

    public void setCroppedImageResult(int cropImageResult, Uri croppedImageUri) {

        if (cropImageResult == RESULT_OK && croppedImageUri != null)
            processCroppedImageIntoImageFiles(croppedImageUri);
    }

    private void processCroppedImageIntoImageFiles(Uri croppedImageUri) {

        try {

            Bitmap croppedImageBitmap = MediaStore.Images.Media.getBitmap(
                    appContext.getContentResolver(), croppedImageUri);

            Log.d(TAG, "tkm - processCroppedImage: path is: " +
                    BitmapUtils.getAbsolutePathFromUri(appContext, croppedImageUri));

            createImageFilesFromCroppedBitMap(croppedImageBitmap);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void createImageFilesFromCroppedBitMap(Bitmap croppedImageBitmap) {

        Bitmap smallBitmap = createScaledBitmap(croppedImageBitmap, 75, true);
        Bitmap mediumBitmap = createScaledBitmap(croppedImageBitmap, 290, true);
        Bitmap largeBitMap = createScaledBitmap(croppedImageBitmap, 400, true);

        if (smallImageFile != null) {

            boolean smallImageFileSaved = saveBitmapToCache(smallBitmap, smallImageFile);

            if (smallImageFileSaved)
            updatedImageModel.setLocalSmallImageUri(FileProvider.getUriForFile(
                            appContext, FILE_PROVIDER_AUTHORITY,smallImageFile).toString());
        }

        if (mediumImageFile != null) {

            boolean mediumImageSaved = saveBitmapToCache(mediumBitmap, mediumImageFile);

            if (mediumImageSaved)
                updatedImageModel.setLocalMediumImageUri(FileProvider.getUriForFile(
                                appContext, FILE_PROVIDER_AUTHORITY, mediumImageFile).toString());
        }

        if (largeImageFile != null) {

            boolean largeImageSaved = saveBitmapToCache(largeBitMap, largeImageFile);

            if (largeImageSaved)
                updatedImageModel.setLocalLargeImageUri(FileProvider.getUriForFile(
                                appContext, FILE_PROVIDER_AUTHORITY, largeImageFile).toString());
        }

        // ToDo - set the new image to the display
    }

    public SingleLiveEvent<String> deleteFullSizeImageEvent() {
        return deleteFullSizeImage;
    }

    public void launchBrowser() {
        launchBrowserEvent.call();
    }

    public SingleLiveEvent<Void> launchBrowserEvent() {
        return launchBrowserEvent;
    }
}