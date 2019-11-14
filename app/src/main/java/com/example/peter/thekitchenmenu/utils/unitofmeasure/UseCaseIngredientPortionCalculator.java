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

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates the measurement of an ingredient for a single portion of a recipe.
 */
public class UseCaseIngredientPortionCalculator {

    public interface UseCasePortionCallback {
        void useCasePortionResult(MeasurementResult result);
        void dataLoadingFailed(FailReason reason);
    }

    public enum ResultStatus {
        INVALID_CONVERSION_FACTOR,
        INVALID_PORTIONS,
        INVALID_TOTAL_MEASUREMENT_ONE,
        INVALID_TOTAL_MEASUREMENT_TWO,
        INVALID_MEASUREMENT,
        RESULT_OK
    }

    public enum FailReason {
        NO_DATA_AVAILABLE,
        DATA_LOADING_ERROR
    }

    private static final String TAG = "tkm-PortionsUseCase";

    private RepositoryRecipeIngredient recipeIngredientRepository;
    private RepositoryRecipePortions portionsRepository;
    private RepositoryIngredient ingredientRepository;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;
    private List<UseCasePortionCallback> listeners = new ArrayList<>();

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


    public UseCaseIngredientPortionCalculator(RepositoryRecipePortions portionsRepository,
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

    public void registerListener(UseCasePortionCallback listener) {
        listeners.add(listener);
    }

    public void unregisterListener(UseCasePortionCallback listener) {
        listeners.remove(listener);
    }

    public void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        quantityEntity = createNewRecipeIngredientQuantityEntity();
        loadIngredient();
    }

    private RecipeIngredientQuantityEntity createNewRecipeIngredientQuantityEntity() {
        long currentTime = timeProvider.getCurrentTimeInMills();
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
                        UseCaseIngredientPortionCalculator.this.quantityEntity = quantityEntity;
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
                        UseCaseIngredientPortionCalculator.this.ingredientEntity = ingredientEntity;
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

        conversionFactorIsSet = unitOfMeasure.isConversionFactorSet(
                ingredientEntity.getConversionFactor());

        portionsAreSet = unitOfMeasure.isNumberOfItemsSet(numberOfPortions);

        if (quantityEntity.getItemBaseUnits() > 0) {
            itemBaseUnitsAreSet = unitOfMeasure.isItemBaseUnitsSet(
                    quantityEntity.getItemBaseUnits());
        }

        if (itemBaseUnitsAreSet) {
            totalMeasurementOneIsSet = true;
            totalMeasurementTwoIsSet = true;
        }

        returnResult();
    }

    public void processModel(MeasurementModel modelIn) {
        this.modelIn = modelIn;
        checkForChanges();
    }

    private void checkForChanges() {
        if (isSubtypeChanged()) {
            setupForNewSubtype();

        } else if (isConversionFactorChanged()) {
            processConversionFactor();

        } else if (isTotalMeasurementOneChanged()) {
            processTotalMeasurementOne();

        } else if (isTotalMeasurementTwoChanged()) {
            processTotalMeasurementTwo();
        }
        returnResult();
    }

    private boolean isSubtypeChanged() {
        return existingModel.getSubtype() != modelIn.getSubtype();
    }

    private void setupForNewSubtype() {
        unitOfMeasure = modelIn.getSubtype().getMeasurementClass();
        conversionFactorIsSet = unitOfMeasure.isConversionFactorSet(
                ingredientEntity.getConversionFactor());
        portionsAreSet = unitOfMeasure.isNumberOfItemsSet(numberOfPortions);
    }

    private boolean isConversionFactorChanged() {
        return modelIn.getConversionFactor() != unitOfMeasure.getConversionFactor();
    }

    private void processConversionFactor() {
        conversionFactorChanged = true;
        conversionFactorIsSet = unitOfMeasure.isConversionFactorSet(
                modelIn.getConversionFactor());

        if (conversionFactorIsSet) {
            saveConversionFactorToIngredient();
        }
    }

    private void saveConversionFactorToIngredient() {
        ingredientRepository.save(getUpdatedIngredientEntity());
    }

    private boolean isTotalMeasurementOneChanged() {
        return modelIn.getTotalMeasurementOne() != unitOfMeasure.getTotalUnitOne();
    }

    private void processTotalMeasurementOne() {
        totalMeasurementOneChanged = true;
        totalMeasurementOneIsSet = unitOfMeasure.isTotalUnitOneSet(
                modelIn.getTotalMeasurementOne());
    }

    private boolean isTotalMeasurementTwoChanged() {
        return modelIn.getTotalMeasurementTwo() != unitOfMeasure.getTotalUnitTwo();
    }

    private void processTotalMeasurementTwo() {
        totalMeasurementTwoChanged = true;
        totalMeasurementTwoIsSet = unitOfMeasure.isTotalUnitTwoSet(
                modelIn.getTotalMeasurementTwo());
    }

    private IngredientEntity getUpdatedIngredientEntity() {
        return new IngredientEntity(
                ingredientEntity.getId(),
                ingredientEntity.getName(),
                ingredientEntity.getDescription(),
                unitOfMeasure.getConversionFactor(),
                ingredientEntity.getCreatedBy(),
                ingredientEntity.getCreateDate(),
                timeProvider.getCurrentTimeInMills()
        );
    }

    private void returnResult() {
        existingModel = new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                getConversionFactorResult(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                getTotalMeasurementOneResult(),
                unitOfMeasure.getItemUnitOne(),
                getTotalMeasurementTwoResult(),
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );

        MeasurementResult result = new MeasurementResult(existingModel, getResultStatus());
        saveIfValid();
        notifyListeners(result);
    }

    private double getConversionFactorResult() {
        return conversionFactorChanged && !conversionFactorIsSet ?
                modelIn.getConversionFactor() : unitOfMeasure.getConversionFactor();
    }

    private double getTotalMeasurementOneResult() {
        return totalMeasurementOneChanged && !totalMeasurementOneIsSet ?
                modelIn.getTotalMeasurementOne() : unitOfMeasure.getTotalUnitOne();
    }

    private int getTotalMeasurementTwoResult() {
        return totalMeasurementTwoChanged && !totalMeasurementTwoIsSet ?
                modelIn.getTotalMeasurementTwo() : unitOfMeasure.getTotalUnitTwo();
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

    private void notifyListeners(MeasurementResult result) {
        for (UseCasePortionCallback callback : listeners) {
            callback.useCasePortionResult(result);
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
                timeProvider.getCurrentTimeInMills()
        );
    }

    private void save(RecipeIngredientQuantityEntity quantityEntity) {
        this.quantityEntity = quantityEntity;
        recipeIngredientRepository.save(quantityEntity);
    }

    public String getIngredientId() {
        return ingredientId;
    }
}