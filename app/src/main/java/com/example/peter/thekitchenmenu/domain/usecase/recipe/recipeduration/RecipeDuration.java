package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeDuration extends UseCase
        implements DataSource.GetEntityCallback<RecipeDurationEntity> {

    private static final String TAG = "tkm-" + RecipeDuration.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
        INVALID_PREP_TIME,
        INVALID_COOK_TIME
    }

    private final int MAX_PREP_TIME;
    private final int MAX_COOK_TIME;

    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final RepositoryRecipeDuration repository;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String recipeId = "";
    private boolean isNewRequest;
    private boolean isCloned;

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

        requestModel = RecipeDurationRequest.Model.Builder.getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeDurationRequest rdr = (RecipeDurationRequest) request;

        System.out.println(TAG + rdr);
        requestModel = rdr.getModel();

        if (isNewRequest(rdr.getId())) {
            extractIds(rdr);
        } else {
            processChanges();
        }
    }

    private boolean isNewRequest(String recipeId) {
        return isNewRequest = !this.recipeId.equals(recipeId);
    }

    private void extractIds(RecipeDurationRequest request) {
        if (isCloneRequest(request)) {
            recipeId = request.getCloneToId();
        } else {
            recipeId = request.getId();
        }
        loadData(request.getId());
    }

    private boolean isCloneRequest(RecipeDurationRequest request) {
        return isCloned = !request.getCloneToId().equals(DO_NOT_CLONE);
    }

    private void loadData(String recipeId) {
        repository.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeDurationEntity entity) {
        persistenceModel = convertEntityToPersistenceModel(entity);
        validateData();

        if (isCloned && failReasons.contains(CommonFailReason.NONE)) {
            save();
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
                setLastUpdate(isCloned ? currentTime : entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);

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
            prepTime = getRequestPrepTime();
            cookTime = getRequestCookTime();
        }
        if (prepTime > MAX_PREP_TIME) {
            failReasons.add(FailReason.INVALID_PREP_TIME);
        }
        if (cookTime > MAX_COOK_TIME) {
            failReasons.add(FailReason.INVALID_COOK_TIME);
        }
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
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
        RecipeDurationResponse response = new RecipeDurationResponse.Builder().
                setId(recipeId).
                setState(getComponentState()).
                setFailReasons(new ArrayList<>(failReasons)).
                setModel(getResponseModel()).
                build();

        if (response.getState() == ComponentState.VALID_CHANGED) {
            persistenceModel = updatePersistenceFromRequestModel();
            save();
        }
        sendResponse(response);
    }

    private ComponentState getComponentState() {
        boolean isValid = failReasons.contains(CommonFailReason.NONE);

        if (failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
            return ComponentState.DATA_UNAVAILABLE;

        } else if (!isValid && !isChanged()) {
            return ComponentState.INVALID_UNCHANGED;

        } else if (isValid && !isChanged()) {
            return ComponentState.VALID_UNCHANGED;

        } else if (!isValid && isChanged()) {
            return ComponentState.INVALID_CHANGED;

        } else {
            return ComponentState.VALID_CHANGED;
        }
    }

    private boolean isChanged() {
        return !isNewRequest && (isPrepTimeChanged() || isCookTimeChanged());
    }

    private boolean isPrepTimeChanged() {
        return getRequestPrepTime() != persistenceModel.getPrepTime();
    }

    private boolean isCookTimeChanged() {
        return getRequestCookTime() != persistenceModel.getCookTime();
    }

    private int getRequestPrepTime() {
        return getTotalMinutes(requestModel.getPrepHours(), requestModel.getPrepMinutes());
    }

    private int getRequestCookTime() {
        return getTotalMinutes(requestModel.getCookHours(), requestModel.getCookMinutes());
    }

    private RecipeDurationPersistenceModel updatePersistenceFromRequestModel() {
        return RecipeDurationPersistenceModel.Builder.
                basedOnPersistenceModel(persistenceModel).

                setPrepTime(getTotalMinutes(
                        requestModel.getPrepHours(),
                        requestModel.getPrepMinutes())).

                setCookTime(getTotalMinutes(
                        requestModel.getCookHours(),
                        requestModel.getCookMinutes())).

                setLastUpdate(timeProvider.getCurrentTimeInMills()).build();
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
                        getTotalMinutes(
                                requestModel.getPrepHours(),
                                requestModel.getPrepMinutes())).

                setCookHours(isNewRequest ?
                        getHours(persistenceModel.getCookTime()) :
                        requestModel.getCookHours()).

                setCookMinutes(isNewRequest ?
                        getMinutes(persistenceModel.getCookTime()) :
                        requestModel.getCookMinutes()).

                setTotalCookTime(isNewRequest ?
                        persistenceModel.getCookTime() :
                        getTotalMinutes(
                                requestModel.getCookHours(),
                                requestModel.getCookMinutes())).

                setTotalTime(isNewRequest ?
                        persistenceModel.getPrepTime() + persistenceModel.getCookTime() :
                        getTotalMinutes(
                                requestModel.getPrepHours(),
                                requestModel.getPrepMinutes()) +
                                getTotalMinutes(
                                        requestModel.getCookHours(),
                                        requestModel.getCookMinutes())).

                setCreateDate(persistenceModel.getCreateDate()).
                setLastUpdate(persistenceModel.getLastUpdate()).
                build();
    }

    private void sendResponse(RecipeDurationResponse response) {
        System.out.println(TAG + response);
        resetState();

        if (response.getFailReasons().contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private void resetState() {
        failReasons.clear();
        isNewRequest = false;
    }

    private void save() {
        System.out.println(TAG + "savingPersistenceModel:" + persistenceModel);
        repository.save(convertModelToEntity(persistenceModel));
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
