package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseConversionFactorStatus.UseCaseConversionFactorResult.*;

public class UseCaseConversionFactorStatus
        implements DataSource.GetEntityCallback<IngredientEntity> {

    public enum UseCaseConversionFactorResult {
        DISABLED,
        ENABLED_UNEDITABLE,
        ENABLED_EDITABLE_UNSET,
        ENABLED_EDITABLE_SET
    }

    private UseCaseConversionFactorViewModel viewModel;
    private RepositoryIngredient repository;
    private boolean isConversionFactorEnabled;
    private IngredientEntity ingredientEntity;

    public UseCaseConversionFactorStatus(RepositoryIngredient repository) {
        this.repository = repository;
    }

    public void setViewModel(UseCaseConversionFactorViewModel viewModel) {
        this.viewModel = viewModel;
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

    }

    private void getResult() {
        if (isConversionFactorEnabled) {
            if (userCanEditIngredient()) {
                if (conversionFactorSet()) {
                    viewModel.useCaseConversionFactorResult(ENABLED_EDITABLE_SET);
                } else {
                    viewModel.useCaseConversionFactorResult(ENABLED_EDITABLE_UNSET);
                }
            } else {
                viewModel.useCaseConversionFactorResult(ENABLED_UNEDITABLE);
            }
        } else {
            viewModel.useCaseConversionFactorResult(DISABLED);
        }
    }

    private boolean userCanEditIngredient() {
        return Constants.getUserId().getValue().equals(ingredientEntity.getCreatedBy());
    }

    private boolean conversionFactorSet() {
        return ingredientEntity.getConversionFactor() != UnitOfMeasureConstants.NO_CONVERSION_FACTOR;
    }
}
