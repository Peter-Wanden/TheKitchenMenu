package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import com.example.peter.thekitchenmenu.data.model.ProductImageModel;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ImageEditor extends Fragment {

    private static final String TAG = "ImageEditor";

    private ImageEditorBinding imageEditorBinding;
    private ImageEditorViewModel imageEditorViewModel;

    // Todo - move instance vars and logic to ViewModel
    private boolean imageHasBeenModified = false;
    private boolean newImageAvailable;
    private boolean cameraImageTaken;
    private String temporaryImagePath;

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

        return rootView;
    }

    private void setViewModel() {

        imageEditorViewModel = ViewModelProviders.of(requireActivity()).
                get(ImageEditorViewModel.class);
    }

    private void takePictureIntent() {

        // https://developer.android.com/training/camera/photobasics
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {

            File photoFile = null;

            try {

                photoFile = BitmapUtils.createImageFile(requireActivity());

            } catch (IOException ex) {

                ex.printStackTrace();
            }

            if (photoFile != null) {

                temporaryImagePath = photoFile.getAbsolutePath();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {

            processAndSetImage();
            newImageAvailable = true;
            cameraImageTaken = true;

        } else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_CANCELED) {

            BitmapUtils.deleteImageFile(requireActivity(), temporaryImagePath);
            cameraImageTaken = false;

        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_OK) {

            newImageAvailable = true;
            processAndSetImage();

        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_CANCELED) {

            Log.e(TAG, "Media store intent cancelled");

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER &&
                resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            newImageAvailable = true;

            imageEditorBinding.productImage.setImageURI(selectedImageUri);

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER &&
                resultCode == RESULT_CANCELED) {

            Log.e(TAG, "Image picker intent cancelled");
        }
    }

    // TODO - Picasso?
    private void processAndSetImage() {

        Bitmap mResultsBitmap = BitmapUtils.resampleImage(
                requireActivity(), null, temporaryImagePath);

        imageEditorBinding.productImage.setImageBitmap(mResultsBitmap);
    }

    private void setImage() {

        Glide.with(this)
                .load(temporaryImagePath)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(imageEditorBinding.productImage);
    }

    public void requestPermissions() {

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_STORAGE_PERMISSION);

        } else {

            takePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case Constants.REQUEST_STORAGE_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    takePictureIntent();

                } else {

                    Toast.makeText(requireActivity(), R.string.storage_permission_denied,
                            Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            }
        }
    }

    private void rotateImage() {

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap productImage = ((BitmapDrawable) imageEditorBinding.
                productImage.
                getDrawable()).
                getBitmap();

        Bitmap rotated = Bitmap.createBitmap(productImage, 0, 0,
                productImage.getWidth(), productImage.getHeight(), matrix, true);

        imageEditorBinding.productImage.setImageBitmap(rotated);

        imageHasBeenModified = true;
    }
}
