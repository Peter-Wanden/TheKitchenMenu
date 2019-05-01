package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.databinding.ImageEditorBinding;
import com.example.peter.thekitchenmenu.utils.BitmapUtils;
import com.example.peter.thekitchenmenu.viewmodels.ImageEditorViewModel;

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
        setInstanceVariables();
        subscribeToEvents();

        return rootView;
    }

    private void setViewModel() {

        imageEditorViewModel = ViewModelProviders.of(requireActivity()).
                get(ImageEditorViewModel.class);
    }

    private void setInstanceVariables() {

        imageEditorBinding.setImageViewModel(imageEditorViewModel);
        imageEditorBinding.setImageModel(imageEditorViewModel.getImageModel());
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

    // TODO - Move all camera code to another (utils) class
    private void launchCamera() {

        // https://developer.android.com/training/camera/photobasics - Using camera app
        // https://developer.android.com/training/camera/cameradirect.html - Controlling the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {

            File photoFile = null;

            try {

                photoFile = BitmapUtils.createImageFile(requireActivity());

            } catch (IOException ex) {

                ex.printStackTrace();
            }

            if (photoFile != null) {

                imageEditorViewModel.setTemporaryImagePath(photoFile.getAbsolutePath());

                Uri photoURI = FileProvider.getUriForFile(requireActivity(),
                        Constants.FILE_PROVIDER_AUTHORITY, photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void launchGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        startActivityForResult(Intent.createChooser(
                intent,
                getString(R.string.intent_gallery_picker_title)),
                Constants.REQUEST_IMAGE_PICKER);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK)

            processAndSetImage();

        else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_CANCELED)

            BitmapUtils.deleteImageFile(requireActivity(),
                    imageEditorViewModel.getTemporaryImagePath());

        else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_OK)

            processAndSetImage();

        else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_CANCELED)

            Log.e(TAG, "tkm - Media store intent cancelled");

        else if (requestCode == Constants.REQUEST_IMAGE_PICKER &&
                resultCode == RESULT_OK) {

            Uri uri = data.getData();

            if (uri != null) imageEditorViewModel.getImageModel().setLocalImageUri(uri.toString());

            Log.d(TAG, "tkm - onActivityResult: image uri is null");


        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER &&
                resultCode == RESULT_CANCELED)

            Log.e(TAG, "tkm - Image picker intent cancelled");
    }

    // TODO - Resampling only required if storing on servers, otherwise just store local Uri
    // todo - If storing locally, store image to local media and delete temp file
    // I think Glide re-samples / down-samples images before loading into image views.
    // For non-web (local and server images) do not down-sample, just take the bitmap image and
    // store it as it is.
    private void processAndSetImage() {

        BitmapUtils.resampleImage(
                requireActivity(),
                null,
                imageEditorViewModel.getTemporaryImagePath());

        imageEditorViewModel.
                getImageModel().
                setLocalImageUri(imageEditorViewModel.getTemporaryImagePath());
    }

    private void requestPermissions() {

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
        imageEditorViewModel.setImageHasChanged(true);
    }

    private void launchBrowser() {
        // TODO - launch browser with (if available), any search term added
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        String query = "";
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }
}
