package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.ProductUserDataModel;
import com.example.peter.thekitchenmenu.utils.ProductTextValidationHandler;

import androidx.annotation.NonNull;

public class ProductUserDataEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductUserDataEditorVi";

    private ProductUserDataModel userDataModel = new ProductUserDataModel();

    public ProductUserDataEditorViewModel(@NonNull Application application) {
        super(application);
    }

    public ProductUserDataModel getUserDataModel() {

        return userDataModel;
    }

    public void setUserDataModel(ProductUserDataModel userDataModel) {

        this.userDataModel = userDataModel;
    }
}
