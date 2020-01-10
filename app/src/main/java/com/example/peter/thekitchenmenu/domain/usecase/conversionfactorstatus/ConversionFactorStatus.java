package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;

public class ConversionFactorStatus
        extends UseCase<ConversionFactorStatusRequest, ConversionFactorStatusResponse>
        implements DataSource.GetEntityCallback<IngredientEntity> {

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
    protected void execute(ConversionFactorStatusRequest request) {
        System.out.println(TAG + "request:" + request);
        UnitOfMeasure unitOfMeasure = request.getSubtype().getMeasurementClass();

        if (isNoIdSupplied(request)) {
            returnDataNotAvailable();
        }
        if (!unitOfMeasure.isConversionFactorEnabled()) {
            returnResultDisabled();
            return;
        }
        loadIngredient(request.getIngredientId());
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
    public void onEntityLoaded(IngredientEntity ingredientEntity) {
        this.ingredientEntity = ingredientEntity;
        checkEditableStatus();
    }

    @Override
    public void onDataNotAvailable() {
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