package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.ui.utils.TextValidator;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProductIdentityViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-ProductIdentityVM";

    private Resources resources;
    private TextValidator validationHandler;

    // Observed by the main ProductEditorViewModel, to save a new model set it to
    // existingIdentityModel
    private final MutableLiveData<ProductIdentityModel> existingIdentityModel =
            new MutableLiveData<>();

    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
    public final ObservableField<String> shoppingListItemName = new ObservableField<>();
    public final ObservableField<String> shoppingListItemNameErrorMessage = new ObservableField<>();
    public final ObservableInt category = new ObservableInt();
    public final ObservableInt shelfLife = new ObservableInt();

    private final SingleLiveEvent<Boolean> identityModelValidEvent = new SingleLiveEvent<>();

    private boolean descriptionValidated = false;
    private boolean shoppingItemNameValidated = false;

    public ProductIdentityViewModel(@NonNull Application application,
                                    TextValidator validationHandler) {
        super(application);
        resources = application.getResources();
        this.validationHandler = validationHandler;

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

        Log.d(TAG, "ProductIdentityViewModel: recreated");
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
        descriptionErrorMessage.set(null);

        TextValidator.Response response = validateText(description.get());

        if (response.getResult() == TextValidator.Result.VALID) {
            descriptionValidated = true;
        } else {
            descriptionValidated = false;
            setError(descriptionErrorMessage, response);
        }
        checkAllFieldsValidated();
    }

    private void shoppingListItemNameChanged() {
        shoppingListItemNameErrorMessage.set(null);

        TextValidator.Response response = validateText(shoppingListItemName.get());

        if (response.getResult() == TextValidator.Result.VALID) {
            shoppingItemNameValidated = true;
        } else {
            shoppingItemNameValidated = false;
            setError(shoppingListItemNameErrorMessage, response);
        }
        checkAllFieldsValidated();
    }

    private TextValidator.Response validateText(String textToValidate) {
        TextValidator.Request request = new TextValidator.Request(
                TextValidator.RequestType.SHORT_TEXT,
                textToValidate
        );
        return validationHandler.validateText(request);
    }

    private void setError(ObservableField<String> errorObservable,
                          TextValidator.Response response) {
        if (response.getResult() == TextValidator.Result.TOO_SHORT) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_short,
                    response.getMinLength(),
                    response.getMaxLength()));

        } else if (response.getResult() == TextValidator.Result.TOO_LONG) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_long,
                    response.getMinLength(),
                    response.getMaxLength()
            ));
        }
    }

    SingleLiveEvent<Boolean> getIdentityModelValidEvent() {
        return identityModelValidEvent;
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