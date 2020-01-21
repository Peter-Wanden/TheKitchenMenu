package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState.*;

public class RecipePortions
        extends UseCase<RecipePortionsRequest, RecipePortionsResponse>
        implements DataSource.GetEntityCallback<RecipePortionsEntity> {

    private static final String TAG = "tkm-" + RecipePortions.class.getSimpleName() + ": ";

    public enum FailReason {
        SERVINGS_TOO_LOW,
        SERVINGS_TOO_HIGH,
        SITTINGS_TOO_LOW,
        SITTINGS_TOO_HIGH,
        NONE
    }

    public static final String DO_NOT_CLONE = "";
    public static final int MIN_SERVINGS = 1;
    public static final int MIN_SITTINGS = 1;

    private final TimeProvider timeProvider;
    private final RepositoryRecipePortions repository;
    private final UniqueIdProvider idProvider;

    private final int maxServings;
    private final int maxSittings;

    private String recipeId = "";
    private RecipePortionsModel requestModel;
    private RecipePortionsModel responseModel;
    private boolean isCloned;

    public RecipePortions(RepositoryRecipePortions repository,
                          UniqueIdProvider idProvider,
                          TimeProvider timeProvider,
                          int maxServings,
                          int maxSittings) {

        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.repository = repository;
        this.maxServings = maxServings;
        this.maxSittings = maxSittings;
        requestModel = new RecipePortionsModel.Builder().getDefault().build();
        responseModel = new RecipePortionsModel.Builder().getDefault().build();
    }

    @Override
    protected void execute(RecipePortionsRequest request) {
        System.out.println(TAG + request);
        requestModel = request.getModel();
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            buildResponse();
        }
    }

    private boolean isNewRequest(RecipePortionsRequest request) {
        return !recipeId.equals(request.getRecipeId());
    }

    private void loadData(RecipePortionsRequest request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getPortionsForRecipe(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(RecipePortionsRequest request) {
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    @Override
    public void onEntityLoaded(RecipePortionsEntity entity) {
        requestModel = convertEntityToModel(entity);

        if (isCloned) {
            save(requestModel);
            isCloned = false;
        }

        equaliseState();
        buildResponse();
    }

    private RecipePortionsModel convertEntityToModel(RecipePortionsEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsModel.Builder().
                setId(isCloned ? idProvider.getUId() : entity.getId()).
                setRecipeId(isCloned ? recipeId : entity.getRecipeId()).
                setServings(entity.getServings()).
                setSittings(entity.getSittings()).
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

    private RecipePortionsModel createNewModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsModel.Builder().
                setId(idProvider.getUId()).
                setRecipeId(recipeId).
                setServings(MIN_SERVINGS).
                setSittings(MIN_SITTINGS).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void buildResponse() {
        RecipePortionsResponse.Builder builder = new RecipePortionsResponse.Builder().
                setState(getState()).
                setFailReasons(getFailReasons()
        );

        equaliseState();

        RecipePortionsResponse response = builder.
                setModel(responseModel).
                build();

        sendResponse(response);
    }

    private RecipeState.ComponentState getState() {
        if (!isValid() && !isChanged()) {
            return ComponentState.INVALID_UNCHANGED;

        } else if (isValid() && !isChanged()) {
            return ComponentState.VALID_UNCHANGED;

        } else if (!isValid() && isChanged()) {
            return ComponentState.INVALID_CHANGED;

        } else  {
            requestModel = RecipePortionsModel.Builder.
                    basedOn(requestModel).
                    setLastUpdate(timeProvider.getCurrentTimeInMills()).
                    build();
            save(requestModel);

            return ComponentState.VALID_CHANGED;
        }
    }

    private List<FailReason> getFailReasons() {
        List<FailReason> failReasons = new ArrayList<>();

        if (requestModel.getServings() < MIN_SERVINGS) {
            failReasons.add(FailReason.SERVINGS_TOO_LOW);
        }
        if (requestModel.getServings() > maxServings) {
            failReasons.add(FailReason.SERVINGS_TOO_HIGH);
        }
        if (requestModel.getSittings() < MIN_SITTINGS) {
            failReasons.add(FailReason.SITTINGS_TOO_LOW);
        }
        if (requestModel.getSittings() > maxSittings) {
            failReasons.add(FailReason.SITTINGS_TOO_HIGH);
        }
        if (isValid()) {
            failReasons.add(FailReason.NONE);
        }
        return failReasons;
    }

    private void sendResponse(RecipePortionsResponse response) {
        System.out.println(TAG + response);
        ComponentState state = response.getState();

        if (state == ComponentState.VALID_UNCHANGED || state == ComponentState.VALID_CHANGED) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private void equaliseState() {
        responseModel = requestModel;
    }

    private boolean isValid() {
        return requestModel.getServings() >= MIN_SERVINGS &&
                requestModel.getServings() <= maxServings &&
                requestModel.getSittings() >= MIN_SITTINGS &&
                requestModel.getSittings() <= maxSittings;
    }

    private boolean isChanged() {
        return !requestModel.equals(responseModel);
    }

    private void save(RecipePortionsModel model) {
        repository.save(convertModelToEntity(model));
    }

    private RecipePortionsEntity convertModelToEntity(RecipePortionsModel model) {
        return new RecipePortionsEntity(
                model.getId(),
                model.getRecipeId(),
                model.getServings(),
                model.getSittings(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
