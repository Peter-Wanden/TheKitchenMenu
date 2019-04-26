package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.data.model.ProductImageModel;

public class ImageEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ImageEditorViewModel";

    private ProductImageModel imageModel;

    public ImageEditorViewModel(@NonNull Application application) {
        super(application);

        imageModel = new ProductImageModel();

    }

    public ProductImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ProductImageModel imageModel) {
        this.imageModel = imageModel;
    }
}
