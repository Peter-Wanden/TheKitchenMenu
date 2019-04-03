package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.utils.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.ProductTextValidationHandler;

import androidx.annotation.NonNull;

public class ProductUserDataEditorViewModel extends ObservableViewModel {

    private ProductTextValidationHandler textValidationHandler;

    public ProductUserDataEditorViewModel(@NonNull Application application) {
        super(application);

    }
}
