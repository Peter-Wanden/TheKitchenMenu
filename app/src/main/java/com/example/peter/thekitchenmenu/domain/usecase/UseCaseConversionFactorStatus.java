package com.example.peter.thekitchenmenu.domain.usecase;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants;

public class UseCaseConversionFactorStatus
        extends
        UseCase<UseCaseConversionFactorStatus.RequestValues,
                UseCaseConversionFactorStatus.ResponseValues>
        implements
        DataSource.GetEntityCallback<IngredientEntity> {

    public enum UseCaseConversionFactorResult {
        NO_DATA_AVAILABLE,
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
    protected void executeUseCase(RequestValues requestValues) {
        UnitOfMeasure unitOfMeasure = requestValues.getSubtype().getMeasurementClass();

        if (!unitOfMeasure.isConversionFactorEnabled()) {
            returnResultDisabled();
            return;
        }
        loadIngredient(requestValues.getIngredientId());
    }

    private void returnResultDisabled() {
        ResponseValues rV = new ResponseValues(UseCaseConversionFactorResult.DISABLED);
        getUseCaseCallback().onSuccess(rV);
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
        ResponseValues rV = new ResponseValues(UseCaseConversionFactorResult.NO_DATA_AVAILABLE);
        getUseCaseCallback().onError(rV);
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
        ResponseValues rV  = new ResponseValues(UseCaseConversionFactorResult.ENABLED_UNEDITABLE);
        getUseCaseCallback().onSuccess(rV);
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
        ResponseValues rV = new ResponseValues(UseCaseConversionFactorResult.ENABLED_EDITABLE_SET);
        getUseCaseCallback().onSuccess(rV);
    }

    private void returnResponseEnabledEditableUnSet() {
        ResponseValues rV = new ResponseValues(UseCaseConversionFactorResult.ENABLED_EDITABLE_UNSET);
        getUseCaseCallback().onSuccess(rV);
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private MeasurementSubtype subtype;
        private String ingredientId;

        public RequestValues(MeasurementSubtype subtype, String ingredientId) {
            this.subtype = subtype;
            this.ingredientId = ingredientId;
        }

        public MeasurementSubtype getSubtype() {
            return subtype;
        }

        public String getIngredientId() {
            return ingredientId;
        }
    }

    public static final class ResponseValues implements UseCase.ResponseValues {
        private UseCaseConversionFactorResult result;

        public ResponseValues(UseCaseConversionFactorResult result) {
            this.result = result;
        }

        public UseCaseConversionFactorResult getResult() {
            return result;
        }
    }
}
