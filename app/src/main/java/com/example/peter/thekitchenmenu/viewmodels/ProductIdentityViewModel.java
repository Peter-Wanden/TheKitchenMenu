package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.utils.ProductTextValidationHandler;

import androidx.annotation.NonNull;

public class ProductIdentityViewModel extends ObservableViewModel {

    private ProductIdentityModel identityModel = new ProductIdentityModel();
    private ProductTextValidationHandler textValidationHandler;

    private boolean descriptionValidated = false;
    private boolean madeByValidated = false;

    public ProductIdentityViewModel(@NonNull Application application) {
        super(application);

        textValidationHandler = new ProductTextValidationHandler(
                application,
                this);
    }

    public ProductIdentityModel getIdentityModel() {

        return identityModel;
    }

    public ProductTextValidationHandler getTextValidationHandler() {

        return textValidationHandler;
    }

    public void setDescriptionValidated(boolean descriptionValidated) {

        this.descriptionValidated = descriptionValidated;
    }

    public void setMadeByValidated(boolean madeByValidated) {

        this.madeByValidated = madeByValidated;
    }

    public boolean allFieldsAreValidated() {

        return descriptionValidated && madeByValidated;
    }
}
