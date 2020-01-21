package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import static com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientDuplicateChecker.NO_DUPLICATE_FOUND;

public class Ingredient
        extends UseCase<IngredientRequest, IngredientResponse>
        implements DataSource.GetEntityCallback<IngredientEntity> {

        private static final String TAG = "tkm-" + Ingredient.class.getSimpleName() + ": ";

    public enum Result {
        UNEDITABLE,
        IS_DUPLICATE,
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        VALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_CHANGED
    }

    static final String CREATE_NEW_INGREDIENT = "";

    private RepositoryIngredient repository;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;
    private IngredientDuplicateChecker duplicateNameChecker;

    private boolean isDuplicate;
    private IngredientModel requestModel;
    private IngredientModel responseModel;

    public Ingredient(RepositoryIngredient repository,
                      UniqueIdProvider idProvider,
                      TimeProvider timeProvider,
                      IngredientDuplicateChecker duplicateNameChecker) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.duplicateNameChecker = duplicateNameChecker;
        requestModel = new IngredientModel.Builder().getDefault().build();
        responseModel = new IngredientModel.Builder().getDefault().build();
    }

    @Override
    protected void execute(IngredientRequest request) {
        System.out.println(TAG + "request:" + request);
        requestModel = request.getModel();
        String ingredientId = request.getModel().getIngredientId();

        if (isCreateNew(ingredientId)) {
            requestModel = createNewIngredientModel();
            sendResponse();
        } else if (isEditExisting(ingredientId)) {
            loadData(ingredientId);
        } else {
            checkForChanges();
        }
    }

    private boolean isCreateNew(String ingredientId) {
        return ingredientId.equals(CREATE_NEW_INGREDIENT);
    }

    private boolean isEditExisting(String ingredientId) {
        IngredientModel model = new IngredientModel.Builder().
                getDefault().
                setIngredientId(ingredientId).
                build();

        return this.requestModel.equals(model);
    }

    private void loadData(String ingredientId) {
        repository.getById(ingredientId, this);
    }

    @Override
    public void onEntityLoaded(IngredientEntity entity) {
        requestModel = convertEntityToModel(entity);
        equaliseRequestResponseStates();
        sendResponse();
    }

    private IngredientModel convertEntityToModel(IngredientEntity entity) {
        return new IngredientModel.Builder().
                setIngredientId(entity.getId()).
                setName(entity.getName()).
                setDescription(entity.getDescription()).
                setConversionFactor(entity.getConversionFactor()).
                setCreatedBy(entity.getCreatedBy()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        requestModel = createNewIngredientModel();
        equaliseRequestResponseStates();

        IngredientResponse response = new IngredientResponse(
                Result.DATA_UNAVAILABLE,
                responseModel
        );
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onError(response);
    }

    private IngredientModel createNewIngredientModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        String ingredientId = idProvider.getUId();

        return new IngredientModel.Builder().
                getDefault().
                setIngredientId(ingredientId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void checkForChanges() {
        if (isNameChanged()) {
            checkForDuplicateName();
        } else if (isDescriptionChanged()) {
            saveIfValid();
        }
    }

    private void checkForDuplicateName() {
        duplicateNameChecker.checkForDuplicateAndNotify(
                requestModel.getName(),
                requestModel.getIngredientId(),

                duplicateId -> {
                    isDuplicate = !duplicateId.equals(NO_DUPLICATE_FOUND);
                    saveIfValid();
                });

    }

    private void saveIfValid() {
        if (getResult() == Result.VALID_CHANGED) {
            requestModel = setLasUpdateToCurrentTime();
            save();
        }
        sendResponse();
    }

    private IngredientModel setLasUpdateToCurrentTime() {
        IngredientModel.Builder builder = IngredientModel.Builder.basedOn(requestModel);
        builder.setLastUpdate(timeProvider.getCurrentTimeInMills());
        return builder.build();
    }

    private void save() {
        repository.save(convertModelToEntity(requestModel));
    }

    private IngredientEntity convertModelToEntity(IngredientModel model) {
        return new IngredientEntity(
                model.getIngredientId(),
                model.getName(),
                model.getDescription(),
                model.getConversionFactor(),
                Constants.getUserId(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }

    private void sendResponse() {
        IngredientResponse response = new IngredientResponse(getResult(), requestModel);
        equaliseRequestResponseStates();
        System.out.println(TAG + "response" + response);
        getUseCaseCallback().onSuccess(response);
    }

    private Result getResult() {
        if (!isEditable()) {
            return Result.UNEDITABLE;

        } else if (isDuplicate) {
            return Result.IS_DUPLICATE;

        } else if (isNameChanged() || isDescriptionChanged()) {
            if (isNameValid()) {
                return Result.VALID_CHANGED;
            } else {
                return Result.INVALID_CHANGED;
            }

        } else if (isNameValid()) {
            return Result.VALID_UNCHANGED;
        } else {
            return Result.INVALID_UNCHANGED;
        }
    }

    private boolean isEditable() {
        return Constants.getUserId().equals(responseModel.getUserId());
    }

    private void equaliseRequestResponseStates() {
        responseModel = requestModel;
    }

    private boolean isNameChanged() {
        return !requestModel.getName().equals(responseModel.getName());
    }

    private boolean isNameValid() {
        return !requestModel.getName().isEmpty();
    }

    private boolean isDescriptionChanged() {
        return !requestModel.getDescription().equals(responseModel.getDescription());
    }
}
