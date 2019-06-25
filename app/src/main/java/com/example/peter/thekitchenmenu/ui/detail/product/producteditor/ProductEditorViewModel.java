package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.data.repository.ProductDataSource;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-EditorViewModel";

    private ProductRepository repository;
    private AddEditProductNavigator navigator;

    private final MutableLiveData<ProductEntity> existingProductEntity = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> showSaveButtonEvent = new SingleLiveEvent<>();

    private ImageModel updatedImageModel = new ImageModel();
    private ProductIdentityModel updatedIdentityModel = new ProductIdentityModel();
    private ProductMeasurementModel updatedMeasurementModel = new ProductMeasurementModel();

    private boolean
            isExistingProduct = false,
            identityModelIsValid = false,
            measurementModelIsValid = false;
    // TODO - Change category and shelf life to an enum

    public ProductEditorViewModel(@NonNull Application application, ProductRepository repository) {
        super(application);
        this.repository = repository;
    }

    void setNavigator(AddEditProductNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void editProduct(String productId) {
        repository.getProduct(productId, new ProductDataSource.GetProductCallback() {
            @Override
            public void onProductLoaded(ProductEntity productEntity) {
                existingProductEntity.setValue(productEntity);
            }

            @Override
            public void onDataNotAvailable() {
                // This is an error, as there should always be a product available
            }
        });
    }

    void setExistingProduct(boolean existingProduct) {
        isExistingProduct = existingProduct;
    }

    MutableLiveData<ProductEntity> getExistingProductEntity() {
        return existingProductEntity;
    }

    void setUpdatedImageModel(ImageModel updatedImageModel) {
        this.updatedImageModel = updatedImageModel;
        // TODO Save images to remote database
    }

    void setIdentityModelIsValid(boolean identityModelIsValid) {
        this.identityModelIsValid = identityModelIsValid;
        checkAllModelsAreValid();
    }

    void setUpdatedIdentityModel(ProductIdentityModel updatedIdentityModel) {
        this.updatedIdentityModel = updatedIdentityModel;
    }

    void setMeasurementModelIsValid(boolean measurementModelIsValid) {
        this.measurementModelIsValid = measurementModelIsValid;
        checkAllModelsAreValid();
    }

    void setUpdatedMeasurementModel(ProductMeasurementModel updatedMeasurementModel) {
        this.updatedMeasurementModel = updatedMeasurementModel;
    }

    SingleLiveEvent<Boolean> getShowSaveButtonEvent() {
        return showSaveButtonEvent;
    }

    private void checkAllModelsAreValid() {
        if (identityModelIsValid && measurementModelIsValid) {
            showSaveButtonEvent.setValue(true);
        } else {
            showSaveButtonEvent.setValue(false);
        }
    }

    void saveProduct() {

        ProductEntity productEntity;
        if (isExistingProduct) {
            productEntity = ProductEntity.updateProduct(
                    existingProductEntity.getValue().getId(),
                    updatedIdentityModel.getDescription(),
                    updatedIdentityModel.getShoppingListItemName(),
                    updatedIdentityModel.getCategory(),
                    updatedIdentityModel.getShelfLife(),
                    updatedMeasurementModel.getNumberOfProducts(),
                    updatedMeasurementModel.getBaseUnits(),
                    updatedMeasurementModel.getMeasurementSubtype().ordinal(),
                    existingProductEntity.getValue().getCreatedBy(),
                    updatedImageModel.getWebImageUrl(),
                    updatedImageModel.getRemoteSmallImageUri(),
                    updatedImageModel.getRemoteMediumImageUri(),
                    updatedImageModel.getLocalLargeImageUri(),
                    existingProductEntity.getValue().getCreateDate()
            );
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
        }
        repository.saveProduct(productEntity);
        navigator.onProductSaved(productEntity.getId());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // TODO - Abort all network operations, unsubscribe observers and drop callbacks.
    }
}