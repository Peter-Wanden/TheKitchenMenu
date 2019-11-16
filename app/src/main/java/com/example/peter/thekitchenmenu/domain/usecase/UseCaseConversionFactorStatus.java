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
        DISABLED,
        ENABLED_UNEDITABLE,
        ENABLED_EDITABLE_UNSET,
        ENABLED_EDITABLE_SET
    }

    private RepositoryIngredient repository;
    private boolean isConversionFactorEnabled;
    private IngredientEntity ingredientEntity;

    public UseCaseConversionFactorStatus(RepositoryIngredient repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        UnitOfMeasure unitOfMeasure = requestValues.getSubtype().getMeasurementClass();

        isConversionFactorEnabled = unitOfMeasure.isConversionFactorEnabled();
        if (!isConversionFactorEnabled) {
            getResult();
            return;
        }
        loadIngredient(requestValues.getIngredientId());
    }

    private void loadIngredient(String ingredientId) {
        repository.getById(ingredientId, this);
    }

    @Override
    public void onEntityLoaded(IngredientEntity ingredientEntity) {
        this.ingredientEntity = ingredientEntity;
        getResult();
    }

    @Override
    public void onDataNotAvailable() {
        ResponseValues responseValues = new ResponseValues(UseCaseConversionFactorResult.DISABLED);
        getUseCaseCallback().onError(responseValues);
    }

    private void getResult() {
        ResponseValues responseValues;
        if (isConversionFactorEnabled) {
            if (userCanEditIngredient()) {
                if (conversionFactorIsSet()) {
                    responseValues = new ResponseValues(
                            UseCaseConversionFactorResult.ENABLED_EDITABLE_SET);
                } else {
                    responseValues = new ResponseValues(
                            UseCaseConversionFactorResult.ENABLED_EDITABLE_UNSET);
                }
            } else {
                responseValues = new ResponseValues(
                        UseCaseConversionFactorResult.ENABLED_UNEDITABLE);
            }
        } else {
            responseValues = new ResponseValues(
                    UseCaseConversionFactorResult.DISABLED);
        }
        getUseCaseCallback().onSuccess(responseValues);
    }

    private boolean userCanEditIngredient() {
        return Constants.getUserId().getValue().equals(ingredientEntity.getCreatedBy());
    }

    private boolean conversionFactorIsSet() {
        return ingredientEntity.getConversionFactor() != UnitOfMeasureConstants.NO_CONVERSION_FACTOR;
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
