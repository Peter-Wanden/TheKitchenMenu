package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;

public class ConversionFactorStatus
        extends UseCaseInteractor<ConversionFactorStatusRequest, ConversionFactorStatusResponse>
        implements DataSource.GetEntityCallback<IngredientEntity> {

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
        UnitOfMeasure unitOfMeasure = request.getSubtype().getMeasurementClass();

        if (request.getIngredientId().equals(null) ||
                request.getIngredientId().isEmpty()) {
            returnDataNotAvailable();
        }

        if (!unitOfMeasure.isConversionFactorEnabled()) {
            returnResultDisabled();
            return;
        }
        loadIngredient(request.getIngredientId());
    }

    private void returnResultDisabled() {
        ConversionFactorStatusResponse response = new
                ConversionFactorStatusResponse(Result.DISABLED);
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
        ConversionFactorStatusResponse response =
                new ConversionFactorStatusResponse(
                        Result.DATA_UNAVAILABLE);
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
        ConversionFactorStatusResponse response  =
                new ConversionFactorStatusResponse(Result.ENABLED_UNEDITABLE);
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
        ConversionFactorStatusResponse response =
                new ConversionFactorStatusResponse(Result.ENABLED_EDITABLE_SET);
        getUseCaseCallback().onSuccess(response);
    }

    private void returnResultEnabledEditableUnSet() {
        ConversionFactorStatusResponse response =
                new ConversionFactorStatusResponse(Result.ENABLED_EDITABLE_UNSET);
        getUseCaseCallback().onSuccess(response);
    }
}