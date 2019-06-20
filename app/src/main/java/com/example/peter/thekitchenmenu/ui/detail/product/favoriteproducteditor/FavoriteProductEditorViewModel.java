package com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsDataSource;
import com.example.peter.thekitchenmenu.data.repository.FavoriteProductsRepository;
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

import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.VALIDATED;

public class FavoriteProductEditorViewModel
        extends AndroidViewModel
        implements FavoriteProductsDataSource.GetFavoriteProductCallback {

    private static final String TAG = "tkm-FavProductEditorVM";

    private String favoriteProductId;
    public final ObservableField<String> retailer = new ObservableField<>();
    public final ObservableField<String> locationRoom = new ObservableField<>();
    public final ObservableField<String> locationInRoom = new ObservableField<>();
    public final ObservableField<String> price = new ObservableField<>();
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

    private final ObservableBoolean dataIsLoading = new ObservableBoolean();
    private final SingleLiveEvent<Void> favoriteProductIsUpdated = new SingleLiveEvent<>();

    private Application appContext;
    private FavoriteProductsRepository repository;

    private String productId;
    private boolean isNewFavoriteProduct;
    private boolean dataHasLoaded;

    public FavoriteProductEditorViewModel(@NonNull Application application,
                                          @NonNull FavoriteProductsRepository repository) {
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

    void start(String productId, String favoriteProductId) {

        Log.d(TAG, "start: productId=" + productId);
        if (favoriteProductId != null) Log.d(TAG, "start: favoriteProductId=" + favoriteProductId);

        this.productId = productId;
        if (dataIsLoading.get()) return;

        this.favoriteProductId = favoriteProductId;

        if (favoriteProductId == null) {
            isNewFavoriteProduct = true;
            return;
        }

        if (dataHasLoaded) return;
        isNewFavoriteProduct = false;
        dataIsLoading.set(true);

        repository.getFavoriteProduct(favoriteProductId, this);
    }

    @Override
    public void onFavoriteProductLoaded(FavoriteProductEntity favoriteProduct) {
        Log.d(TAG, "onFavoriteProductLoaded: ");

        if (favoriteProduct != null) {
            createDate = favoriteProduct.getCreateDate();

            retailer.set(favoriteProduct.getRetailer());
            locationRoom.set(favoriteProduct.getLocationRoom());
            locationInRoom.set(favoriteProduct.getLocationInRoom());
            price.set(favoriteProduct.getPrice());

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
        if (price.get() == null || Strings.isEmptyOrWhitespace(price.get())) {
            return;
        }

        BigDecimal minAmount = new BigDecimal(appContext.getString(R.string.min_currency_value));
        BigDecimal maxAmount = new BigDecimal(appContext.getString(R.string.max_price_for_product));
        BigDecimal givenAmount = removeCurrencySymbolFromPrice();

        if (givenAmount.compareTo(minAmount) >= 0  && givenAmount.compareTo(maxAmount) < 0) {
            priceValidated = true;
        } else {
            priceValidated = false;
            priceErrorEvent.setValue(appContext.getString(
                    R.string.input_error_product_pack_price));
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

    void saveFavoriteProduct() {
        FavoriteProductEntity favoriteProduct;
        String price = removeCurrencySymbolFromPrice().toString();
        Log.d(TAG, "saveFavoriteProduct: price=" + price);

        if (isNewFavoriteProduct || favoriteProductId == null) {
            favoriteProduct = FavoriteProductEntity.createFavoriteProduct(
                    productId,
                    retailer.get(),
                    locationRoom.get(),
                    locationInRoom.get(),
                    price);

            if (!favoriteProduct.isEmpty()) createFavoriteProduct(favoriteProduct);

        } else {
            favoriteProduct = FavoriteProductEntity.updateFavoriteProduct(
                    favoriteProductId,
                    productId,
                    retailer.get(),
                    locationRoom.get(),
                    locationInRoom.get(),
                    price,
                    createDate);

            if (!favoriteProduct.isEmpty()) updateFavoriteProduct(favoriteProduct);
        }

        if (favoriteProduct.isEmpty()) {
            Log.d(TAG, "saveFavoriteProduct: cannot save empty favorite product");
        }
    }

    private void createFavoriteProduct(FavoriteProductEntity favoriteProduct) {
        repository.saveFavoriteProduct(favoriteProduct);
        favoriteProductIsUpdated.call();
    }

    private void updateFavoriteProduct(FavoriteProductEntity favoriteProduct) {
        if (isNewFavoriteProduct)
            throw new RuntimeException("updateFavoriteProduct called but is new favorite Product.");
        repository.saveFavoriteProduct(favoriteProduct);
        favoriteProductIsUpdated.call();
    }

    SingleLiveEvent<Void> getFavoriteProductIsUpdated() {
        return favoriteProductIsUpdated;
    }
}
