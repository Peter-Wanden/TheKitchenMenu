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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.databinding.ImageEditorBinding;
import com.example.peter.thekitchenmenu.utils.imageeditor.BitmapUtils;
import com.example.peter.thekitchenmenu.utils.imageeditor.LastImageUpdated;
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

public class ImageEditor extends Fragment {

    private static final String TAG = "ImageEditor";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.peter.thekitchenmenu.fileprovider";

    private ImageEditorBinding imageEditorBinding;
    private ImageEditorViewModel imageEditorViewModel;

    private File tempOriginalFile = null;
    private File tempLargeImage = null;
    private File tempMediumImage = null;
    private File tempSmallImage = null;
    private String tempOriginalImagePath = null;
    private Uri tempOriginalImageUri = null;
    private String tempLargeFileUri = null;
    private String tempMediumFileUri = null;
    private String tempSmallFileUri = null;

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

        Observer<ImageModel> imageModelObserver =
                newImageModel -> imageEditorViewModel.setNewImageModel(newImageModel);

        imageEditorViewModel.getImageModel().observe(this, imageModelObserver);
    }

    private void setBindingInstanceVariables() {

        imageEditorBinding.setImageViewModel(imageEditorViewModel);
        imageEditorBinding.setImageModel(imageEditorViewModel.getNewImageModel());
        imageEditorBinding.setImageHandler(imageEditorViewModel.getImageEditorHandler());
    }

    private void subscribeToEvents() {

        imageEditorViewModel.getLaunchGalleryEvent().observe(
                this, event -> launchGallery());

        imageEditorViewModel.getLaunchCameraEvent().observe(
                this, event -> requestPermissions());

        imageEditorViewModel.getRotateImageEvent().observe(
                this, event -> rotateImage(imageEditorBinding.productImage));

        imageEditorViewModel.getLaunchBrowserEvent().observe(
                this, event -> launchBrowser());
    }

    // Todo - implement
    private void checkHardware() {

        imageEditorViewModel.setDeviceHasCamera(requireActivity().getPackageManager().
                        hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
    }

    private void launchCamera() {

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

                tempOriginalFile = BitmapUtils.createTempImageFile(requireContext());

            } catch (IOException e) {

                e.printStackTrace();
            }

            if (tempOriginalFile != null) {

                tempOriginalImagePath = tempOriginalFile.getAbsolutePath();

                tempOriginalImageUri = FileProvider.getUriForFile(
                        requireContext(),
                        FILE_PROVIDER_AUTHORITY,
                        tempOriginalFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempOriginalImageUri);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void createImageFiles() {

        try {

            // TODO - Create one file, resample, save, then create the next one

            tempSmallImage = BitmapUtils.createTempImageFile(requireActivity());
            tempMediumImage = BitmapUtils.createTempImageFile(requireActivity());
            tempLargeImage = BitmapUtils.createTempImageFile(requireActivity());

            getUris();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUris() {

        tempSmallFileUri = tempSmallImage.getAbsolutePath();
        tempMediumFileUri = tempMediumImage.getAbsolutePath();
        tempLargeFileUri = tempLargeImage.getAbsolutePath();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK)

            cropImage();

        else if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_CANCELED)

            BitmapUtils.deleteImageFile(requireActivity(), tempOriginalImagePath);

        else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_OK) {

//            processAndSetImage(tempLargeFileUri);
        }


        else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_CANCELED)

            Log.e(TAG, "tkm - Media store intent cancelled");

        else if (requestCode == Constants.REQUEST_IMAGE_PICKER &&
                resultCode == RESULT_OK) {

//            tempLargeFileUri = data.getData();

//            if (localTempLargeImage != null) {
//
//                imageEditorViewModel.getImageModel().setLocalImageUri(tempLargeFileUri.toString());
//                imageEditorViewModel.setLastImageUpdated(LastImageUpdated.LOCAL_IMAGE);
//            }

            Log.d(TAG, "tkm - onActivityResult: image uri is null");

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER &&
                resultCode == RESULT_CANCELED)

            Log.e(TAG, "tkm - Image picker intent cancelled");

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                tempOriginalImageUri = result.getUri();
                processAndSetImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Log.e(TAG, "onActivityResult: ", error);
            }
        }
    }

    private void launchGallery() {

        CropImage.activity().
                setAspectRatio(1,1).
                start(requireActivity(), this);
    }

    private void cropImage() {

        CropImage.activity(tempOriginalImageUri).
                setAspectRatio(1,1).
                start(requireContext(), this);
    }
    // TODO - Resampling only required if storing on servers, otherwise just store local Uri
    // todo - If storing locally, store image to local media and delete temp file
    // I think Glide re-samples / down-samples images before loading into image views.
    // For non-web (local and server images) do not down-sample, just take the bitmap image and
    // store it as it is.

    private void createScaledBitmaps() {

        // Create a new file
        // pass in the



    }

    public static Bitmap createScaledBitmap(Bitmap image, float imageSize, boolean filter) {

        float ratio = Math.min(imageSize / image.getWidth(), imageSize / image.getHeight());
        int width = Math.round(ratio * image.getWidth());
        int height = Math.round(ratio * image.getHeight());

        return Bitmap.createScaledBitmap(image, width, height, filter);
    }

    private void processAndSetImage() {



//        Bitmap bitmap = BitmapFactory.decodeFile();
//        Bitmap scaledBitmap = createScaledBitmap(bitmap, 400, true);

//        Log.d(TAG, "processAndSetImage: width is:" + bitmap.getWidth()
//        + " height is: " + bitmap.getHeight());

        // TODO - DON'T FORGET TO DELETE OLD BITMAPS AND URI'S

        Glide.with(this).load(tempOriginalImageUri).into(imageEditorBinding.productImage);

//        BitmapUtils.resampleImage(
//                requireActivity(),
//                null,
//                imageEditorViewModel.getTempSmallImagePath());

//        imageEditorViewModel.
//                getImageModel().
//                setLocalLargeImageUri(imageEditorViewModel.getTempSmallImagePath());
    }

    private void requestPermissions() {

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.REQUEST_STORAGE_PERMISSION);

        else launchCamera();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case Constants.REQUEST_STORAGE_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                    launchCamera();

                else Toast.makeText(requireActivity(), R.string.storage_permission_denied,
                        Toast.LENGTH_SHORT)
                        .show();
                break;
            }
        }
    }

    private void rotateImage(ImageView productImage) {

        BitmapUtils.rotateImage(productImage);
        imageEditorViewModel.setNewImageDataAvailable(true);
    }

    private void launchBrowser() {
        // TODO - launch browser with (if available), any search term added
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        String query = "";
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO - delete the large original file if it exist
        
    }
}
