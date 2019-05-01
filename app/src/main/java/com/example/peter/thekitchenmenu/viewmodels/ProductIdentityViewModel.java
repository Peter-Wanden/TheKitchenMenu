package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.utils.ProductIdentityTextValidationHandler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductIdentityViewModel extends ObservableViewModel {

    private ProductIdentityModel identityModel = new ProductIdentityModel();
    private ProductIdentityTextValidationHandler textValidationHandler;
    private MutableLiveData<Boolean> getIdentityModelIsValid = new MutableLiveData<>(false);

    private boolean descriptionValidated = false;
    private boolean madeByValidated = false;

    public ProductIdentityViewModel(@NonNull Application application) {
        super(application);

        textValidationHandler = new ProductIdentityTextValidationHandler(
                application,
                this);
    }

    public ProductIdentityModel getIdentityModel() {

        return identityModel;
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

    public MutableLiveData<Boolean> getGetIdentityModelIsValid() {
        return getIdentityModelIsValid;
    }
}
