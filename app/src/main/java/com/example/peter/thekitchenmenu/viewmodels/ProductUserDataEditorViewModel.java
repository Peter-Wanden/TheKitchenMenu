package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.ProductUserDataModel;
import com.example.peter.thekitchenmenu.utils.ProductUserDataTextValidationHandler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductUserDataEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductUserDataEditorVi";

    private ProductUserDataModel userDataModel = new ProductUserDataModel();

    private ProductUserDataTextValidationHandler textValidationHandler;
    private MutableLiveData<Boolean> allUserDataFieldsValidated = new MutableLiveData<>(false);
    private boolean retailerValidated = false;
    private boolean locationRoomValidated = false;
    private boolean locationInRoomValidated = false;

    public ProductUserDataEditorViewModel(@NonNull Application application) {
        super(application);

        textValidationHandler = new ProductUserDataTextValidationHandler(
                application,
                this);
    }

    public ProductUserDataModel getUserDataModel() {

        return userDataModel;
    }

    public ProductUserDataTextValidationHandler getTextValidationHandler() {
        return textValidationHandler;
    }

    public void setUserDataModel(ProductUserDataModel userDataModel) {

        this.userDataModel = userDataModel;
    }

    public void setRetailerValidated(boolean retailerValidated) {
        this.retailerValidated = retailerValidated;
    }

    public void setLocationRoomValidated(boolean locationRoomValidated) {
        this.locationRoomValidated = locationRoomValidated;
    }

    public void setLocationInRoomValidated(boolean locationInRoomValidated) {
        this.locationInRoomValidated = locationInRoomValidated;
    }

    public MutableLiveData<Boolean> getAllUserDataFieldsValidated() {
        return allUserDataFieldsValidated;
    }
}
