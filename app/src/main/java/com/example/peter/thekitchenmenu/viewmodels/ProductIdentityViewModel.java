package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.utils.ProductIdentityTextValidationHandler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductIdentityViewModel extends ObservableViewModel {

    private MutableLiveData<ProductIdentityModel> identityModel = new MutableLiveData<>();
    private ProductIdentityTextValidationHandler textValidationHandler;

    // New model created as user types
    private ProductIdentityModel newIdentityModel = new ProductIdentityModel();

    // Tracking of valid fields. When all are true post new model to mutable identityModel
    // TODO CHECK model and newModel are different before updating to prevent loops
    private boolean descriptionValidated = false;
    private boolean madeByValidated = false;

    public ProductIdentityViewModel(@NonNull Application application) {
        super(application);

        textValidationHandler = new ProductIdentityTextValidationHandler(
                application,
                this);
    }

    public MutableLiveData<ProductIdentityModel> getIdentityModel() {
        return identityModel;
    }

    public ProductIdentityModel getNewIdentityModel() {
        return newIdentityModel;
    }

    public void setNewIdentityModel(ProductIdentityModel newIdentityModel) {
        this.newIdentityModel = newIdentityModel;
    }

    public ProductIdentityTextValidationHandler getTextValidationHandler() {

        return textValidationHandler;
    }

    public void setDescriptionValidated(boolean descriptionValidated) {

        this.descriptionValidated = descriptionValidated;
    }

    public void setMadeByValidated(boolean madeByValidated) {

        this.madeByValidated = madeByValidated;
    }
}
