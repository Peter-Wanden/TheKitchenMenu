package com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.repository.UsedProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.UsedProductRepository;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.VALIDATED;

public class UsedProductEditorViewModel
        extends AndroidViewModel
        implements UsedProductDataSource.GetUsedProductCallback {

    private static final String TAG = "tkm-UsedProductEditorVM";

    private String usedProductId;
    public final ObservableField<String> retailer = new ObservableField<>();
    public final ObservableField<String> locationRoom = new ObservableField<>();
    public final ObservableField<String> locationInRoom = new ObservableField<>();
    public final ObservableDouble price = new ObservableDouble();
    private long createDate;

    private final SingleLiveEvent<String> retailerErrorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> locationRoomErrorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> locationInRoomErrorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> priceErrorEvent = new SingleLiveEvent<>();

    private boolean retailerValidated;
    private boolean locationRoomValidated;
    private boolean locationInRoomValidated;
    private boolean priceValidated;
    public final ObservableBoolean allFieldsValidated = new ObservableBoolean();

    public final ObservableBoolean dataIsLoading = new ObservableBoolean();
    private final SingleLiveEvent<Void> usedProductIsUpdated = new SingleLiveEvent<>();

    private Application appContext;
    private UsedProductRepository repository;

    private String productId;
    private boolean isNewUsedProduct;
    private boolean dataHasLoaded;

    public UsedProductEditorViewModel(@NonNull Application application,
                                      @NonNull UsedProductRepository repository) {
        super(application);
        this.appContext = application;
        this.repository = repository;

        retailer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                retailerChanged();
            }
        });

        locationRoom.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                locationRoomChanged();
            }
        });

        locationInRoom.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                locationInRoomChanged();
            }
        });

        price.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                priceChanged();
            }
        });
    }

    void start(String productId, String usedProductId) {

        Log.d(TAG, "start: productId=" + productId);
        if (usedProductId != null) Log.d(TAG, "start: usedProductId=" + usedProductId);

        this.productId = productId;
        if (dataIsLoading.get()) return;

        this.usedProductId = usedProductId;

        if (usedProductId == null) {
            isNewUsedProduct = true;
            return;
        }

        if (dataHasLoaded) return;
        isNewUsedProduct = false;
        dataIsLoading.set(true);

        repository.getUsedProduct(usedProductId, this);
    }

    @Override
    public void onUsedProductLoaded(UsedProductEntity usedProduct) {
        Log.d(TAG, "onUsedProductLoaded: ");

        if (usedProduct != null) {
            createDate = usedProduct.getCreateDate();

            retailer.set(usedProduct.getRetailer());
            locationRoom.set(usedProduct.getLocationRoom());
            locationInRoom.set(usedProduct.getLocationInRoom());
            price.set(usedProduct.getPrice());

            dataIsLoading.set(false);
            dataHasLoaded = true;
        }
    }

    @Override
    public void onDataNotAvailable() {
        dataIsLoading.set(false);
    }

    private void retailerChanged() {
        String retailerValidationResponse = validateText(retailer.get());

        if (retailerValidationResponse.equals(VALIDATED)) {
            retailerValidated = true;

        } else {
            retailerValidated = false;
            retailerErrorEvent.setValue(retailerValidationResponse);
        }
        checkAllFieldsValidated();
    }

    private void locationRoomChanged() {
        String locationRoomValidationResponse = validateText(locationRoom.get());

        if (locationRoomValidationResponse.equals(VALIDATED)) {
            locationRoomValidated = true;

        } else {
            locationRoomValidated = false;
            locationRoomErrorEvent.setValue(locationRoomValidationResponse);
        }
        checkAllFieldsValidated();
    }

    private void locationInRoomChanged() {
        String locationInRoomValidationResponse = validateText(locationInRoom.get());

        if (locationInRoomValidationResponse.equals(VALIDATED)) {
            locationInRoomValidated = true;

        } else {
            locationInRoomValidated = false;
            locationInRoomErrorEvent.setValue(locationInRoomValidationResponse);
        }
        checkAllFieldsValidated();
    }

    private String validateText(String textToValidate) {
        return TextValidationHandler.validateText(appContext, textToValidate);
    }

    private void priceChanged() {
        if (price.get() > 0 && price.get() < 100) {
            priceValidated = true;

        } else {
            priceValidated = false;
            priceErrorEvent.setValue("Price must be greater than 0p and less than £100");
        }
        checkAllFieldsValidated();
    }

    private void checkAllFieldsValidated() {

        if (retailerValidated &&
                locationRoomValidated &&
                locationInRoomValidated &&
                priceValidated)
            allFieldsValidated.set(true);

        else allFieldsValidated.set(false);
    }

    SingleLiveEvent<String> getRetailerErrorEvent() {
        return retailerErrorEvent;
    }

    SingleLiveEvent<String> getLocationRoomErrorEvent() {
        return locationRoomErrorEvent;
    }

    SingleLiveEvent<String> getLocationInRoomErrorEvent() {
        return locationInRoomErrorEvent;
    }

    SingleLiveEvent<String> getPriceErrorEvent() {
        return priceErrorEvent;
    }

    void saveUsedProduct() {
        UsedProductEntity usedProduct;

        if (isNewUsedProduct || usedProductId == null) {
            usedProduct = UsedProductEntity.createNewUsedProduct(
                    productId,
                    retailer.get(),
                    locationRoom.get(),
                    locationInRoom.get(),
                    price.get());

            if (!usedProduct.isEmpty()) createUsedProduct(usedProduct);

        } else {
            usedProduct = UsedProductEntity.updateUsedProduct(
                    usedProductId,
                    productId,
                    retailer.get(),
                    locationRoom.get(),
                    locationInRoom.get(),
                    price.get(),
                    createDate);

            if (!usedProduct.isEmpty()) updateUsedProduct(usedProduct);
        }

        if (usedProduct.isEmpty()) {
            Log.d(TAG, "saveUsedProduct: cannot save empty used product");
        }
    }

    private void createUsedProduct(UsedProductEntity usedProduct) {
        Log.d(TAG, "createUsedProduct: ");
        repository.saveUsedProduct(usedProduct);
        usedProductIsUpdated.call();
    }

    private void updateUsedProduct(UsedProductEntity usedProduct) {
        Log.d(TAG, "updateUsedProduct: ");
        if (isNewUsedProduct)
            throw new RuntimeException("updateUsedProduct called but is new UsedProduct.");
        repository.saveUsedProduct(usedProduct);
        usedProductIsUpdated.call();
    }

    SingleLiveEvent<Void> getUsedProductIsUpdated() {
        return usedProductIsUpdated;
    }
}
