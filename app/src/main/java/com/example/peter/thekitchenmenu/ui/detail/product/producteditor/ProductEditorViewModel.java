package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-EditorViewModel";

    private AddEditProductNavigator navigator;

    private final MutableLiveData<ProductEntity> uneditedProductEntity = new MutableLiveData<>();

    private final SingleLiveEvent<Void> updateOptionsMenuEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> showUnsavedChangesDialogEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> setActivityTitleEvent = new SingleLiveEvent<>();

    private ImageModel updatedImageModel = new ImageModel();
    private ProductIdentityModel updatedIdentityModel;
    private ProductMeasurementModel updatedMeasurementModel;

    private boolean
            isExistingProductEntity,
            identityModelIsValid,
            measurementModelIsValid,
            showReviewButton;

    // TODO - Change category and shelf life to an enum

    public ProductEditorViewModel(@NonNull Application application) {
        super(application);
    }

    void setNavigator(AddEditProductNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start(ProductEntity productEntity) {
        isExistingProductEntity = true;
        uneditedProductEntity.setValue(productEntity);
        setActivityTitleEvent.setValue(R.string.activity_title_edit_product);
    }

    void start() {
        isExistingProductEntity = false;
        setActivityTitleEvent.setValue(R.string.activity_title_add_new_product);
    }

    MutableLiveData<ProductEntity> getUneditedProductEntity() {
        return uneditedProductEntity;
    }

    void setUpdatedImageModel(ImageModel updatedImageModel) {
        this.updatedImageModel = updatedImageModel;
    }

    void setUpdatedIdentityModel(ProductIdentityModel updatedIdentityModel) {
        this.updatedIdentityModel = updatedIdentityModel;
    }

    void setIdentityModelIsValid(boolean identityModelIsValid) {
        this.identityModelIsValid = identityModelIsValid;
        enableReviewIfAllIsValid();
    }

    void setUpdatedMeasurementModel(ProductMeasurementModel updatedMeasurementModel) {
        this.updatedMeasurementModel = updatedMeasurementModel;
    }

    void setMeasurementModelIsValid(boolean measurementModelIsValid) {
        this.measurementModelIsValid = measurementModelIsValid;
        enableReviewIfAllIsValid();
    }

    private void enableReviewIfAllIsValid() {
        if (identityModelIsValid && measurementModelIsValid &&
                (isExistingProductEntityThatsBeenEdited() || !isExistingProductEntity)) {
            showReviewButton();
        } else {
            hideReviewButton();
        }
    }

    private boolean isExistingProductEntityThatsBeenEdited() {
        if (isExistingProductEntity) {
            ProductEntity productEntity = new ProductEntity(
                    uneditedProductEntity.getValue().getId(),
                    updatedIdentityModel.getDescription(),
                    updatedIdentityModel.getShoppingListItemName(),
                    updatedIdentityModel.getCategory(),
                    updatedIdentityModel.getShelfLife(),
                    updatedMeasurementModel.getNumberOfProducts(),
                    updatedMeasurementModel.getBaseUnits(),
                    updatedMeasurementModel.getMeasurementSubtype().ordinal(),
                    uneditedProductEntity.getValue().getCreatedBy(),
                    updatedImageModel.getWebImageUrl(),
                    updatedImageModel.getRemoteSmallImageUri(),
                    updatedImageModel.getRemoteMediumImageUri(),
                    updatedImageModel.getRemoteLargeImageUri(),
                    uneditedProductEntity.getValue().getCreateDate(),
                    uneditedProductEntity.getValue().getLastUpdate()
            );
            Log.d(TAG, "isExistingProductEntityThatsBeenEdited: unedited=" + uneditedProductEntity.getValue().toString());
            Log.d(TAG, "isExistingProductEntityThatsBeenEdited: edited=" + productEntity.toString());
            Log.d(TAG, "isExistingProductEntityThatsBeenEdited: equals=" + uneditedProductEntity.getValue().equals(productEntity));
            return !uneditedProductEntity.getValue().equals(productEntity);
        }
        return false;
    }

    private void showReviewButton() {
        showReviewButton = true;
        updateOptionsMenuEvent.call();
    }

    private void hideReviewButton() {
        showReviewButton = false;
        updateOptionsMenuEvent.call();
    }

    boolean isShowReviewButton() {
        return showReviewButton;
    }

    SingleLiveEvent<Void> getUpdateOptionsMenuEvent() {
        return updateOptionsMenuEvent;
    }

    void createOrUpdateProduct() {
        @NonNull
        ProductEntity productEntity;

        if (isExistingProductEntity) {
            productEntity = ProductEntity.updateProduct(
                    uneditedProductEntity.getValue().getId(),
                    updatedIdentityModel.getDescription(),
                    updatedIdentityModel.getShoppingListItemName(),
                    updatedIdentityModel.getCategory(),
                    updatedIdentityModel.getShelfLife(),
                    updatedMeasurementModel.getNumberOfProducts(),
                    updatedMeasurementModel.getBaseUnits(),
                    updatedMeasurementModel.getMeasurementSubtype().ordinal(),
                    uneditedProductEntity.getValue().getCreatedBy(),
                    updatedImageModel.getWebImageUrl(),
                    updatedImageModel.getRemoteSmallImageUri(),
                    updatedImageModel.getRemoteMediumImageUri(),
                    updatedImageModel.getLocalLargeImageUri(),
                    uneditedProductEntity.getValue().getCreateDate()
            );
            navigator.reviewEditedProduct(productEntity);

        } else {
            productEntity = ProductEntity.createProduct(
                    updatedIdentityModel.getDescription(),
                    updatedIdentityModel.getShoppingListItemName(),
                    updatedIdentityModel.getCategory(),
                    updatedIdentityModel.getShelfLife(),
                    updatedMeasurementModel.getNumberOfProducts(),
                    updatedMeasurementModel.getBaseUnits(),
                    updatedMeasurementModel.getMeasurementSubtype().ordinal(),
                    Constants.getUserId().getValue(),
                    updatedImageModel.getWebImageUrl(),
                    updatedImageModel.getRemoteSmallImageUri(),
                    updatedImageModel.getRemoteMediumImageUri(),
                    updatedImageModel.getLocalLargeImageUri()
            );
            navigator.reviewNewProduct(productEntity);
        }
    }

    void backPressed() {
        if (isExistingProductEntity)
            showUnsavedChangesDialogEvent.call();

        else if (updatedImageModel != null)
            showUnsavedChangesDialogEvent.call();

        else if (updatedIdentityModel != null)
            showUnsavedChangesDialogEvent.call();

        else if (updatedMeasurementModel != null)
            showUnsavedChangesDialogEvent.call();

        else
            navigator.cancelEditing();
    }

    SingleLiveEvent<Void> getShowUnsavedChangesDialogEvent() {
        return showUnsavedChangesDialogEvent;
    }

    SingleLiveEvent<Integer> getSetActivityTitleEvent() {
        return setActivityTitleEvent;
    }
}