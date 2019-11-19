package com.example.peter.thekitchenmenu.domain.usecase;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants;

import java.util.Objects;

public class UseCaseConversionFactorStatus
        extends
        UseCase<UseCaseConversionFactorStatus.RequestValues,
                UseCaseConversionFactorStatus.ResponseValues>
        implements
        DataSource.GetEntityCallback<IngredientEntity> {

    public enum UseCaseResult {
        DATA_NOT_AVAILABLE,
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

        if (requestValues.getIngredientId().equals(null) ||
                requestValues.getIngredientId().isEmpty()) {
            returnDataNotAvailable();
        }

        if (!unitOfMeasure.isConversionFactorEnabled()) {
            returnResultDisabled();
            return;
        }
        loadIngredient(requestValues.getIngredientId());
    }

    private void returnResultDisabled() {
        ResponseValues rV = new ResponseValues(UseCaseResult.DISABLED);
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
        returnDataNotAvailable();
    }

    private void returnDataNotAvailable() {
        ResponseValues values = new ResponseValues(UseCaseResult.DATA_NOT_AVAILABLE);
        getUseCaseCallback().onError(values);
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
        ResponseValues values  = new ResponseValues(UseCaseResult.ENABLED_UNEDITABLE);
        getUseCaseCallback().onSuccess(values);
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
        ResponseValues values = new ResponseValues(UseCaseResult.ENABLED_EDITABLE_SET);
        getUseCaseCallback().onSuccess(values);
    }

    private void returnResponseEnabledEditableUnSet() {
        ResponseValues values = new ResponseValues(UseCaseResult.ENABLED_EDITABLE_UNSET);
        getUseCaseCallback().onSuccess(values);
    }

    public static final class RequestValues implements UseCase.RequestValues {
        @NonNull
        private MeasurementSubtype subtype;
        @NonNull
        private String ingredientId;

        public RequestValues(@NonNull MeasurementSubtype subtype, @NonNull String ingredientId) {
            this.subtype = subtype;
            this.ingredientId = ingredientId;
        }

        @NonNull
        public MeasurementSubtype getSubtype() {
            return subtype;
        }

        @NonNull
        public String getIngredientId() {
            return ingredientId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RequestValues that = (RequestValues) o;
            return subtype == that.subtype &&
                    ingredientId.equals(that.ingredientId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subtype, ingredientId);
        }
    }

    public static final class ResponseValues implements UseCase.ResponseValues {
        @NonNull
        private UseCaseResult result;

        public ResponseValues(@NonNull UseCaseResult result) {
            this.result = result;
        }

        @NonNull
        public UseCaseResult getResult() {
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResponseValues that = (ResponseValues) o;
            return result == that.result;
        }

        @Override
        public int hashCode() {
            return Objects.hash(result);
        }

        @Override
        public String toString() {
            return "ResponseValues{" +
                    "result=" + result +
                    '}';
        }
    }
}