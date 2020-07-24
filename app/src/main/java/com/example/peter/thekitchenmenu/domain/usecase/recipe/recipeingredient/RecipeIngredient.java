package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.ingredient.DataAccessIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Calculates the measurement of an ingredient for a single portion of a recipe.
 */
public class RecipeIngredient extends UseCaseBase {

    private static final String TAG = "tkm-" + RecipeIngredient.class.getSimpleName() + ": ";

    public enum Result {
        QUANTITY_DATA_UNAVAILABLE,
        INGREDIENT_DATA_UNAVAILABLE,
        PORTIONS_DATA_UNAVAILABLE,
        INVALID_CONVERSION_FACTOR,
        INVALID_PORTIONS,
        INVALID_TOTAL_UNIT_ONE,
        INVALID_TOTAL_UNIT_TWO,
        INVALID_MEASUREMENT,
        RESULT_OK
    }

    public enum FailReason implements FailReasons {
        QUANTITY_DATA_UNAVAILABLE(450),
        INGREDIENT_DATA_UNAVAILABLE(451),
        PORTIONS_DATA_UNAVAILABLE(452),
        INVALID_CONVERSION_FACTOR(453),
        INVALID_PORTIONS(454),
        INVALID_TOTAL_UNIT_ONE(455),
        INVALID_TOTAL_UNIT_TWO(456),
        INVALID_MEASUREMENT(457);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason failReason : FailReason.values())
                options.put(failReason.id, failReason);
        }

        public static FailReason getById(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }


    // TODO -------------------------- USE A RECIPE FOR THE DATA HERE ---------------------- TODO //
    @Nonnull
    private final DataAccessRecipeIngredient recipeIngredientRepository;

    private DataAccessRecipePortions portionsRepository; // TODO - Or use a recipe instance
    @Nonnull
    private final DataAccessIngredient ingredientRepository; // TODO - for all three??
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
    private int numberOfPortions;

    private boolean portionsChanged;
    private boolean isPortionsSet;

    private String dataId = "";
    private String recipeId = "";
    private String ingredientId = "";
    private String recipeIngredientId = "";

    private MeasurementModel modelIn;
    private MeasurementModel existingModel;
    private RecipeIngredientPersistenceModel persistenceModel;
    private IngredientEntity ingredientEntity;

    public RecipeIngredient(@Nonnull DataAccessRecipePortions portionsRepository,
                            @Nonnull DataAccessRecipeIngredient recipeIngredientRepository,
                            @Nonnull DataAccessIngredient ingredientRepository,
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
        RecipeIngredientRequest r = (RecipeIngredientRequest) request;

        System.out.println(TAG + r);
        if (isNewInstantiation()) {
            extractIdsAndStart(r);
        } else {
            setupUseCase();
            processModel(r.getModel());
        }
    }

    private boolean isNewInstantiation() {
        return recipeIngredientId.isEmpty();
    }

    private void extractIdsAndStart(RecipeIngredientRequest request) {
        if (isRecipeIngredientIdProvided(request)) {
            start(request.getRecipeIngredientId());
        } else {
            start(request.getRecipeId(), request.getIngredientId());
        }
    }

    private boolean isRecipeIngredientIdProvided(RecipeIngredientRequest request) {
        return !request.getRecipeIngredientId().isEmpty();
    }

    private void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        persistenceModel = createNewPersistenceModel();
        recipeIngredientId = persistenceModel.getDataId();
        loadIngredient();
    }

    private RecipeIngredientPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeIngredientPersistenceModel.Builder().getDefault().build();
    }

    private void start(String recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
        loadRecipeIngredient(recipeIngredientId);
    }

    private void loadRecipeIngredient(String recipeIngredientId) {
//        recipeIngredientRepository.getByDataId(
//                recipeIngredientId,
//                new PrimitiveDataSource.GetEntityCallback<RecipeIngredientEntity>() {
//                    @Override
//                    public void onEntityLoaded(RecipeIngredientEntity entity) {
//                        recipeId = entity.getRecipeId();
//                        ingredientId = entity.getIngredientId();
//                         RecipeIngredient.this.persistenceModel = entity;
//                        loadIngredient();
//                    }
//
//                    @Override
//                    public void onDataUnavailable() {
//                        returnDataUnAvailable(Result.QUANTITY_DATA_UNAVAILABLE);
//                    }
//                });
    }

    private void loadIngredient() {
//        ingredientRepository.getByDataId(
//                ingredientId,
//                new PrimitiveDataSource.GetEntityCallback<IngredientEntity>() {
//                    @Override
//                    public void onEntityLoaded(IngredientEntity entity) {
//                        ingredientId = entity.getDataId();
//                        RecipeIngredient.this.ingredientEntity = entity;
//                        loadPortions();
//                    }
//
//                    @Override
//                    public void onDataUnavailable() {
//                        returnDataUnAvailable(Result.INGREDIENT_DATA_UNAVAILABLE);
//                    }
//                });
    }

    private void loadPortions() {
        portionsRepository.getByDomainId(
                recipeId,
                new GetDomainModelCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipePortionsPersistenceModel model) {
                        numberOfPortions = model.getServings() *
                                model.getSittings();
                        setupUnitOfMeasure();
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        returnDataUnAvailable(Result.PORTIONS_DATA_UNAVAILABLE);
                    }
                }
        );
    }

    private void returnDataUnAvailable(Result status) {
        RecipeIngredientResponse response = new RecipeIngredientResponse(
                UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL,
                status);
        System.out.println(TAG + response);
        getUseCaseCallback().onUseCaseError(response);
    }

    private void setupUnitOfMeasure() {
//        int subtypeAsInt = persistenceModel.getMeasurementSubtype();
//        MeasurementSubtype subType = MeasurementSubtype.fromInt(subtypeAsInt);
//
//        unitOfMeasure = subType.getMeasurementClass();
//        setConversionFactor();
//        setPortions();
//
//        if (unitOfMeasure.isItemBaseUnitsSet(persistenceModel.getItemBaseUnits())) {
//            isTotalUnitOneSet = true;
//            isTotalUnitTwoSet = true;
//        }
//
//        updateExistingModel();
    }

    private void setupUseCase() {
        portionsChanged = false;
        conversionFactorChanged = false;
        totalUnitOneChanged = false;
        totalUnitTwoChanged = false;
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
//        ingredientRepository.save(getUpdatedIngredientEntity());
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
        RecipeIngredientResponse response = getResponse();
        System.out.println(TAG + response);
        getUseCaseCallback().onUseCaseSuccess(response);
    }

    private RecipeIngredientResponse getResponse() {
        return new RecipeIngredientResponse(
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

    private Result getResultStatus() {

        if (portionsChanged && !isPortionsSet) {
            return Result.INVALID_PORTIONS;
        }
        if (conversionFactorChanged && !isConversionFactorSet) {
            return Result.INVALID_CONVERSION_FACTOR;
        }
        if (totalUnitOneChanged && !isTotalUnitOneSet) {
            return Result.INVALID_TOTAL_UNIT_ONE;
        }
        if (totalUnitTwoChanged && !isTotalUnitTwoSet) {
            return Result.INVALID_TOTAL_UNIT_TWO;
        }
        if (!unitOfMeasure.isValidMeasurement()) {
            return Result.INVALID_MEASUREMENT;
        }
        return Result.RESULT_OK;
    }


    private void saveIfValid() {
        if (quantityEntityHasChanged() && unitOfMeasure.isValidMeasurement()) {
//            save(updatedRecipeIngredientEntity());
        }
    }

    private boolean quantityEntityHasChanged() {
//        if (persistenceModel != null) {
//            RecipeIngredientEntity updatedEntity = new RecipeIngredientEntity(
//                    persistenceModel.getDataId(),
//                    persistenceModel.getRecipeId(),
//                    persistenceModel.getIngredientId(),
//                    persistenceModel.getProductId(),
//                    unitOfMeasure.getItemBaseUnits(),
//                    unitOfMeasure.getMeasurementSubtype().asInt(),
//                    Constants.getUserId(),
//                    persistenceModel.getCreateDate(),
//                    persistenceModel.getLastUpdate()
//            );
//            return !persistenceModel.equals(updatedEntity);
//
//        } else
//            return false;
        return false;
    }

    // todo - this should only be accessible through interface
    public String getIngredientId() {
        return ingredientId;
    }
}