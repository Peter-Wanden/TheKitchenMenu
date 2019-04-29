package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.model.ProductImageModel;
import com.example.peter.thekitchenmenu.ui.detail.product.editor.ImageEditor;

public class ImageEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ImageEditorViewModel";

    private ImageEditing editor;
    private ProductImageModel imageModel = new ProductImageModel();


    public ImageEditorViewModel(@NonNull Application application) {
        super(application);

        editor = new ImageEditor();
    }

    public void launchCamera() {

        editor.launchGallery();
    }

    public void rotateImage() {

        editor.rotateImage();
    }

    public void launchGallery() {

        Log.d(TAG, "launchGallery: called!");
        editor.launchGallery();
    }

    public ProductImageModel getImageModel() {

        return imageModel;
    }

    public void setImageModel(ProductImageModel imageModel) {

        this.imageModel = imageModel;
    }
}
