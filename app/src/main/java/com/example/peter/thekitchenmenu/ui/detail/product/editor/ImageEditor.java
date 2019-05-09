package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.Manifest;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.databinding.ImageEditorBinding;
import com.example.peter.thekitchenmenu.viewmodels.ImageEditorViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.IOException;

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

    private Uri croppedImageResultUri = null;
    private Bitmap croppedImageBitmap = null;

    private boolean cameraImageTaken = false;
    private File    cameraImageFile = null;
    private String  cameraImageFilePath = null;
    private Uri     cameraImageFileUri = null;

    private File smallImageFile = null;
    private File mediumImageFile = null;
    private File largeImageFile = null;

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
        setBindingInstanceVariables();
        subscribeToEvents();
        checkHardware();

        return rootView;
    }

    private void setViewModel() {
        imageEditorViewModel = ViewModelProviders.of(requireActivity()).
                get(ImageEditorViewModel.class);
    }

    private void setBindingInstanceVariables() {

        imageEditorBinding.setImageViewModel(imageEditorViewModel);
        imageEditorBinding.setImageModel(imageEditorViewModel.getNewImageModel());
        imageEditorBinding.setImageHandler(imageEditorViewModel.getImageEditorHandler());
    }

    private void subscribeToEvents() {

        imageEditorViewModel.getLaunchGalleryEvent().observe(
                this, event -> getImageFromGallery());

        imageEditorViewModel.getLaunchCameraEvent().observe(
                this, event -> requestPermissions());

        imageEditorViewModel.getLaunchBrowserEvent().observe(
                this, event -> launchBrowser());
    }

    // Todo - implement
    private void checkHardware() {
        imageEditorViewModel.setDeviceHasCamera(requireActivity().getPackageManager().
                        hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
    }

    private void getImageFromCamera() {

        // TODO - implement for no camera (already done in manifest!!)
        // If your application uses, but does not require a camera in order to function, instead
        // set android:required to false. In doing so, Google Play will allow devices without a
        // camera to download your application. It's then your responsibility to check for the
        // availability of the camera at runtime by calling
        // hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY). If a camera is not available,
        // you should then disable your camera features.

        // https://developer.android.com/training/camera/photobasics - Using camera app
        // https://developer.android.com/training/camera/cameradirect.html - Controlling the camera
        // Todo, if the localTempLargeImage is !=null, delete the file, then recreate it.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {

            try {

                cameraImageFile = createTempImageFile(requireContext());

            } catch (IOException e) {

                e.printStackTrace();
            }

            if (cameraImageFile != null) {

                cameraImageFilePath = cameraImageFile.getAbsolutePath();
                cameraImageFileUri = FileProvider.getUriForFile(
                        requireContext(),
                        FILE_PROVIDER_AUTHORITY,
                        cameraImageFile);

                cameraImageTaken = true;

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageFileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void getImageFromGallery() {

        CropImage.activity().
                setAspectRatio(1,1).
                start(requireActivity(), this);
    }

    private void cropCameraImage() {

        CropImage.activity(cameraImageFileUri).
                setAspectRatio(1,1).
                start(requireContext(), this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
            cropCameraImage();

        else if (requestCode == REQUEST_IMAGE_CAPTURE &&resultCode == RESULT_CANCELED)
            deleteImageFile(requireActivity(), cameraImageFilePath);

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                croppedImageResultUri = result.getUri();
//                deleteCameraImage();
                processCroppedBitMap();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Log.e(TAG, "tkm - onActivityResult: ", error);
//                deleteCameraImage();
            }
        }
    }

    private void processCroppedBitMap() {

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

            smallImageFile = createTempImageFile(requireContext());
            mediumImageFile = createTempImageFile(requireContext());
            largeImageFile = createTempImageFile(requireContext());

            createScaledBitmaps();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createScaledBitmaps() {

        Bitmap smallBitmap = createScaledBitmap(croppedImageBitmap, 75, true);
        Bitmap mediumBitmap = createScaledBitmap(croppedImageBitmap, 290, true);
        Bitmap largeBitMap = createScaledBitmap(croppedImageBitmap, 400, true);

        String smallImageFileUri = saveBitmapToFileLocation(smallBitmap, smallImageFile);
        String mediumImageFileUri = saveBitmapToFileLocation(mediumBitmap, mediumImageFile);
        String largeImageFileUri = saveBitmapToFileLocation(largeBitMap, largeImageFile);

        imageEditorViewModel.setLocalImageUris(
                smallImageFileUri,
                mediumImageFileUri,
                largeImageFileUri);

        Log.d(TAG, "tkm - createScaledBitmaps:" +
                " small uri: " + smallImageFileUri +
                " med uri: " + mediumImageFileUri +
                " large uri: " + largeImageFileUri);

        processAndSetImage(mediumImageFileUri);

        // TODO - Set callbacks for bitmap processing before deleting any images
//        if (croppedImageBitmap != null) croppedImageBitmap = null;
//        deleteImageFile(requireActivity(), croppedImageResultUri.toString());
//        delete camera image
//        When first instantiated check for old files!!!
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

    private void requestPermissions() {

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.REQUEST_STORAGE_PERMISSION);

        else getImageFromCamera();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case Constants.REQUEST_STORAGE_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                    getImageFromCamera();

                else Toast.makeText(requireActivity(), R.string.storage_permission_denied,
                        Toast.LENGTH_SHORT)
                        .show();
                break;
            }
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
