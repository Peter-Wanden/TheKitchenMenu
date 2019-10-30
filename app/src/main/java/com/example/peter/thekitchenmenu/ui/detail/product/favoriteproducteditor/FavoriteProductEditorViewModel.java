package com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DataSourceFavoriteProducts;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.google.android.gms.common.util.Strings;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class FavoriteProductEditorViewModel
        extends AndroidViewModel
        implements DataSource.GetEntityCallback<FavoriteProductEntity> {

    private static final String TAG = "tkm-FavProductEditorVM";

    private Resources resources;
    private AddEditFavoriteProductNavigator navigator;
    private TextValidationHandler validationHandler;

    private String productId;
    private FavoriteProductEntity favoriteProductEntity;

    public final ObservableField<String> retailer = new ObservableField<>();
    public final ObservableField<String> locationRoom = new ObservableField<>();
    public final ObservableField<String> locationInRoom = new ObservableField<>();
    public final ObservableField<String> price = new ObservableField<>();

    public final ObservableField<String> retailerErrorMessage = new ObservableField<>();
    public final ObservableField<String> locationRoomErrorMessage = new ObservableField<>();
    public final ObservableField<String> locationInRoomErrorMessage = new ObservableField<>();
    public final ObservableField<String> priceErrorMessage = new ObservableField<>();
    private long createDate;

    private final SingleLiveEvent<String> setActivityTitleEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> allInputValuesAreValidEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> showUnsavedChangesDialogEvent = new SingleLiveEvent<>();

    private boolean
            retailerValidated,
            locationRoomValidated,
            locationInRoomValidated,
            priceValidated;

    private DataSourceFavoriteProducts favoriteProductEntityDataSource;
    public final ObservableBoolean dataIsLoading = new ObservableBoolean();
    private boolean isExistingFavoriteProductEntity;

    public FavoriteProductEditorViewModel(
            @NonNull Application application,
            @NonNull DataSourceFavoriteProducts favoriteProductEntityDataSource,
            TextValidationHandler validationHandler) {

        super(application);
        this.favoriteProductEntityDataSource = favoriteProductEntityDataSource;
        resources = application.getResources();
        this.validationHandler = validationHandler;

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

    void setNavigator(AddEditFavoriteProductNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start(String productId) {
        this.productId = productId;
        dataIsLoading.set(true);
        favoriteProductEntityDataSource.getByProductId(productId, this);
    }

    @Override
    public void onEntityLoaded(FavoriteProductEntity favoriteProductEntity) {
        dataIsLoading.set(false);
        this.favoriteProductEntity = favoriteProductEntity;
        setUpForExistingFavoriteProductEntity();
    }

    private void setUpForExistingFavoriteProductEntity() {
        isExistingFavoriteProductEntity = true;
        setEntityValuesToObservables(favoriteProductEntity);
        setTitle();
    }

    private void setEntityValuesToObservables(FavoriteProductEntity favoriteProductEntity) {
        createDate = favoriteProductEntity.getCreateDate();
        retailer.set(favoriteProductEntity.getRetailer());
        locationRoom.set(favoriteProductEntity.getLocationRoom());
        locationInRoom.set(favoriteProductEntity.getLocationInRoom());
        price.set(favoriteProductEntity.getPrice());
    }

    @Override
    public void onDataNotAvailable() {
        dataIsLoading.set(false);
        setUpForNewFavoriteProductEntity();
    }

    private void setUpForNewFavoriteProductEntity() {
        isExistingFavoriteProductEntity = false;
        setTitle();
    }

    private void setTitle() {
        if (isExistingFavoriteProductEntity)
            setActivityTitleEvent.setValue(resources.getString(
                    R.string.activity_title_add_favorite_product));
        else
            setActivityTitleEvent.setValue(resources.getString(
                    R.string.activity_title_edit_favorite_product));
    }

    SingleLiveEvent<String> getSetActivityTitleEvent() {
        return setActivityTitleEvent;
    }

    private void retailerChanged() {
        retailerErrorMessage.set(null);
        String retailerValidationResponse = validateText(retailer.get());

        if (retailerValidationResponse.equals(TextValidationHandler.VALIDATED)) {
            retailerValidated = true;
        }
        else {
            retailerValidated = false;
            retailerErrorMessage.set(retailerValidationResponse);
        }
        checkAllFieldsValidated();
    }

    private void locationRoomChanged() {
        locationRoomErrorMessage.set(null);
        String locationRoomValidationResponse = validateText(locationRoom.get());

        if (locationRoomValidationResponse.equals(TextValidationHandler.VALIDATED))
            locationRoomValidated = true;
        else {
            locationRoomValidated = false;
            locationRoomErrorMessage.set(locationRoomValidationResponse);
        }
        checkAllFieldsValidated();
    }

    private void locationInRoomChanged() {
        locationInRoomErrorMessage.set(null);
        String locationInRoomValidationResponse = validateText(locationInRoom.get());

        if (locationInRoomValidationResponse.equals(TextValidationHandler.VALIDATED))
            locationInRoomValidated = true;
        else {
            locationInRoomValidated = false;
            locationInRoomErrorMessage.set(locationInRoomValidationResponse);
        }
        checkAllFieldsValidated();
    }

    private String validateText(String textToValidate) {
        return validationHandler.validateShortText(resources, textToValidate);
    }

    private void priceChanged() {
        priceErrorMessage.set(null);
        if (Strings.isEmptyOrWhitespace(price.get()))
            return;

        BigDecimal minAmount = new BigDecimal(resources.getString(R.string.min_currency_value));
        BigDecimal maxAmount = new BigDecimal(resources.getString(R.string.max_price_for_product));
        BigDecimal givenAmount = removeCurrencySymbolFromPrice();

        if (givenAmount.compareTo(minAmount) >= 0 && givenAmount.compareTo(maxAmount) < 0)
            priceValidated = true;
        else {
            priceValidated = false;
            priceErrorMessage.set(resources.getString(R.string.input_error_product_pack_price));
        }
        checkAllFieldsValidated();
    }

    private BigDecimal removeCurrencySymbolFromPrice() {

        String replaceable = String.format(
                "[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
        String cleanString = price.get().replaceAll(replaceable, "");

        BigDecimal amount;

        try {
            amount = new BigDecimal(cleanString).setScale(
                    2, BigDecimal.ROUND_FLOOR).divide(
                    new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        } catch (NumberFormatException e) {
            amount = new BigDecimal("0");
        }
        return amount;
    }

    private void checkAllFieldsValidated() {
        if (retailerValidated &&
                locationRoomValidated &&
                locationInRoomValidated &&
                priceValidated)
            allInputValuesAreValidEvent.setValue(true);

        else
            allInputValuesAreValidEvent.setValue(false);
    }

    void upOrBackPressed() {
        if (isValidFavoriteProductEntity()) {
            showUnsavedChangesDialogEvent.call();
        } else
            navigator.onFavoriteEditAddCanceled();
    }

    private boolean isValidFavoriteProductEntity() {
        return allInputValuesAreValidEvent.getValue() &&
                (!isExistingFavoriteProductEntity || isExistingFavoriteProductThatHasBeenEdited());
    }

    SingleLiveEvent<Boolean> getAllInputValuesAreValidEvent() {
        return allInputValuesAreValidEvent;
    }

    private boolean isExistingFavoriteProductThatHasBeenEdited() {
        if (isExistingFavoriteProductEntity) {
            FavoriteProductEntity favoriteProductEntity = new FavoriteProductEntity(
                    this.favoriteProductEntity.getId(),
                    this.favoriteProductEntity.getProductId(),
                    retailer.get(),
                    locationRoom.get(),
                    locationInRoom.get(),
                    price.get(),
                    this.favoriteProductEntity.getCreateDate(),
                    this.favoriteProductEntity.getLastUpdate()
            );
            return this.favoriteProductEntity.equals(favoriteProductEntity);
        }
        return false;
    }

    void saveFavoriteProduct() {
        FavoriteProductEntity favoriteProduct;
        String price = removeCurrencySymbolFromPrice().toString();

        if (isExistingFavoriteProductEntity) {
            favoriteProduct = FavoriteProductEntity.updateFavoriteProduct(
                    favoriteProductEntity.getId(),
                    productId,
                    retailer.get(),
                    locationRoom.get(),
                    locationInRoom.get(),
                    price,
                    createDate);

            if (!favoriteProduct.isEmpty())
                updateFavoriteProduct(favoriteProduct);

        } else {
            favoriteProduct = FavoriteProductEntity.createFavoriteProduct(
                    productId,
                    retailer.get(),
                    locationRoom.get(),
                    locationInRoom.get(),
                    price);

            if (!favoriteProduct.isEmpty())
                saveNewFavoriteProduct(favoriteProduct);
        }

        if (favoriteProduct.isEmpty()) {
            Log.d(TAG, "saveFavoriteProduct: cannot save empty favorite product");
        }
    }

    private void saveNewFavoriteProduct(FavoriteProductEntity favoriteProduct) {
        favoriteProductEntityDataSource.save(favoriteProduct);
        navigator.onFavoriteProductSaved();
    }

    private void updateFavoriteProduct(FavoriteProductEntity favoriteProduct) {
        if (!isExistingFavoriteProductEntity)
            throw new RuntimeException("updateFavoriteProduct called but is new favorite Product.");
        favoriteProductEntityDataSource.save(favoriteProduct);
        navigator.onFavoriteProductSaved();
    }

    public String getProductId() {
        return productId;
    }

    SingleLiveEvent<Void> getShowUnsavedChangesDialogEvent() {
        return showUnsavedChangesDialogEvent;
    }
}
