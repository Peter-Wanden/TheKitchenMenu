package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult.*;

/**
 * Calculates the measurement of an ingredient for a single portion of a recipe.
 */
public class UnitOfMeasurePortionUseCase {

    private static final String TAG = "tkm-PortionsUseCase";

    private RepositoryRecipeIngredient recipeIngredientRepository;
    private RepositoryRecipePortions portionsRepository;
    private RepositoryIngredient ingredientRepository;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;
    private PortionUseCaseViewModel viewModel;

    private UnitOfMeasure unitOfMeasure = MeasurementSubtype.METRIC_MASS.getMeasurementClass();
    private boolean conversionFactorChanged;
    private boolean conversionFactorIsSet;
    private boolean portionsChanged;
    private boolean portionsAreSet;
    private boolean itemBaseUnitsAreSet;
    private boolean totalMeasurementOneChanged;
    private boolean totalMeasurementOneIsSet;
    private boolean totalMeasurementTwoChanged;
    private boolean totalMeasurementTwoIsSet;

    private String recipeId;
    private String ingredientId;
    private int numberOfPortions;

    private MeasurementModel modelIn;
    private MeasurementModel existingModel;
    private RecipeIngredientQuantityEntity quantityEntity;
    private IngredientEntity ingredientEntity;


    public UnitOfMeasurePortionUseCase(RepositoryRecipePortions portionsRepository,
                                       RepositoryRecipeIngredient recipeIngredientRepository,
                                       RepositoryIngredient ingredientRepository,
                                       UniqueIdProvider idProvider,
                                       TimeProvider timeProvider) {
        this.portionsRepository = portionsRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
    }

    public void setViewModel(PortionUseCaseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        quantityEntity = createNewRecipeIngredientQuantityEntity();
        loadIngredient();
    }

    private RecipeIngredientQuantityEntity createNewRecipeIngredientQuantityEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeIngredientQuantityEntity(
                idProvider.getUId(),
                recipeId,
                ingredientId,
                "",
                0,
                0,
                Constants.getUserId().getValue(),
                currentTime,
                currentTime
        );
    }

    public void start(String recipeIngredientId) {
        loadExistingQuantityEntity(recipeIngredientId);
    }

    private void loadExistingQuantityEntity(String recipeIngredientId) {
        recipeIngredientRepository.getById(
                recipeIngredientId,
                new DataSource.GetEntityCallback<RecipeIngredientQuantityEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIngredientQuantityEntity quantityEntity) {
                        recipeId = quantityEntity.getRecipeId();
                        ingredientId = quantityEntity.getIngredientId();
                        UnitOfMeasurePortionUseCase.this.quantityEntity = quantityEntity;
                        loadIngredient();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void loadIngredient() {
        ingredientRepository.getById(
                ingredientId,
                new DataSource.GetEntityCallback<IngredientEntity>() {
                    @Override
                    public void onEntityLoaded(IngredientEntity ingredientEntity) {
                        UnitOfMeasurePortionUseCase.this.ingredientEntity = ingredientEntity;
                        loadPortions();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void loadPortions() {
        portionsRepository.getPortionsForRecipe(
                recipeId,
                new DataSource.GetEntityCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity portionsEntity) {
                        numberOfPortions = portionsEntity.getServings() *
                                portionsEntity.getSittings();
                        setupUnitOfMeasure();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void setupUnitOfMeasure() {
        unitOfMeasure = MeasurementSubtype.fromInt(
                quantityEntity.getUnitOfMeasureSubtype()).getMeasurementClass();

        conversionFactorIsSet = unitOfMeasure.conversionFactorIsSet(
                ingredientEntity.getConversionFactor());

        portionsAreSet = unitOfMeasure.numberOfItemsIsSet(numberOfPortions);

        if (quantityEntity.getItemBaseUnits() > 0) {
            itemBaseUnitsAreSet = unitOfMeasure.itemBaseUnitsAreSet(
                    quantityEntity.getItemBaseUnits());
        }

        if (itemBaseUnitsAreSet) {
            totalMeasurementOneIsSet = true;
            totalMeasurementTwoIsSet = true;
        }

        returnResult();
    }

    public void modelIn(MeasurementModel modelIn) {
        this.modelIn = modelIn;
        checkForChanges();
    }

    private void checkForChanges() {
        if (existingModel.getSubtype() != modelIn.getSubtype()) {
            unitOfMeasure = modelIn.getSubtype().getMeasurementClass();
            conversionFactorIsSet = unitOfMeasure.conversionFactorIsSet(
                    ingredientEntity.getConversionFactor());
            portionsAreSet = unitOfMeasure.numberOfItemsIsSet(numberOfPortions);

        } else if (modelIn.getConversionFactor() != unitOfMeasure.getConversionFactor()) {
            conversionFactorChanged = true;
            conversionFactorIsSet = unitOfMeasure.conversionFactorIsSet(
                    modelIn.getConversionFactor());

            if (conversionFactorIsSet)
                saveConversionFactorToIngredient();

        } else if (modelIn.getTotalMeasurementOne() != unitOfMeasure.getTotalMeasurementOne()) {
            totalMeasurementOneChanged = true;
            totalMeasurementOneIsSet = unitOfMeasure.totalMeasurementOneIsSet(
                    modelIn.getTotalMeasurementOne());

        } else if (modelIn.getTotalMeasurementTwo() != unitOfMeasure.getTotalMeasurementTwo()) {
            totalMeasurementTwoChanged = true;
            totalMeasurementTwoIsSet = unitOfMeasure.totalMeasurementTwoIsSet(
                    modelIn.getTotalMeasurementTwo());
        }
        returnResult();
    }

    private void saveConversionFactorToIngredient() {
        ingredientRepository.save(getUpdatedIngredientEntity());
    }

    private IngredientEntity getUpdatedIngredientEntity() {
        return new IngredientEntity(
                ingredientEntity.getId(),
                ingredientEntity.getName(),
                ingredientEntity.getDescription(),
                unitOfMeasure.getConversionFactor(),
                ingredientEntity.getCreatedBy(),
                ingredientEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private void returnResult() {
        existingModel = new MeasurementModel(
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getTotalMeasurementOne(),
                unitOfMeasure.getTotalMeasurementTwo(),
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.getItemBaseUnits()
        );

        MeasurementResult result = new MeasurementResult(existingModel, getResultStatus());

        saveIfValid();
        viewModel.modelOut(result);
    }

    private ResultStatus getResultStatus() {
        if (portionsChanged && !portionsAreSet) {
            portionsChanged = false;
            return ResultStatus.INVALID_PORTIONS;
        }
        if (conversionFactorChanged && !conversionFactorIsSet) {
            conversionFactorChanged = false;
            return ResultStatus.INVALID_CONVERSION_FACTOR;
        }
        if (totalMeasurementOneChanged && !totalMeasurementOneIsSet) {
            totalMeasurementOneChanged = false;
            return ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE;
        }
        if (totalMeasurementTwoChanged && !totalMeasurementTwoIsSet) {
            totalMeasurementTwoChanged = false;
            return ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO;
        }
        if (!unitOfMeasure.isValidMeasurement()) {
            return ResultStatus.INVALID_MEASUREMENT;
        }
        return ResultStatus.RESULT_OK;
    }

    private void saveIfValid() {
        if (quantityEntityHasChanged() && unitOfMeasure.isValidMeasurement()) {
            save(updatedRecipeIngredientEntity());
        }
    }

    private boolean quantityEntityHasChanged() {
        if (quantityEntity != null) {
            RecipeIngredientQuantityEntity updatedEntity = new RecipeIngredientQuantityEntity(
                    quantityEntity.getId(),
                    quantityEntity.getRecipeId(),
                    quantityEntity.getIngredientId(),
                    quantityEntity.getProductId(),
                    unitOfMeasure.getItemBaseUnits(),
                    unitOfMeasure.getMeasurementSubtype().asInt(),
                    Constants.getUserId().getValue(),
                    quantityEntity.getCreateDate(),
                    quantityEntity.getLastUpdate()
            );
            return !quantityEntity.equals(updatedEntity);

        } else
            return false;
    }

    private RecipeIngredientQuantityEntity updatedRecipeIngredientEntity() {
        return new RecipeIngredientQuantityEntity(
                quantityEntity.getId(),
                quantityEntity.getRecipeId(),
                quantityEntity.getIngredientId(),
                quantityEntity.getProductId(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getMeasurementSubtype().asInt(),
                quantityEntity.getCreatedBy(),
                quantityEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private void save(RecipeIngredientQuantityEntity quantityEntity) {
        this.quantityEntity = quantityEntity;
        recipeIngredientRepository.save(quantityEntity);
    }
}
