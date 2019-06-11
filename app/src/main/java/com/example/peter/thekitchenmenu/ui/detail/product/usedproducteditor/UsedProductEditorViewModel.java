package com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor;

import android.app.Application;
import android.text.Editable;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.entity.UsedProductEntity;
import com.example.peter.thekitchenmenu.data.repository.UsedProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.UsedProductRepository;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.VALIDATED;

public class UsedProductEditorViewModel
        extends AndroidViewModel // TODO - Should it extend BaseObservable?
        implements UsedProductDataSource.GetUsedProductCallback {

    private static final String TAG = "tkm-UsedProductEditorVM";

    private String usedProductId;
    public final ObservableField<String> retailer = new ObservableField<>();
    public final ObservableField<String> locationRoom = new ObservableField<>();
    public final ObservableField<String> locationInRoom = new ObservableField<>();
    public final ObservableDouble price = new ObservableDouble();
    private long createDate;
    private long lastUpdate;

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
    private boolean usedProductIsComplete;

    // For testing

    private UsedProductEntity usedProduct;

    public UsedProductEditorViewModel(@NonNull Application application,
                                      @NonNull UsedProductRepository repository) {
        super(application);
        this.appContext = application;
        this.repository = repository;

        usedProduct = UsedProductEntity.createNewUsedProduct(
                "1234",
                "Waitrose",
                "Kitchen",
                "Fridge",
                24.99);
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

        if (usedProduct != null) {

            retailer.set(usedProduct.getRetailer());
            locationRoom.set(usedProduct.getLocationRoom());
            locationInRoom.set(usedProduct.getLocationInRoom());
            price.set(usedProduct.getPrice());

            createDate = usedProduct.getCreateDate();
            lastUpdate = usedProduct.getLastUpdate();

            dataIsLoading.set(false);
            dataHasLoaded = true;
        }
    }

    @Override
    public void onDataNotAvailable() {
        dataIsLoading.set(false);
    }

    public void retailerHasChanged(Editable editedRetailer) {
        Log.d(TAG, "retailerHasChanged: =" + editedRetailer.toString());
        retailer.set(editedRetailer.toString());
        String retailerValidationResponse = validateText(editedRetailer);

        if (retailerValidationResponse.equals(VALIDATED)) {
            retailerValidated = true;
            checkAllFieldsValidated();
        } else {
            retailerValidated = false;
            retailerErrorEvent.setValue(retailerValidationResponse);
        }
    }

    private String validateText(Editable editable) {
        return TextValidationHandler.validateText(appContext, editable);
    }

    private void checkAllFieldsValidated() {
        if (retailerValidated && locationRoomValidated && locationInRoomValidated && priceValidated)
            allFieldsValidated.set(true);
        else allFieldsValidated.set(false);
    }

    public SingleLiveEvent<String> getRetailerErrorEvent() {
        return retailerErrorEvent;
    }

    public SingleLiveEvent<String> getLocationRoomErrorEvent() {
        return locationRoomErrorEvent;
    }

    public SingleLiveEvent<String> getLocationInRoomErrorEvent() {
        return locationInRoomErrorEvent;
    }

    public SingleLiveEvent<String> getPriceErrorEvent() {
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
        } else {
            usedProduct = UsedProductEntity.updateUsedProduct(
                    usedProductId,
                    productId,
                    retailer.get(),
                    locationRoom.get(),
                    locationInRoom.get(),
                    price.get(),
                    createDate);
        }
    }
}
