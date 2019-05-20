package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.ProductUserDataModel;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductUserDataEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductUserDataEditorVi";

    private ProductUserDataModel userDataModel = new ProductUserDataModel();
    private ProductUserDataTextValidationHandler textValidationHandler;

    private MutableLiveData<Boolean> userDataModelIsValidated = new MutableLiveData<>(false);
    private boolean retailerValidated = false;
    private boolean locationRoomValidated = false;
    private boolean locationInRoomValidated = false;

    public ProductUserDataEditorViewModel(@NonNull Application application) {
        super(application);

        textValidationHandler = new ProductUserDataTextValidationHandler(
                application,
                this);
    }

    ProductUserDataModel getUserDataModel() {
        return userDataModel;
    }

    ProductUserDataTextValidationHandler getTextValidationHandler() {
        return textValidationHandler;
    }

    void setRetailerValidated(boolean retailerValidated) {
        this.retailerValidated = retailerValidated;
        checkUserDataModelValidated();
    }

    void setLocationRoomValidated(boolean locationRoomValidated) {
        this.locationRoomValidated = locationRoomValidated;
        checkUserDataModelValidated();
    }

    void setLocationInRoomValidated(boolean locationInRoomValidated) {
        this.locationInRoomValidated = locationInRoomValidated;
        checkUserDataModelValidated();
    }

    private void checkUserDataModelValidated() {
        if(retailerValidated && locationRoomValidated && locationInRoomValidated)
            getUserDataModelIsValidated().setValue(Boolean.TRUE);
    }

    private MutableLiveData<Boolean> getUserDataModelIsValidated() {
        return userDataModelIsValidated;
    }
}
