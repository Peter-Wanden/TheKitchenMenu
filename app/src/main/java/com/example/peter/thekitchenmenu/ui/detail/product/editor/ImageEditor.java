package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.databinding.ImageEditorBinding;
import com.example.peter.thekitchenmenu.viewmodels.ImageEditorViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.peter.thekitchenmenu.utils.imageeditor.BitmapUtils.*;

public class ImageEditor extends Fragment {

    private static final String TAG = "ImageEditor";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String FILE_PROVIDER_AUTHORITY =
            "com.example.peter.thekitchenmenu.fileprovider";

    private ImageEditorBinding imageEditorBinding;
    private ImageEditorViewModel imageEditorViewModel;

    private Bitmap croppedImageBitmap = null;

    private boolean cameraImageTaken = false;
    private String cameraImageFilePath = null;
    private Uri cameraImageFileUri = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        imageEditorBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.image_editor, container,
                false);

        View rootView = imageEditorBinding.getRoot();
        imageEditorBinding.setLifecycleOwner(this);

        setViewModel();
        setObservers();
        setBindingInstanceVariables();
        subscribeToEvents();
        checkHardware();

        return rootView;
    }

    private void setViewModel() {
        imageEditorViewModel = ViewModelProviders.of(requireActivity()).
                get(ImageEditorViewModel.class);
    }

    private void setObservers() {

        final Observer<ImageModel> imageModelObserver = imageModel -> {
            imageEditorBinding.setImageModel(imageModel);
        };

        imageEditorViewModel.getImageModel().observe(this, imageModelObserver);
    }

    private void setBindingInstanceVariables() {

        imageEditorBinding.setImageViewModel(imageEditorViewModel);
        imageEditorBinding.setImageModel(imageEditorViewModel.getNewImageModel());
    }

    private void subscribeToEvents() {

        imageEditorViewModel.getLaunchGalleryEvent().observe(
                this, event -> getImageFromGallery());

        imageEditorViewModel.getLaunchCameraEvent().observe(
                this, event -> getImageFromCamera());

        imageEditorViewModel.getLaunchBrowserEvent().observe(
                this, event -> launchBrowser());
    }

    private void checkHardware() {

        imageEditorViewModel.setDeviceHasCamera(requireActivity().getPackageManager().
                        hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
    }

    private void getImageFromCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {

            File cameraImageFile = null;

            try {

                cameraImageFile = createTempImageFile(requireContext());

            } catch (IOException e) {

                e.printStackTrace();
            }

            if (cameraImageFile != null) {

                cameraImageFileUri = FileProvider.getUriForFile(
                        requireActivity(),
                        FILE_PROVIDER_AUTHORITY,
                        cameraImageFile);

                cameraImageFilePath = cameraImageFile.getAbsolutePath();
                cameraImageTaken = true;

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageFileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void getImageFromGallery() {

        CropImage.activity().setActivityTitle("TKM - Image Cropper").
                setAspectRatio(1,1).
                start(requireActivity(), this);
    }

    private void cropCameraImage() {

        CropImage.activity(cameraImageFileUri).setActivityTitle("TKM - Image Cropper").
                setAspectRatio(1,1).
                start(requireContext(), this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            cropCameraImage();
        }

        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            deleteImageFile(requireActivity(), cameraImageFilePath);
        }

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri croppedImageResultUri = result.getUri();
                processCroppedBitMap(croppedImageResultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Log.e(TAG, "tkm - onActivityResult: ", error);
            }
        }
    }

    private void processCroppedBitMap(Uri croppedImageResultUri) {

        try {

            croppedImageBitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().getContentResolver(),
                    croppedImageResultUri);

            createImageFiles();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void createImageFiles() {

        try {

            File smallImageFile = createTempImageFile(requireContext());
            File mediumImageFile = createTempImageFile(requireContext());
            File largeImageFile = createTempImageFile(requireContext());

            createScaledBitmaps(
                    smallImageFile,
                    mediumImageFile,
                    largeImageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createScaledBitmaps(File smallImageFile,
                                     File mediumImageFile,
                                     File largeImageFile) {

        Bitmap smallBitmap = createScaledBitmap(croppedImageBitmap, 75, true);
        Bitmap mediumBitmap = createScaledBitmap(croppedImageBitmap, 290, true);
        Bitmap largeBitMap = createScaledBitmap(croppedImageBitmap, 400, true);

        String smallImageFileUri = saveBitmapToCache(smallBitmap, smallImageFile);
        String mediumImageFileUri = saveBitmapToCache(mediumBitmap, mediumImageFile);
        String largeImageFileUri = saveBitmapToCache(largeBitMap, largeImageFile);

        imageEditorViewModel.setLocalImageUris(
                smallImageFileUri,
                mediumImageFileUri,
                largeImageFileUri);

        processAndSetImage(mediumImageFileUri);
    }

    private void deleteTemporaryFiles(Pair[] filesAndDates) {

        long fiveMinutes = 60000 * 5;
        long timeNow = System.currentTimeMillis();

        for (Pair file : filesAndDates) {

            File filename = (File) file.first;
            long lastModified = (long) file.second;
            long fileAge = timeNow - lastModified;

            if (fileAge > fiveMinutes) {

                if (filename !=null) {

                    boolean fileDeleted = filename.delete();

                    if (fileDeleted)
                        Log.d(TAG, "tkm - deleteTemporaryFiles: file deleted: " +
                                filename.getName());
                }
            }
        }

        long daysBeforeDeletionOfTempFiles = TimeUnit.DAYS.toMillis(3);

        long daysBeforeDeletion = 3;
        long threeDaysInMilliseconds = 1000 * 60 * 60 * 24 * daysBeforeDeletion;
        Log.d(TAG, "tkm - deleteTemporaryFiles: S days in milliseconds is: " + daysBeforeDeletionOfTempFiles);
    }

    private Pair[] getTemporaryFiles() {

        // TODO - get files Uri?
        int i = 0;
        File[] cachedFiles = requireActivity().getCacheDir().listFiles();
        Pair[] filesAndDates = new Pair[cachedFiles.length];

        for (File file : cachedFiles) {

            Pair<File, Long> fileWithDate = new Pair<>(file, file.lastModified());
            filesAndDates[i] = fileWithDate;
            i++;
        }

        return filesAndDates;
    }

    private void processAndSetImage(String largeImageFileUri) {

        Glide.with(this).
                load(largeImageFileUri).
                into(imageEditorBinding.productImage);
    }

    private void deleteCameraImage() {

        if (cameraImageTaken) {

            deleteImageFile(requireActivity(), cameraImageFilePath);
            cameraImageTaken = false;
        }
    }

    private void launchBrowser() {
        // TODO - launch browser with (if available), any search term added
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        String query = "";
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }
}
