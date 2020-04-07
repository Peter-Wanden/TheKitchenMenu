package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.ui.ObservableAndroidViewModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.lifecycle.MutableLiveData;

import javax.annotation.Nonnull;

public class ProductEditorViewModel extends ObservableAndroidViewModel {

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

    public ProductEditorViewModel(@Nonnull Application application) {
        super(application);
    }

    void setNavigator(AddEditProductNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start() {
        isExistingProductEntity = false;
        setActivityTitleEvent.setValue(R.string.activity_title_add_new_product);
    }

    void start(ProductEntity productEntity) {
        isExistingProductEntity = true;
        uneditedProductEntity.setValue(productEntity);
        setActivityTitleEvent.setValue(R.string.activity_title_edit_product);
    }

    SingleLiveEvent<Integer> getSetActivityTitleEvent() {
        return setActivityTitleEvent;
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
        enableReviewIfValidProductEntity();
    }

    void setUpdatedMeasurementModel(ProductMeasurementModel updatedMeasurementModel) {
        this.updatedMeasurementModel = updatedMeasurementModel;
    }

    void setMeasurementModelIsValid(boolean measurementModelIsValid) {
        this.measurementModelIsValid = measurementModelIsValid;
        enableReviewIfValidProductEntity();
    }

    private void enableReviewIfValidProductEntity() {
        if (isValidatedProductEntity())
            showReviewButton();
        else
            hideReviewButton();
    }

    void upOrBackPressed() {
        if (isValidatedProductEntity())
            showUnsavedChangesDialogEvent.call();
        else
            navigator.cancelEditing();
    }

    private boolean isValidatedProductEntity() {
        return identityModelIsValid && measurementModelIsValid &&
                (isExistingProductEntityThatHasBeenEdited() || !isExistingProductEntity);
    }

    private boolean isExistingProductEntityThatHasBeenEdited() {
        if (isExistingProductEntity) {
            ProductEntity productEntity = new ProductEntity(
                    uneditedProductEntity.getValue().getId(),
                    updatedIdentityModel.getDescription(),
                    updatedIdentityModel.getShoppingListItemName(),
                    updatedIdentityModel.getCategory(),
                    updatedIdentityModel.getShelfLife(),
                    updatedMeasurementModel.getNumberOfItems(),
                    updatedMeasurementModel.getBaseUnits(),
                    updatedMeasurementModel.getMeasurementSubtype().asInt(),
                    uneditedProductEntity.getValue().getCreatedBy(),
                    updatedImageModel.getWebImageUrl(),
                    updatedImageModel.getRemoteSmallImageUri(),
                    updatedImageModel.getRemoteMediumImageUri(),
                    updatedImageModel.getRemoteLargeImageUri(),
                    uneditedProductEntity.getValue().getCreateDate(),
                    uneditedProductEntity.getValue().getLastUpdate()
            );
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
        @Nonnull
        ProductEntity productEntity;

        if (isExistingProductEntity) {
            productEntity = ProductEntity.updateProduct(
                    uneditedProductEntity.getValue().getId(),
                    updatedIdentityModel.getDescription(),
                    updatedIdentityModel.getShoppingListItemName(),
                    updatedIdentityModel.getCategory(),
                    updatedIdentityModel.getShelfLife(),
                    updatedMeasurementModel.getNumberOfItems(),
                    updatedMeasurementModel.getBaseUnits(),
                    updatedMeasurementModel.getMeasurementSubtype().asInt(),
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
                    updatedMeasurementModel.getNumberOfItems(),
                    updatedMeasurementModel.getBaseUnits(),
                    updatedMeasurementModel.getMeasurementSubtype().asInt(),
                    Constants.getUserId(),
                    updatedImageModel.getWebImageUrl(),
                    updatedImageModel.getRemoteSmallImageUri(),
                    updatedImageModel.getRemoteMediumImageUri(),
                    updatedImageModel.getLocalLargeImageUri()
            );
            navigator.reviewNewProduct(productEntity);
        }
    }

    SingleLiveEvent<Void> getShowUnsavedChangesDialogEvent() {
        return showUnsavedChangesDialogEvent;
    }
}