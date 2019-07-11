package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;
import android.content.res.Resources;
import android.text.Editable;

import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public class ProductIdentityViewModel extends AndroidViewModel {

    private static final String TAG = "tkm - ProductIdentityVM";

    private Resources resources;

    // Observed by the main ProductEditorViewModel, to save a new model set it here.
    private final MutableLiveData<ProductIdentityModel> existingIdentityModel =
            new MutableLiveData<>();

    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> shoppingListItemName = new ObservableField<>();
    public final ObservableInt category = new ObservableInt();
    public final ObservableInt shelfLife = new ObservableInt();

    private final SingleLiveEvent<String> descriptionErrorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> shoppingListItemNameErrorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> identityModelValidEvent = new SingleLiveEvent<>();

    private boolean descriptionValidated = false;
    private boolean shoppingItemNameValidated = false;

    public ProductIdentityViewModel(@NonNull Application application) {
        super(application);
        resources = application.getResources();

        description.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                descriptionChanged();
            }
        });

        shoppingListItemName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                shoppingListItemNameChanged();
            }
        });

    }

    MutableLiveData<ProductIdentityModel> getExistingIdentityModel() {
        return existingIdentityModel;
    }

    void setIdentityModel(ProductIdentityModel model) {
        description.set(model.getDescription());
        shoppingListItemName.set(model.getShoppingListItemName());
        category.set(model.getCategory());
        shelfLife.set(model.getShelfLife());
    }

    private void descriptionChanged() {
        String descriptionValidationResponse = validateText(description.get());

        if (descriptionValidationResponse.equals(TextValidationHandler.VALIDATED)) {
            descriptionValidated = true;
        } else {
            descriptionValidated = false;
            descriptionErrorEvent.setValue(descriptionValidationResponse);
        }
        checkAllFieldsValidated();
    }

    SingleLiveEvent<String> getDescriptionErrorEvent() {
        return descriptionErrorEvent;
    }

    private void shoppingListItemNameChanged() {
        String shoppingListItemNameValidationResponse = validateText(shoppingListItemName.get());

        if (shoppingListItemNameValidationResponse.equals(TextValidationHandler.VALIDATED)) {
            shoppingItemNameValidated = true;
        } else {
            shoppingItemNameValidated = false;
            shoppingListItemNameErrorEvent.setValue(shoppingListItemNameValidationResponse);
        }
        checkAllFieldsValidated();
    }

    SingleLiveEvent<String> getShoppingListItemNameErrorEvent() {
        return shoppingListItemNameErrorEvent;
    }

    SingleLiveEvent<Boolean> getIdentityModelValidEvent() {
        return identityModelValidEvent;
    }

    private String validateText(String textToValidate) {
        return TextValidationHandler.validateShortText(resources, textToValidate);
    }

    private void checkAllFieldsValidated() {
        if (descriptionValidated && shoppingItemNameValidated) {

            ProductIdentityModel model = new ProductIdentityModel(
                    description.get(),
                    shoppingListItemName.get(),
                    category.get(),
                    shelfLife.get());

            existingIdentityModel.setValue(model);
            identityModelValidEvent.setValue(true);
        } else
            identityModelValidEvent.setValue(false);
    }
}