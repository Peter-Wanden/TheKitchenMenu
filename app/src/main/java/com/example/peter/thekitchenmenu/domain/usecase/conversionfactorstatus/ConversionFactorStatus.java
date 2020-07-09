package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import java.util.HashMap;
import java.util.Map;

public class ConversionFactorStatus extends UseCaseBase
        implements DomainDataAccess.GetDomainModelCallback<IngredientPersistenceModel> {

    private static final String TAG = "tkm-" + ConversionFactorStatus.class.getSimpleName() + ": ";

    public enum Result {
        DATA_UNAVAILABLE,
        DISABLED,
        ENABLED_UNEDITABLE,
        ENABLED_EDITABLE_UNSET,
        ENABLED_EDITABLE_SET
    }

    public enum FailReason implements FailReasons {
        DISABLED(101),
        ENABLED_UNEDITABLE(102),
        ENABLED_EDITABLE_UNSET(103),
        ENABLED_EDITABLE_SET(104);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason fr : FailReason.values())
                options.put(fr.id, fr);
        }

        public static FailReason getFromId(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }

    private RepositoryIngredient repository;
    private IngredientEntity ingredientEntity;
    private IngredientPersistenceModel persistenceModel;

    public ConversionFactorStatus(RepositoryIngredient repository) {
        this.repository = repository;
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        ConversionFactorStatusRequest r = (ConversionFactorStatusRequest) request;
        UnitOfMeasure unitOfMeasure = r.getSubtype().getMeasurementClass();

        if (isNoIdSupplied(r)) {
            returnDataNotAvailable();
        }
        if (!unitOfMeasure.isConversionFactorEnabled()) {
            returnResultDisabled();
            return;
        }
        loadIngredient(r.getIngredientId());
    }

    private boolean isNoIdSupplied(ConversionFactorStatusRequest request) {
        return request.getIngredientId().equals(null) || request.getIngredientId().isEmpty();
    }

    private void returnResultDisabled() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                Result.DISABLED
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onUseCaseSuccess(response);
    }

    private void loadIngredient(String ingredientId) {
        repository.getByDataId(ingredientId, this);
    }

    @Override
    public void onPersistenceModelLoaded(IngredientPersistenceModel model) {
        persistenceModel = model;
        checkEditableStatus();
    }

    @Override
    public void onPersistenceModelUnavailable() {
        returnDataNotAvailable();
    }

    private void returnDataNotAvailable() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                        Result.DATA_UNAVAILABLE
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onUseCaseError(response);
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
        getUseCaseCallback().onUseCaseSuccess(response);
    }

    private void isConversionFactorPreviouslySet() {
        if (conversionFactorPreviouslySet()) {
            returnResultEnabledEditablePreviouslySet();
        } else {
            returnResultEnabledEditableUnSet();
        }
    }

    private boolean conversionFactorPreviouslySet() {
        return ingredientEntity.getConversionFactor() != UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
    }

    private void returnResultEnabledEditablePreviouslySet() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                Result.ENABLED_EDITABLE_SET
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onUseCaseSuccess(response);
    }

    private void returnResultEnabledEditableUnSet() {
        ConversionFactorStatusResponse response = new ConversionFactorStatusResponse(
                Result.ENABLED_EDITABLE_UNSET
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onUseCaseSuccess(response);
    }
}