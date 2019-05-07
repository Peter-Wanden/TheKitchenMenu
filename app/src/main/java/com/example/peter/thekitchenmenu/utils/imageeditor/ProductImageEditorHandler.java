package com.example.peter.thekitchenmenu.utils.imageeditor;

import com.example.peter.thekitchenmenu.viewmodels.ImageEditorViewModel;

public class ProductImageEditorHandler implements ImageEditorHandler {

    private ImageEditorViewModel viewModel;
    private LastImageUpdated lastImageUpdated = LastImageUpdated.NO_IMAGE;

    public void setViewModel(ImageEditorViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    public LastImageUpdated getLastImageUpdated() {
        return lastImageUpdated;
    }

    @Override
    public void setLastImageUpdated(LastImageUpdated lastImageUpdated) {
        this.lastImageUpdated = lastImageUpdated;
    }

}
