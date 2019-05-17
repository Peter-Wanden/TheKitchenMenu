package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.ui.detail.product.editor.ProductIdentityTextValidationHandler;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductIdentityViewModel extends ObservableViewModel {

    private MutableLiveData<ProductIdentityModel> existingIdentityModel = new MutableLiveData<>();
    private ProductIdentityModel updatedIdentityModel = new ProductIdentityModel();
    private ProductIdentityTextValidationHandler textValidationHandler;

    // Tracking of valid fields. When all are true post new model to mutable existingIdentityModel
    // TODO CHECK model and newModel are different before updating to prevent loops
    private boolean descriptionValidated = false;
    private boolean madeByValidated = false;

    public ProductIdentityViewModel(@NonNull Application application) {
        super(application);

        textValidationHandler = new ProductIdentityTextValidationHandler(
                application,
                this);
    }

    MutableLiveData<ProductIdentityModel> getExistingIdentityModel() {
        return existingIdentityModel;
    }

    ProductIdentityModel getUpdatedIdentityModel() {
        return updatedIdentityModel;
    }

    public void setUpdatedIdentityModel(ProductIdentityModel updatedIdentityModel) {
        this.updatedIdentityModel = updatedIdentityModel;
    }

    ProductIdentityTextValidationHandler getTextValidationHandler() {

        return textValidationHandler;
    }

    void setDescriptionValidated(boolean descriptionValidated) {

        this.descriptionValidated = descriptionValidated;
    }

    void setMadeByValidated(boolean madeByValidated) {

        this.madeByValidated = madeByValidated;
    }
}
