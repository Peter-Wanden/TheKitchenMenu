package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.UseCaseCommandAbstract;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;

public class UseCaseConversionFactorStatus
        extends UseCaseCommandAbstract<UseCaseConversionFactorStatusRequest, UseCaseConversionFactorStatusResponse>
        implements DataSource.GetEntityCallback<IngredientEntity> {

    public enum UseCaseResult {
        INGREDIENT_DATA_NOT_AVAILABLE,
        DISABLED,
        ENABLED_UNEDITABLE,
        ENABLED_EDITABLE_UNSET,
        ENABLED_EDITABLE_SET
    }

    private RepositoryIngredient repository;
    private IngredientEntity ingredientEntity;

    public UseCaseConversionFactorStatus(RepositoryIngredient repository) {
        this.repository = repository;
    }

    @Override
    protected void execute(UseCaseConversionFactorStatusRequest request) {
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
        UseCaseConversionFactorStatusResponse response = new
                UseCaseConversionFactorStatusResponse(UseCaseResult.DISABLED);
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
        UseCaseConversionFactorStatusResponse response =
                new UseCaseConversionFactorStatusResponse(
                        UseCaseResult.INGREDIENT_DATA_NOT_AVAILABLE);
        getUseCaseCallback().onError(response);
    }

    private void checkEditableStatus() {
        if (userCanEditIngredient()) {
            hasConversionFactorBeenPreviouslySet();
        } else {
            returnResultUneditable();
        }
    }

    private boolean userCanEditIngredient() {
        return Constants.getUserId().getValue().equals(ingredientEntity.getCreatedBy());
    }

    private void returnResultUneditable() {
        UseCaseConversionFactorStatusResponse response  =
                new UseCaseConversionFactorStatusResponse(UseCaseResult.ENABLED_UNEDITABLE);
        getUseCaseCallback().onSuccess(response);
    }

    private void hasConversionFactorBeenPreviouslySet() {
        if (conversionFactorPreviouslySet()) {
            returnResultEnabledEditablePreviouslySet();
        } else {
            returnResponseEnabledEditableUnSet();
        }
    }

    private boolean conversionFactorPreviouslySet() {
        return ingredientEntity.getConversionFactor() != UnitOfMeasureConstants.NO_CONVERSION_FACTOR;
    }

    private void returnResultEnabledEditablePreviouslySet() {
        UseCaseConversionFactorStatusResponse response =
                new UseCaseConversionFactorStatusResponse(UseCaseResult.ENABLED_EDITABLE_SET);
        getUseCaseCallback().onSuccess(response);
    }

    private void returnResponseEnabledEditableUnSet() {
        UseCaseConversionFactorStatusResponse response =
                new UseCaseConversionFactorStatusResponse(UseCaseResult.ENABLED_EDITABLE_UNSET);
        getUseCaseCallback().onSuccess(response);
    }
}