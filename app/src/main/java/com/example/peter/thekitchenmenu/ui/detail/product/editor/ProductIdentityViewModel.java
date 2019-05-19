package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductIdentityViewModel extends ObservableViewModel {

    private static final String TAG = "ProductIdentityVM";

    private MutableLiveData<ProductIdentityModel> existingIdentityModel = new MutableLiveData<>();
    private ProductIdentityModel updatedIdentityModel = new ProductIdentityModel();
    private ProductIdentityTextValidationHandler textValidationHandler;

    // Tracking of valid fields. When all are true post new model to mutable existingIdentityModel
    private boolean descriptionValidated = false;
    private boolean shoppingListItemNameValidated = false;

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

    ProductIdentityTextValidationHandler getTextValidationHandler() {
        return textValidationHandler;
    }

    void setDescriptionValidated(boolean descriptionValidated) {
        this.descriptionValidated = descriptionValidated;
        checkAllFieldsValidated();
    }

    void setShoppingListItemNameValidated(boolean shoppingListItemNameValidated) {
        this.shoppingListItemNameValidated = shoppingListItemNameValidated;
        checkAllFieldsValidated();
    }

    private void checkAllFieldsValidated() {
        if (descriptionValidated && shoppingListItemNameValidated) {
            existingIdentityModel.setValue(updatedIdentityModel);
        }
    }
}
