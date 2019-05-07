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
import com.example.peter.thekitchenmenu.databinding.ImageEditorBinding;
import com.example.peter.thekitchenmenu.utils.BitmapUtils;
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
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ImageEditor extends Fragment {

    private static final String TAG = "ImageEditor";

    private ImageEditorBinding imageEditorBinding;
    private ImageEditorViewModel imageEditorViewModel;
    private File imageFile = null;
    private Uri imageFileUri = null;

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
        imageEditorBinding.setImageModel(imageEditorViewModel.getImageModel());
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

    private void checkHardware() {

        imageEditorViewModel.setDeviceHasCamera(
                requireActivity().getPackageManager().
                        hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
    }

    // TODO - Move all camera code to another (utils) class
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
        // Todo, if the imageFile is !=null, delete the file, then recreate it.
        imageFile = null;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {

            if (createImageFile()) {

                imageEditorViewModel.setTemporaryImagePath(imageFile.getAbsolutePath());

                imageFileUri = FileProvider.getUriForFile(requireActivity(),
                        Constants.FILE_PROVIDER_AUTHORITY, imageFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

                startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private boolean createImageFile() {

        try {

            imageFile = BitmapUtils.createImageFile(requireActivity());
            return true;

        } catch (IOException ex) {

            ex.printStackTrace();
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK)

            cropImage();
//            processAndSetImage();
//            performCrop();

        else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_CANCELED)

            BitmapUtils.deleteImageFile(requireActivity(),
                    imageEditorViewModel.getTemporaryImagePath());

        else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_OK)

            processAndSetImage(imageFileUri);

        else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_CANCELED)

            Log.e(TAG, "tkm - Media store intent cancelled");

        else if (requestCode == Constants.REQUEST_IMAGE_PICKER &&
                resultCode == RESULT_OK) {

            imageFileUri = data.getData();

            if (imageFile != null) {

                imageEditorViewModel.getImageModel().setLocalImageUri(imageFileUri.toString());
                imageEditorViewModel.setLastImageUpdated(LastImageUpdated.LOCAL_IMAGE);
            }

            Log.d(TAG, "tkm - onActivityResult: image uri is null");

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER &&
                resultCode == RESULT_CANCELED)

            Log.e(TAG, "tkm - Image picker intent cancelled");

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                processAndSetImage(resultUri);

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

        CropImage.activity(imageFileUri).
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

    private void processAndSetImage(Uri resultUri) {


//        Bitmap bitmap = BitmapFactory.decodeFile();
//        Bitmap scaledBitmap = createScaledBitmap(bitmap, 400, true);

//        Log.d(TAG, "processAndSetImage: width is:" + bitmap.getWidth()
//        + " height is: " + bitmap.getHeight());

        // TODO - DON'T FORGET TO DELETE OLD BITMAPS AND URI'S

        Glide.with(this).load(resultUri).into(imageEditorBinding.productImage);

//        BitmapUtils.resampleImage(
//                requireActivity(),
//                null,
//                imageEditorViewModel.getTemporaryImagePath());

//        imageEditorViewModel.
//                getImageModel().
//                setLocalImageUri(imageEditorViewModel.getTemporaryImagePath());
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
}
