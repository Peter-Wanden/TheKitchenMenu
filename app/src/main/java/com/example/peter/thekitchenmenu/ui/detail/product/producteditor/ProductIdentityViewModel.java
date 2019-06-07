package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;
import android.text.Editable;

import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.VALIDATED;

public class ProductIdentityViewModel extends ObservableViewModel {

    private static final String TAG = "tkm - ProductIdentityVM";

    private MutableLiveData<ProductIdentityModel> existingIdentityModel = new MutableLiveData<>();
    private ProductIdentityModel editedIdentityModel = new ProductIdentityModel();
    private SingleLiveEvent<String> descriptionErrorEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> shoppingListItemNameErrorEvent = new SingleLiveEvent<>();
    private Application appContext;

    // Tracking of valid fields. When all are true post new model to mutable existingIdentityModel
    private boolean descriptionValidated = false;
    private boolean shoppingItemNameValidated = false;

    public ProductIdentityViewModel(@NonNull Application application) {
        super(application);

        appContext = application;
    }

    MutableLiveData<ProductIdentityModel> getExistingIdentityModel() {
        return existingIdentityModel;
    }

    ProductIdentityModel getEditedIdentityModel() {
        return editedIdentityModel;
    }

    void setEditedIdentityModel(ProductIdentityModel editedIdentityModel) {
        this.editedIdentityModel = editedIdentityModel;
    }

    public void descriptionChanged(Editable editedDescription) {
        editedIdentityModel.setDescription(editedDescription.toString());
        String descriptionValidationResponse = validateText(editedDescription);

        if (descriptionValidationResponse.equals(VALIDATED)) {
            descriptionValidated = true;
            checkAllFieldsValidated();
        } else {
            descriptionValidated = false;
            descriptionErrorEvent.setValue(descriptionValidationResponse);
        }
    }

    SingleLiveEvent<String> getDescriptionErrorEvent() {
        return descriptionErrorEvent;
    }

    public void shoppingListItemNameChanged(Editable editedShoppingItem) {
        editedIdentityModel.setShoppingListItemName(editedShoppingItem.toString());
        String shoppingListItemNameValidationResponse = validateText(editedShoppingItem);

        if (shoppingListItemNameValidationResponse.equals(VALIDATED)) {
            shoppingItemNameValidated = true;
            checkAllFieldsValidated();
        } else {
            shoppingItemNameValidated = false;
            shoppingListItemNameErrorEvent.setValue(shoppingListItemNameValidationResponse);
        }
    }

    SingleLiveEvent<String> getShoppingListItemNameErrorEvent() {
        return shoppingListItemNameErrorEvent;
    }

    private String validateText(Editable editable) {
        return TextValidationHandler.validateText(appContext, editable);
    }

    private void checkAllFieldsValidated() {
        if (descriptionValidated && shoppingItemNameValidated) {
            existingIdentityModel.setValue(editedIdentityModel);
        }
    }
}