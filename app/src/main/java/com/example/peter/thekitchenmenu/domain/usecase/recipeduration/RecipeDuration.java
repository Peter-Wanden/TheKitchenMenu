package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeDuration
        extends UseCase<RecipeDurationRequest, RecipeDurationResponse>
        implements DataSource.GetEntityCallback<RecipeDurationEntity> {

    private static final String TAG = "tkm-" + RecipeDuration.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
        DATA_UNAVAILABLE,
        INVALID_PREP_TIME,
        INVALID_COOK_TIME,
        NONE
    }

    private final int MAX_PREP_TIME;
    private final int MAX_COOK_TIME;

    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final RepositoryRecipeDuration repository;

    private String recipeId = "";
    private boolean isCloned;
    private boolean isChanged;
    private boolean isNewRequest;
    private boolean isValid;
    private final List<FailReasons> failReasons;

    private RecipeDurationPersistenceModel persistenceModel;
    private RecipeDurationRequest.Model requestModel;

    public RecipeDuration(@Nonnull RepositoryRecipeDuration repository,
                          @Nonnull TimeProvider timeProvider,
                          int maxPrepTime,
                          int maxCookTime) {
        this.repository = repository;
        this.timeProvider = timeProvider;

        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;

        failReasons = new ArrayList<>();

        requestModel = RecipeDurationRequest.Model.Builder.getDefault().build();
    }

    @Override
    protected void execute(RecipeDurationRequest request) {
        System.out.println(TAG + request);
        requestModel = request.getModel();

        if (isNewRequest(request.getRecipeId())) {
            System.out.println(TAG + "isNewRequest");
            extractIds(request);
        } else {
            processChanges();
        }
    }

    private boolean isNewRequest(String recipeId) {
        return isNewRequest = !this.recipeId.equals(recipeId);
    }

    private void extractIds(RecipeDurationRequest request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        loadData(request.getRecipeId());
    }

    private boolean isCloneRequest(RecipeDurationRequest request) {
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    private void loadData(String recipeId) {
        repository.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeDurationEntity entity) {
        isChanged = false;
        persistenceModel = convertEntityToPersistenceModel(entity);
        validateData();

        if (isCloned) {
            save(persistenceModel);
            isCloned = false;
        }
        buildResponse();
    }

    private RecipeDurationPersistenceModel convertEntityToPersistenceModel(RecipeDurationEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeDurationPersistenceModel.Builder().
                setId(isCloned ? recipeId : entity.getId()).
                setPrepTime(entity.getPrepTime()).
                setCookTime(entity.getCookTime()).
                setCreateDate(isCloned ? currentTime : entity.getCreateDate()).
                setLastUpdate(isCloned ? currentTime : entity.getLastUpdate()).build();
    }

    @Override
    public void onDataNotAvailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(FailReason.DATA_UNAVAILABLE);
        buildResponse();
    }

    private RecipeDurationPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return RecipeDurationPersistenceModel.Builder.getDefault().
                setId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void processChanges() {
        int prepTime = getTotalMinutes(requestModel.getPrepHours(), requestModel.getPrepMinutes());
        int cookTime = getTotalMinutes(requestModel.getCookHours(), requestModel.getCookMinutes());

        if (prepTime != persistenceModel.getPrepTime()) {
            isChanged = true;
        }
        if (cookTime != persistenceModel.getCookTime()) {
            isChanged = true;
        }
        System.out.println(TAG + "processingChanges: isChanged:" + isChanged);
        validateData();
        buildResponse();
    }

    private void validateData() {
        int prepTime;
        int cookTime;
        if (isNewRequest) {
            prepTime = persistenceModel.getPrepTime();
            cookTime = persistenceModel.getCookTime();
        } else {
            prepTime = getTotalMinutes(requestModel.getPrepHours(), requestModel.getPrepMinutes());
            cookTime = getTotalMinutes(requestModel.getCookHours(), requestModel.getCookMinutes());
        }

        boolean isPrepTimeValid = prepTime <= MAX_PREP_TIME;
        boolean isCookTimeValid = cookTime <= MAX_COOK_TIME;
        if (!isPrepTimeValid) {
            failReasons.add(FailReason.INVALID_PREP_TIME);
        }
        if (!isCookTimeValid) {
            failReasons.add(FailReason.INVALID_COOK_TIME);
        }

        isValid = isPrepTimeValid && isCookTimeValid;

        System.out.println(TAG + "validatingData:" +
                " isPrepTimeValid:" + isPrepTimeValid +
                " isCookTimeValid:" + isCookTimeValid +
                " isValid:" + isValid);
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }

    private int getTotalMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void buildResponse() {

        RecipeDurationResponse.Builder responseBuilder = new RecipeDurationResponse.Builder().
                setFailReasons(getFailReasons()
                );

        if (getComponentState() == ComponentState.VALID_CHANGED) {
            saveUpdatedModel();
        }

        responseBuilder.
                setState(getComponentState()).
                setModel(getResponseModel());

        System.out.println(TAG + "buildingResponse:" +
                " componentState:" + getComponentState());

        sendResponse(responseBuilder.build());
    }

    private ComponentState getComponentState() {
        if (failReasons.contains(FailReason.DATA_UNAVAILABLE)) {
            return ComponentState.DATA_UNAVAILABLE;

        } else if (!isValid && !isChanged) {
            return ComponentState.INVALID_UNCHANGED;

        } else if (isValid && !isChanged) {
            return ComponentState.VALID_UNCHANGED;

        } else if (!isValid && isChanged) {
            return ComponentState.INVALID_CHANGED;

        } else {
            return ComponentState.VALID_CHANGED;
        }
    }

    private List<FailReasons> getFailReasons() {
        if (failReasons.isEmpty()) {
            failReasons.add(FailReason.NONE);
        }
        return new ArrayList<>(failReasons);
    }

    private RecipeDurationResponse.Model getResponseModel() {
        return new RecipeDurationResponse.Model.Builder().
                setPrepHours(isNewRequest ?
                        getHours(persistenceModel.getPrepTime()) :
                        requestModel.getPrepHours()).

                setPrepMinutes(isNewRequest ?
                        getMinutes(persistenceModel.getPrepTime()) :
                        requestModel.getPrepMinutes()).

                setTotalPrepTime(isNewRequest ?
                        persistenceModel.getPrepTime() :
                        getTotalMinutes(requestModel.getPrepHours(), requestModel.getPrepMinutes())).

                setCookHours(isNewRequest ?
                        getHours(persistenceModel.getCookTime()) :
                        requestModel.getCookHours()).

                setCookMinutes(isNewRequest ?
                        getMinutes(persistenceModel.getCookTime()) :
                        requestModel.getCookMinutes()).

                setTotalCookTime(isNewRequest ?
                        persistenceModel.getCookTime() :
                        getTotalMinutes(requestModel.getCookHours(), requestModel.getCookMinutes())).

                setTotalTime(isNewRequest ?
                        persistenceModel.getPrepTime() + persistenceModel.getCookTime() :
                        getTotalMinutes(
                                requestModel.getPrepHours(),
                                requestModel.getPrepMinutes()) +
                                getTotalMinutes(
                                        requestModel.getCookHours(),
                                        requestModel.getPrepMinutes())).

                setCreateDate(persistenceModel.getCreateDate()).
                setLastUpdate(persistenceModel.getLastUpdate()).
                build();
    }

    private void sendResponse(RecipeDurationResponse response) {
        System.out.println(TAG + response);
        resetState();

        if (response.getFailReasons().contains(FailReason.NONE)) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private void resetState() {
        isChanged = false;
        failReasons.clear();
        isNewRequest = false;
    }

    private void saveUpdatedModel() {
        RecipeDurationPersistenceModel updatedModel = RecipeDurationPersistenceModel.Builder.
                basedOnPersistenceModel(persistenceModel).
                setPrepTime(getTotalMinutes(
                        requestModel.getPrepHours(),
                        requestModel.getPrepMinutes())).
                setCookTime(getTotalMinutes(
                        requestModel.getCookHours(),
                        requestModel.getCookMinutes())).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).build();

        save(updatedModel);
    }

    private void save(RecipeDurationPersistenceModel model) {
        System.out.println(TAG + "savingPersistenceModel:" + model);
        repository.save(convertModelToEntity(model));
    }

    private RecipeDurationEntity convertModelToEntity(RecipeDurationPersistenceModel model) {
        return new RecipeDurationEntity(
                model.getId(),
                model.getPrepTime(),
                model.getCookTime(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
