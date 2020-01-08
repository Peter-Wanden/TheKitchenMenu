package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.LinkedList;
import java.util.List;

public class RecipeDuration
        extends UseCaseInteractor<RecipeDurationRequest, RecipeDurationResponse>
        implements DataSource.GetEntityCallback<RecipeDurationEntity> {

    private static final String TAG = "tkm-" + RecipeDuration.class.getSimpleName() + ": ";

    public enum FailReason {
        INVALID_PREP_TIME,
        INVALID_COOK_TIME,
        NONE
    }

    public static final String DO_NOT_CLONE = "";

    private final TimeProvider timeProvider;
    private final RepositoryRecipeDuration repository;
    private final int MAX_PREP_TIME;
    private final int MAX_COOK_TIME;

    private String recipeId = "";
    private RecipeDurationModel requestModel = RecipeDurationModel.Builder.getDefault().build();
    private RecipeDurationModel responseModel = RecipeDurationModel.Builder.getDefault().build();
    private boolean isCloned;

    public RecipeDuration(RepositoryRecipeDuration repository,
                          TimeProvider timeProvider,
                          int maxPrepTime,
                          int maxCookTime) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;
    }

    @Override
    protected void execute(RecipeDurationRequest request) {
        System.out.println(TAG + request);
        requestModel = request.getModel();
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            calculateValues();
        }
    }

    private boolean isNewRequest(RecipeDurationRequest request) {
        return !recipeId.equals(request.getRecipeId());
    }

    private void loadData(RecipeDurationRequest request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getById(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(RecipeDurationRequest request) {
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    @Override
    public void onEntityLoaded(RecipeDurationEntity entity) {
        requestModel = convertEntityToModel(entity);

        if (isCloned) {
            save(requestModel);
            isCloned = false;
        }

        equaliseState();
        buildResponse();
    }

    private RecipeDurationModel convertEntityToModel(RecipeDurationEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeDurationModel.Builder().
                setId(isCloned ? recipeId : entity.getId()).
                setPrepHours(getHours(entity.getPrepTime())).
                setPrepMinutes(getMinutes(entity.getPrepTime())).
                setTotalPrepTime(entity.getPrepTime()).
                setCookHours(getHours(entity.getCookTime())).
                setCookMinutes(getMinutes(entity.getCookTime())).
                setTotalCookTime(entity.getCookTime()).
                setTotalTime(entity.getPrepTime() + entity.getCookTime()).
                setCreateDate(isCloned ? currentTime : entity.getCreateDate()).
                setLastUpdate(isCloned ? currentTime : entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        requestModel = createNewModel();
        equaliseState();
        save(responseModel);
        buildResponse();
    }

    private RecipeDurationModel createNewModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return RecipeDurationModel.Builder.
                getDefault().
                setId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void calculateValues() {
        int totalPrepTime = calculateTotalMinutes(requestModel.getPrepHours(),
                requestModel.getPrepMinutes());

        int totalCookTime = calculateTotalMinutes(requestModel.getCookHours(),
                requestModel.getCookMinutes());

        requestModel = RecipeDurationModel.Builder.
                basedOn(requestModel).
                setPrepHours(getHours(totalPrepTime)).
                setPrepMinutes(getMinutes(totalPrepTime)).
                setTotalPrepTime(totalPrepTime).
                setCookHours(getHours(totalCookTime)).
                setCookMinutes(getMinutes(totalCookTime)).
                setTotalCookTime(totalCookTime).
                build();

        buildResponse();
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }

    private int calculateTotalMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void buildResponse() {
        RecipeDurationResponse.Builder builder = new RecipeDurationResponse.Builder().
                setState(getState()).
                setFailReasons(getFailReasons()
        );

        equaliseState();

        RecipeDurationResponse response = builder.
                setModel(responseModel).
                build();

        sendResponse(response);
    }

    private void equaliseState() {
        responseModel = requestModel;
    }

    private void sendResponse(RecipeDurationResponse response) {
        System.out.println(TAG + response);
        if (response.getState() == RecipeState.ComponentState.VALID_CHANGED ||
                response.getState() == RecipeState.ComponentState.VALID_UNCHANGED) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private List<FailReason> getFailReasons() {
        List<FailReason> failReasons = new LinkedList<>();

        if (requestModel.getTotalPrepTime() > MAX_PREP_TIME) {
            failReasons.add(FailReason.INVALID_PREP_TIME);
        }
        if (requestModel.getTotalCookTime() > MAX_COOK_TIME) {
            failReasons.add(FailReason.INVALID_COOK_TIME);
        }
        if (isValid()) {
            failReasons.add(FailReason.NONE);
        }
        return failReasons;
    }

    private RecipeState.ComponentState getState() {
        if (!isValid() && !isChanged()) {
            return RecipeState.ComponentState.INVALID_UNCHANGED;

        } else if (isValid() && !isChanged()) {
            return RecipeState.ComponentState.VALID_UNCHANGED;

        } else if (!isValid() && isChanged()) {
            return RecipeState.ComponentState.INVALID_CHANGED;

        } else {
            requestModel = RecipeDurationModel.Builder.
                    basedOn(requestModel).
                    setLastUpdate(timeProvider.getCurrentTimeInMills()).
                    build();
            save(requestModel);

            return RecipeState.ComponentState.VALID_CHANGED;
        }
    }

    private boolean isValid() {
        return requestModel.getTotalPrepTime() <= MAX_PREP_TIME &&
                requestModel.getTotalCookTime() <= MAX_COOK_TIME;
    }

    private boolean isChanged() {
        return !requestModel.equals(responseModel);
    }

    private void save(RecipeDurationModel model) {
        repository.save(convertModelToEntity(model));
    }

    private RecipeDurationEntity convertModelToEntity(RecipeDurationModel model) {
        return new RecipeDurationEntity(
                model.getId(),
                model.getTotalPrepTime(),
                model.getTotalCookTime(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
