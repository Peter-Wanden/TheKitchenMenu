package com.example.peter.thekitchenmenu.ui.imageeditor;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.databinding.ImageEditorBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import static com.example.peter.thekitchenmenu.ui.imageeditor.ImageEditorViewModel.*;

public class ImageEditorFragment extends Fragment {

    private static final String TAG = "ImageEditorFragment";

    private ImageEditorBinding imageEditorBinding;
    private ImageEditorViewModel viewModel;

    public static ImageEditorFragment newInstance() {
        return new ImageEditorFragment();
    }

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
        viewModel = new ViewModelProvider(requireActivity()).get(ImageEditorViewModel.class);
    }

    private void setObservers() {
        final Observer<ImageModel> imageModelObserver = imageModel ->
                imageEditorBinding.setImageModel(imageModel);

        viewModel.getExistingImageModel().observe(getViewLifecycleOwner(), imageModelObserver);
    }

    private void setBindingInstanceVariables() {
        imageEditorBinding.setImageViewModel(viewModel);
        imageEditorBinding.setImageModel(viewModel.getUpdatedImageModel());
    }

    private void subscribeToEvents() {
        viewModel.getImageFromCameraEvent().observe(getViewLifecycleOwner(), this::getImageFromCamera);
        viewModel.getImageFromGalleryEvent().observe(getViewLifecycleOwner(), event -> getImageFromGallery());
        viewModel.launchBrowserEvent().observe(getViewLifecycleOwner(), event -> launchBrowser());
        viewModel.cropFullSizeImageEvent().observe(getViewLifecycleOwner(), event -> cropImage());
    }

    private void getImageFromCamera(Uri fullSizeImageUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fullSizeImageUri);

        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void getImageFromGallery() {
        // see https://codelabs.developers.google.com/codelabs/android-storage-permissions/#4
        Intent imagePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imagePickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerIntent.setType("image/*");

        startActivityForResult(imagePickerIntent, REQUEST_IMAGE_IMPORT);
    }

    private void launchBrowser() {
        // TODO - launch browser with (if available), any search term added
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        String query = "";
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }

    private void cropImage() {
        CropImage.activity(
                Uri.fromFile(viewModel.getFullSizeImageFile())).
                setActivityTitle(requireActivity().
                        getString(R.string.activity_title_image_cropper)).
                setAspectRatio(1, 1).
                start(requireContext(), this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onConfigurationChange();
    }
}
