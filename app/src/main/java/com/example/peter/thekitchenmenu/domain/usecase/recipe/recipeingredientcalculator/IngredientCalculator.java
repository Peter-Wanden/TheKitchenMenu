package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

/**
 * Calculates the measurement of an ingredient for a single portion of a recipe.
 */
public class IngredientCalculator extends UseCase {

    private static final String TAG = "tkm-" + IngredientCalculator.class.getSimpleName() + ": ";

    public enum ResultStatus {
        QUANTITY_DATA_NOT_AVAILABLE,
        INGREDIENT_DATA_NOT_AVAILABLE,
        PORTIONS_DATA_NOT_AVAILABLE,
        INVALID_CONVERSION_FACTOR,
        INVALID_PORTIONS,
        INVALID_TOTAL_UNIT_ONE,
        INVALID_TOTAL_UNIT_TWO,
        INVALID_MEASUREMENT,
        RESULT_OK
    }
    // TODO -------------------------- USE A RECIPE FOR THE DATA HERE ---------------------- TODO //
    @Nonnull
    private final RepositoryRecipeIngredient recipeIngredientRepository; // TODO - Use the use cases for this data
    @Nonnull
    private final RepositoryRecipePortions portionsRepository; // TODO - Or use a recipe instance
    @Nonnull
    private final RepositoryIngredient ingredientRepository; // TODO - for all three??
    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private TimeProvider timeProvider;

    private UnitOfMeasure unitOfMeasure;
    private boolean conversionFactorChanged;
    private boolean isConversionFactorSet;

    private boolean totalUnitOneChanged;
    private boolean totalUnitTwoChanged;
    private boolean isTotalUnitOneSet;
    private boolean isTotalUnitTwoSet;

    private boolean portionsChanged;
    private boolean isPortionsSet;

    private String recipeId = "";
    private String ingredientId = "";
    private String recipeIngredientId = "";
    private int numberOfPortions;

    private MeasurementModel modelIn;
    private MeasurementModel existingModel;
    private RecipeIngredientEntity quantityEntity;
    private IngredientEntity ingredientEntity;

    public IngredientCalculator(@Nonnull RepositoryRecipePortions portionsRepository,
                                @Nonnull RepositoryRecipeIngredient recipeIngredientRepository,
                                @Nonnull RepositoryIngredient ingredientRepository,
                                @Nonnull UniqueIdProvider idProvider,
                                @Nonnull TimeProvider timeProvider) {
        this.portionsRepository = portionsRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        IngredientCalculatorRequest icr = (IngredientCalculatorRequest) request;

        System.out.println(TAG + icr);
        if (isNewInstantiation()) {
            extractIdsAndStart(icr);
        } else {
            processModel(icr.getModel());
        }
    }

    private boolean isNewInstantiation() {
        return recipeIngredientId.isEmpty();
    }

    private void extractIdsAndStart(IngredientCalculatorRequest request) {
        if (isRecipeIngredientIdProvided(request)) {
            start(request.getRecipeIngredientId());
        } else {
            start(request.getRecipeId(), request.getIngredientId());
        }
    }

    private boolean isRecipeIngredientIdProvided(IngredientCalculatorRequest request) {
        return !request.getRecipeIngredientId().isEmpty();
    }

    private void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        quantityEntity = createNewRecipeIngredientQuantityEntity();
        recipeIngredientId = quantityEntity.getId();
        loadIngredient();
    }

    private RecipeIngredientEntity createNewRecipeIngredientQuantityEntity() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeIngredientEntity(
                idProvider.getUId(),
                recipeId,
                ingredientId,
                "",
                0,
                0,
                Constants.getUserId(),
                currentTime,
                currentTime
        );
    }

    private void start(String recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
        loadRecipeIngredient(recipeIngredientId);
    }

    private void loadRecipeIngredient(String recipeIngredientId) {
        recipeIngredientRepository.getById(
                recipeIngredientId,
                new PrimitiveDataSource.GetEntityCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIngredientEntity entity) {
                        recipeId = entity.getRecipeId();
                        ingredientId = entity.getIngredientId();
                        IngredientCalculator.this.quantityEntity = entity;
                        loadIngredient();
                    }

                    @Override
                    public void onDataUnavailable() {
                        returnDataNotAvailable(ResultStatus.QUANTITY_DATA_NOT_AVAILABLE);
                    }
                });
    }

    private void loadIngredient() {
        ingredientRepository.getById(
                ingredientId,
                new PrimitiveDataSource.GetEntityCallback<IngredientEntity>() {
                    @Override
                    public void onEntityLoaded(IngredientEntity entity) {
                        ingredientId = entity.getId();
                        IngredientCalculator.this.ingredientEntity = entity;
                        loadPortions();
                    }

                    @Override
                    public void onDataUnavailable() {
                        returnDataNotAvailable(ResultStatus.INGREDIENT_DATA_NOT_AVAILABLE);
                    }
                });
    }

    private void loadPortions() {
        portionsRepository.getPortionsForRecipe(
                recipeId,
                new PrimitiveDataSource.GetEntityCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity entity) {
                        numberOfPortions = entity.getServings() *
                                entity.getSittings();
                        setupUnitOfMeasure();
                    }

                    @Override
                    public void onDataUnavailable() {
                        returnDataNotAvailable(ResultStatus.PORTIONS_DATA_NOT_AVAILABLE);
                    }
                });
    }

    private void returnDataNotAvailable(ResultStatus status) {
        IngredientCalculatorResponse response = new IngredientCalculatorResponse(
                UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL,
                status);
        System.out.println(TAG + response);
        getUseCaseCallback().onError(response);
    }

    private void setupUnitOfMeasure() {
        int subtypeAsInt = quantityEntity.getMeasurementSubtype();
        MeasurementSubtype subType = MeasurementSubtype.fromInt(subtypeAsInt);

        unitOfMeasure = subType.getMeasurementClass();
        setConversionFactor();
        setPortions();

        if (unitOfMeasure.isItemBaseUnitsSet(quantityEntity.getItemBaseUnits())) {
            isTotalUnitOneSet = true;
            isTotalUnitTwoSet = true;
        }

        updateExistingModel();
    }

    private void processModel(MeasurementModel modelIn) {
        this.modelIn = modelIn;
        checkForChanges();
    }

    private void checkForChanges() {
        if (isSubtypeChanged()) {
            setupForNewSubtype();

        } else if (isConversionFactorChanged()) {
            processConversionFactor();

        } else if (isTotalUnitOneChanged()) {
            processTotalMeasurementOne();

        } else if (isTotalUnitTwoChanged()) {
            processTotalMeasurementTwo();
        }
        updateExistingModel();
    }

    private boolean isSubtypeChanged() {
        return existingModel.getSubtype() != modelIn.getSubtype();
    }

    private void setupForNewSubtype() {
        unitOfMeasure = modelIn.getSubtype().getMeasurementClass();
        setConversionFactor();
        setPortions();
    }

    private void setConversionFactor() {
        isConversionFactorSet = unitOfMeasure.isConversionFactorSet(
                ingredientEntity.getConversionFactor());
    }

    private void setPortions() {
        isPortionsSet = unitOfMeasure.isNumberOfItemsSet(numberOfPortions);
    }

    private boolean isConversionFactorChanged() {
        return conversionFactorChanged = Double.compare(
                modelIn.getConversionFactor(),
                unitOfMeasure.getConversionFactor())
                != 0;
    }

    private void processConversionFactor() {
        isConversionFactorSet = unitOfMeasure.isConversionFactorSet(
                modelIn.getConversionFactor());

        if (isConversionFactorSet) {
            saveConversionFactorToIngredient();
        }
    }

    private void saveConversionFactorToIngredient() {
        ingredientRepository.save(getUpdatedIngredientEntity());
    }

    private boolean isTotalUnitOneChanged() {
        return totalUnitOneChanged = Double.compare(
                modelIn.getTotalUnitOne(),
                unitOfMeasure.getTotalUnitOne())
                != 0;
    }

    private void processTotalMeasurementOne() {
        isTotalUnitOneSet = unitOfMeasure.isTotalUnitOneSet(modelIn.getTotalUnitOne());
    }

    private boolean isTotalUnitTwoChanged() {
        return totalUnitTwoChanged = modelIn.getTotalUnitTwo() != unitOfMeasure.getTotalUnitTwo();
    }

    private void processTotalMeasurementTwo() {
        isTotalUnitTwoSet = unitOfMeasure.isTotalUnitTwoSet(modelIn.getTotalUnitTwo());
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

    private void updateExistingModel() {
        existingModel = MeasurementModelBuilder.basedOnUnitOfMeasure(unitOfMeasure).
                setConversionFactor(getConversionFactorResult()).
                setTotalUnitOne(getTotalUnitOneResult()).
                setTotalUnitTwo(getTotalUnitTwoResult()).
                build();

        returnResult();
    }

    private void returnResult() {
        saveIfValid();
        IngredientCalculatorResponse response = getResponse();
        resetResults();
        System.out.println(TAG + response);
        getUseCaseCallback().onSuccess(response);
    }

    private IngredientCalculatorResponse getResponse() {
        return new IngredientCalculatorResponse(
                existingModel,
                getResultStatus());
    }

    private double getConversionFactorResult() {
        return conversionFactorChanged && !isConversionFactorSet ?
                modelIn.getConversionFactor() : unitOfMeasure.getConversionFactor();
    }

    private double getTotalUnitOneResult() {
        return totalUnitOneChanged && !isTotalUnitOneSet ?
                modelIn.getTotalUnitOne() : unitOfMeasure.getTotalUnitOne();
    }

    private int getTotalUnitTwoResult() {
        return totalUnitTwoChanged && !isTotalUnitTwoSet ?
                modelIn.getTotalUnitTwo() : unitOfMeasure.getTotalUnitTwo();
    }

    private ResultStatus getResultStatus() {

        if (portionsChanged && !isPortionsSet) {
            return ResultStatus.INVALID_PORTIONS;
        }
        if (conversionFactorChanged && !isConversionFactorSet) {
            return ResultStatus.INVALID_CONVERSION_FACTOR;
        }
        if (totalUnitOneChanged && !isTotalUnitOneSet) {
            return ResultStatus.INVALID_TOTAL_UNIT_ONE;
        }
        if (totalUnitTwoChanged && !isTotalUnitTwoSet) {
            return ResultStatus.INVALID_TOTAL_UNIT_TWO;
        }
        if (!unitOfMeasure.isValidMeasurement()) {
            return ResultStatus.INVALID_MEASUREMENT;
        }
        return ResultStatus.RESULT_OK;
    }

    private void resetResults() {
        portionsChanged = false;
        conversionFactorChanged = false;
        totalUnitOneChanged = false;
        totalUnitTwoChanged = false;
    }

    private void saveIfValid() {
        if (quantityEntityHasChanged() && unitOfMeasure.isValidMeasurement()) {
            save(updatedRecipeIngredientEntity());
        }
    }

    private boolean quantityEntityHasChanged() {
        if (quantityEntity != null) {
            RecipeIngredientEntity updatedEntity = new RecipeIngredientEntity(
                    quantityEntity.getId(),
                    quantityEntity.getRecipeId(),
                    quantityEntity.getIngredientId(),
                    quantityEntity.getProductId(),
                    unitOfMeasure.getItemBaseUnits(),
                    unitOfMeasure.getMeasurementSubtype().asInt(),
                    Constants.getUserId(),
                    quantityEntity.getCreateDate(),
                    quantityEntity.getLastUpdate()
            );
            return !quantityEntity.equals(updatedEntity);

        } else
            return false;
    }

    private RecipeIngredientEntity updatedRecipeIngredientEntity() {
        return new RecipeIngredientEntity(
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

    private void save(RecipeIngredientEntity quantityEntity) {
        this.quantityEntity = quantityEntity;
        recipeIngredientRepository.save(quantityEntity);
    }

    // todo - this should only be accessible through interface
    public String getIngredientId() {
        return ingredientId;
    }
}