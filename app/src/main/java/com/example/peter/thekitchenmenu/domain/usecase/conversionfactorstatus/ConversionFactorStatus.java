package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;

public class ConversionFactorStatus extends UseCase
        implements PrimitiveDataSource.GetEntityCallback<IngredientEntity> {

    private static final String TAG = "tkm-" + ConversionFactorStatus.class.getSimpleName() + ": ";

    public enum Result {
        DATA_UNAVAILABLE,
        DISABLED,
        ENABLED_UNEDITABLE,
        ENABLED_EDITABLE_UNSET,
        ENABLED_EDITABLE_SET
    }

    private RepositoryIngredient repository;
    private IngredientEntity ingredientEntity;

    public ConversionFactorStatus(RepositoryIngredient repository) {
        this.repository = repository;
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        ConversionFactorStatusRequest sr = (ConversionFactorStatusRequest) request;
        UnitOfMeasure unitOfMeasure = sr.getSubtype().getMeasurementClass();

        if (isNoIdSupplied(sr)) {
            returnDataNotAvailable();
        }
        if (!unitOfMeasure.isConversionFactorEnabled()) {
            returnResultDisabled();
            return;
        }
        loadIngredient(sr.getIngredientId());
    }

    private boolean isNoIdSupplied(ConversionFactorStatusRequest request) {
        return request.getIngredientId().equals(null) || request.getIngredientId().isEmpty();
    }

    private void returnResultDisabled() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                Result.DISABLED
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onSuccess(response);
    }

    private void loadIngredient(String ingredientId) {
        repository.getById(ingredientId, this);
    }

    @Override
    public void onEntityLoaded(IngredientEntity entity) {
        this.ingredientEntity = entity;
        checkEditableStatus();
    }

    @Override
    public void onDataUnavailable() {
        returnDataNotAvailable();
    }

    private void returnDataNotAvailable() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                        Result.DATA_UNAVAILABLE
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onError(response);
    }

    private void checkEditableStatus() {
        if (userCanEditIngredient()) {
            isConversionFactorPreviouslySet();
        } else {
            returnResultUneditable();
        }
    }

    private boolean userCanEditIngredient() {
        return Constants.getUserId().equals(ingredientEntity.getCreatedBy());
    }

    private void returnResultUneditable() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                Result.ENABLED_UNEDITABLE
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onSuccess(response);
    }

    private void isConversionFactorPreviouslySet() {
        if (conversionFactorPreviouslySet()) {
            returnResultEnabledEditablePreviouslySet();
        } else {
            returnResultEnabledEditableUnSet();
        }
    }

    private boolean conversionFactorPreviouslySet() {
        return ingredientEntity.getConversionFactor() != UnitOfMeasureConstants.NO_CONVERSION_FACTOR;
    }

    private void returnResultEnabledEditablePreviouslySet() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                Result.ENABLED_EDITABLE_SET
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onSuccess(response);
    }

    private void returnResultEnabledEditableUnSet() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                Result.ENABLED_EDITABLE_UNSET
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onSuccess(response);
    }
}