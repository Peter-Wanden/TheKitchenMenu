package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.databinding.ImageEditorBinding;
import com.example.peter.thekitchenmenu.utils.imageeditor.ImageEditorViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.peter.thekitchenmenu.app.Constants.FILE_PROVIDER_AUTHORITY;
import static com.example.peter.thekitchenmenu.utils.imageeditor.BitmapUtils.*;
import static com.example.peter.thekitchenmenu.utils.imageeditor.ImageEditorViewModel.*;

public class ImageEditorFragment extends Fragment {

    private static final String TAG = "ImageEditorFragment";

    private ImageEditorBinding imageEditorBinding;
    private ImageEditorViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        imageEditorBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.image_editor,
                container,
                false);

        View rootView = imageEditorBinding.getRoot();
        imageEditorBinding.setLifecycleOwner(this);

        setViewModel();
        setObservers();
        setBindingInstanceVariables();
        subscribeToEvents();

        return rootView;
    }

    private void setViewModel() {

        viewModel = ViewModelProviders.of(requireActivity()).
                get(ImageEditorViewModel.class);
    }

    private void setObservers() {

        final Observer<ImageModel> imageModelObserver = imageModel -> {
            imageEditorBinding.setImageModel(imageModel);
        };

        viewModel.getExistingImageModel().observe(this, imageModelObserver);
    }

    private void setBindingInstanceVariables() {

        imageEditorBinding.setImageViewModel(viewModel);
        imageEditorBinding.setImageModel(viewModel.getUpdatedImageModel());
    }

    private void subscribeToEvents() {

        viewModel.getImageFromCameraEvent().observe(this, event -> getImageFromCamera());
        viewModel.getImageFromGalleryEvent().observe(this, event -> getImageFromGallery());
        viewModel.launchBrowserEvent().observe(this, event -> launchBrowser());
        viewModel.cropFullSizeImageEvent().observe(this, event -> cropImage());
    }

    private void getImageFromCamera() {

        if (viewModel.getFullSizeImageFile() != null) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(
                            requireActivity(),
                            FILE_PROVIDER_AUTHORITY,
                            viewModel.getFullSizeImageFile()));

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void getImageFromGallery() {

        // TODO - see https://codelabs.developers.google.com/codelabs/android-storage-permissions/#4
        Intent imagePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imagePickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerIntent.setType("image/*");

        startActivityForResult(imagePickerIntent, REQUEST_IMAGE_IMPORT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            viewModel.cameraImageResult(RESULT_OK);

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            viewModel.cameraImageResult(RESULT_CANCELED);

        } else if (requestCode == REQUEST_IMAGE_IMPORT && resultCode == RESULT_OK && data != null) {
            viewModel.imageImportResult(RESULT_OK, data.getData());

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                viewModel.setCroppedImageResult(resultCode, result.getUri());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, "tkm - onActivityResult: ", error);
            }
        }
    }

    private void cropImage() {

        CropImage.activity(
                Uri.fromFile(viewModel.getFullSizeImageFile())).
                setActivityTitle(requireActivity().
                        getString(R.string.activity_title_image_cropper)).
                setAspectRatio(1, 1).
                start(requireContext(), this);
    }

    private void launchBrowser() {
        // TODO - launch browser with (if available), any search term added
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        String query = "";
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }

    private void deleteFullSizeImage(String fullSizeImagePath) {
        deleteImageFile(requireActivity(), fullSizeImagePath);
    }
}
