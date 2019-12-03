package com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.UseCaseCommandAbstract;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

/**
 * Calculates the measurement of an ingredient for a single portion of a recipe.
 */
public class UseCasePortionCalculator
        extends UseCaseCommandAbstract<UseCasePortionCalculatorRequest, UseCasePortionCalculatorResponse> {

    private static final String TAG = "tkm-" + UseCasePortionCalculator.class.getSimpleName() + " ";

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

    private RepositoryRecipeIngredient recipeIngredientRepository;
    private RepositoryRecipePortions portionsRepository;
    private RepositoryIngredient ingredientRepository;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;

    private UnitOfMeasure unitOfMeasure;
    private boolean conversionFactorChanged;
    private boolean isConversionFactorSet;
    private boolean portionsChanged;
    private boolean isPortionsSet;
    private boolean isItemBaseUnitsSet;
    private boolean totalUnitOneChanged;
    private boolean isTotalUnitOneSet;
    private boolean totalUnitTwoChanged;
    private boolean isTotalUnitTwoSet;

    private String recipeId = "";
    private String ingredientId = "";
    private String recipeIngredientId = "";
    private int numberOfPortions;

    private MeasurementModel modelIn;
    private MeasurementModel existingModel;
    private RecipeIngredientQuantityEntity quantityEntity;
    private IngredientEntity ingredientEntity;

    public UseCasePortionCalculator(RepositoryRecipePortions portionsRepository,
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

    @Override
    protected void execute(UseCasePortionCalculatorRequest request) {
        if (isNewInstantiation()) {
            extractIdsAndStart(request);
        } else {
            processModel(request.getModel());
        }
    }

    private boolean isNewInstantiation() {
        return this.recipeIngredientId.isEmpty();
    }

    private void extractIdsAndStart(UseCasePortionCalculatorRequest request) {
        if (isRecipeIngredientIdProvided(request)) {
            start(request.getRecipeIngredientId());
        } else {
            start(request.getRecipeId(), request.getIngredientId());
        }
    }

    private boolean isRecipeIngredientIdProvided(UseCasePortionCalculatorRequest request) {
        return !request.getRecipeIngredientId().isEmpty();
    }

    private void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        quantityEntity = createNewRecipeIngredientQuantityEntity();
        recipeIngredientId = quantityEntity.getId();
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

    private void start(String recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
        loadRecipeIngredient(recipeIngredientId);
    }

    private void loadRecipeIngredient(String recipeIngredientId) {
        recipeIngredientRepository.getById(
                recipeIngredientId,
                new DataSource.GetEntityCallback<RecipeIngredientQuantityEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIngredientQuantityEntity quantityEntity) {
                        recipeId = quantityEntity.getRecipeId();
                        ingredientId = quantityEntity.getIngredientId();
                        UseCasePortionCalculator.this.quantityEntity = quantityEntity;
                        loadIngredient();
                    }

                    @Override
                    public void onDataNotAvailable() {
                        returnDataNotAvailable(ResultStatus.QUANTITY_DATA_NOT_AVAILABLE);
                    }
                });
    }

    private void loadIngredient() {
        ingredientRepository.getById(
                ingredientId,
                new DataSource.GetEntityCallback<IngredientEntity>() {
                    @Override
                    public void onEntityLoaded(IngredientEntity ingredientEntity) {
                        UseCasePortionCalculator.this.ingredientEntity = ingredientEntity;
                        loadPortions();
                    }

                    @Override
                    public void onDataNotAvailable() {
                        returnDataNotAvailable(ResultStatus.INGREDIENT_DATA_NOT_AVAILABLE);
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
                        returnDataNotAvailable(ResultStatus.PORTIONS_DATA_NOT_AVAILABLE);
                    }
                });
    }

    private void returnDataNotAvailable(ResultStatus status) {
        UseCasePortionCalculatorResponse response = new UseCasePortionCalculatorResponse(
                UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL,
                status);

        getUseCaseCallback().onError(response);
    }

    private void setupUnitOfMeasure() {

        int subtypeAsInt = quantityEntity.getUnitOfMeasureSubtype();
        MeasurementSubtype subType = MeasurementSubtype.fromInt(subtypeAsInt);

        unitOfMeasure = subType.getMeasurementClass();
        setConversionFactor();
        setPortions();

        if (quantityEntity.getItemBaseUnits() > 0) {
            isItemBaseUnitsSet = unitOfMeasure.isItemBaseUnitsSet(
                    quantityEntity.getItemBaseUnits());
        }

        if (isItemBaseUnitsSet) {
            isTotalUnitOneSet = true;
            isTotalUnitTwoSet = true;
        }

        updateExistingModel();
    }

    private void processModel(MeasurementModel modelIn) {
        System.out.println(TAG + "modelIn=" + modelIn);
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
        return modelIn.getConversionFactor() != unitOfMeasure.getConversionFactor();
    }

    private void processConversionFactor() {
        conversionFactorChanged = true;
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
        return modelIn.getTotalUnitOne() != unitOfMeasure.getTotalUnitOne();
    }

    private void processTotalMeasurementOne() {
        totalUnitOneChanged = true;
        isTotalUnitOneSet = unitOfMeasure.isTotalUnitOneSet(modelIn.getTotalUnitOne());
    }

    private boolean isTotalUnitTwoChanged() {
        return modelIn.getTotalUnitTwo() != unitOfMeasure.getTotalUnitTwo();
    }

    private void processTotalMeasurementTwo() {
        totalUnitTwoChanged = true;
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

        UseCasePortionCalculatorResponse response = getResponse();

        System.out.println(TAG + "response=" + response);

        resetResults();

        getUseCaseCallback().onSuccess(response);

    }

    private UseCasePortionCalculatorResponse getResponse() {
        return new UseCasePortionCalculatorResponse(
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
        if (totalUnitOneChanged && !isTotalUnitOneSet && unitOfMeasure.isValidMeasurement()) {
            return ResultStatus.INVALID_TOTAL_UNIT_ONE;
        }
        if (totalUnitTwoChanged && !isTotalUnitTwoSet && unitOfMeasure.isValidMeasurement()) {
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

    // todo - this should only be accessible through interface
    public String getIngredientId() {
        return ingredientId;
    }
}