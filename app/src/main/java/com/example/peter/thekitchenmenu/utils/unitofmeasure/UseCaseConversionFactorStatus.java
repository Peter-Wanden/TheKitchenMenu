package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseConversionFactorStatus.UseCaseConversionFactorResult.*;

public class UseCaseConversionFactorStatus
        implements DataSource.GetEntityCallback<IngredientEntity> {

    public interface UseCaseConversionFactorCallback {
        void useCaseConversionFactorResult(UseCaseConversionFactorResult result);
    }

    public enum UseCaseConversionFactorResult {
        DISABLED,
        ENABLED_UNEDITABLE,
        ENABLED_EDITABLE_UNSET,
        ENABLED_EDITABLE_SET
    }

    private List<UseCaseConversionFactorCallback> statusReceivers = new ArrayList<>();
    private RepositoryIngredient repository;
    private boolean isConversionFactorEnabled;
    private IngredientEntity ingredientEntity;

    public UseCaseConversionFactorStatus(RepositoryIngredient repository) {
        this.repository = repository;
    }

    public void registerListener(UseCaseConversionFactorCallback listener) {
        statusReceivers.add(listener);
    }

    public void unregisterListener(UseCaseConversionFactorCallback listener) {
        statusReceivers.remove(listener);
    }

    public void getConversionFactorStatus(MeasurementSubtype subtype,
                                          String ingredientId) {
        UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();
        isConversionFactorEnabled = unitOfMeasure.isConversionFactorEnabled();

        if (!isConversionFactorEnabled) {
            getResult();
            return;
        }
        loadIngredient(ingredientId);
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
        notifyListeners(DISABLED);
    }

    private void getResult() {
        if (isConversionFactorEnabled) {
            if (userCanEditIngredient()) {
                if (conversionFactorIsSet()) {
                    notifyListeners(ENABLED_EDITABLE_SET);
                } else {
                    notifyListeners(ENABLED_EDITABLE_UNSET);
                }
            } else {
                notifyListeners(ENABLED_UNEDITABLE);
            }
        } else {
            notifyListeners(DISABLED);
        }
    }

    private boolean userCanEditIngredient() {
        return Constants.getUserId().getValue().equals(ingredientEntity.getCreatedBy());
    }

    private boolean conversionFactorIsSet() {
        return ingredientEntity.getConversionFactor() != UnitOfMeasureConstants.NO_CONVERSION_FACTOR;
    }

    private void notifyListeners(UseCaseConversionFactorResult result) {
        for (UseCaseConversionFactorCallback callback : statusReceivers)
            callback.useCaseConversionFactorResult(result);
    }
}
