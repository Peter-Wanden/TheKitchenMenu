package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipePortions extends UseCase
        implements DataSource.GetEntityCallback<RecipePortionsEntity> {

    private static final String TAG = "tkm-" + RecipePortions.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
        SERVINGS_TOO_LOW,
        SERVINGS_TOO_HIGH,
        SITTINGS_TOO_LOW,
        SITTINGS_TOO_HIGH
    }

    static final int MIN_SERVINGS = 1;
    static final int MIN_SITTINGS = 1;
    private final int maxServings;
    private final int maxSittings;

    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final RepositoryRecipePortions repository;
    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String recipeId = "";
    private boolean isNewRequest;
    private boolean isCloned;

    private RecipePortionsPersistenceModel persistenceModel;
    private RecipePortionsRequest.Model requestModel;

    public RecipePortions(@Nonnull RepositoryRecipePortions repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          int maxServings,
                          int maxSittings) {

        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.repository = repository;
        this.maxServings = maxServings;
        this.maxSittings = maxSittings;

        requestModel = RecipePortionsRequest.Model.Builder.getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipePortionsRequest portionsRequest = (RecipePortionsRequest) request;

        System.out.println(TAG + portionsRequest);
        requestModel = portionsRequest.getModel();

        if (isNewRequest(portionsRequest.getId())) {
            extractIds(portionsRequest);
        } else {
            processChanges();
        }
    }

    private boolean isNewRequest(String recipeId) {
        return isNewRequest = !this.recipeId.equals(recipeId);
    }

    private void extractIds(RecipePortionsRequest request) {
        if (isCloneRequest(request)) {
            recipeId = request.getCloneToId();
        } else {
            recipeId = request.getId();
        }
        loadData(request.getId());
    }

    private boolean isCloneRequest(RecipePortionsRequest request) {
        return isCloned = !request.getCloneToId().equals("DO_NOT_CLONE");
    }

    private void loadData(String recipeId) {
        repository.getPortionsForRecipe(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipePortionsEntity entity) {
        persistenceModel = convertEntityToPersistenceModel(entity);
        validateData();

        if (isCloned && failReasons.contains(CommonFailReason.NONE)) {
            save();
            isCloned = false;
        }
        buildResponse();
    }

    private RecipePortionsPersistenceModel convertEntityToPersistenceModel(RecipePortionsEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsPersistenceModel.Builder().
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
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);

        buildResponse();
    }

    private RecipePortionsPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsPersistenceModel.Builder().
                setId(idProvider.getUId()).
                setRecipeId(recipeId).
                setServings(MIN_SERVINGS).
                setSittings(MIN_SITTINGS).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void processChanges() {
        validateData();
        buildResponse();
    }

    private void validateData() {
        int servings;
        int sittings;
        if (isNewRequest) {
            servings = persistenceModel.getServings();
            sittings = persistenceModel.getSittings();
        } else {
            servings = requestModel.getServings();
            sittings = requestModel.getSittings();
        }
        if (servings < MIN_SERVINGS) {
            failReasons.add(FailReason.SERVINGS_TOO_LOW);
        }
        if (servings > maxServings) {
            failReasons.add(FailReason.SERVINGS_TOO_HIGH);
        }
        if (sittings < MIN_SITTINGS) {
            failReasons.add(FailReason.SITTINGS_TOO_LOW);
        }
        if (sittings > maxSittings) {
            failReasons.add(FailReason.SITTINGS_TOO_HIGH);
        }
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
    }

    private void buildResponse() {
        RecipePortionsResponse response = new RecipePortionsResponse.Builder().
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

        if (!isValid && !isChanged()) {
            return ComponentState.INVALID_UNCHANGED;
        } else if (isValid && !isChanged()) {
            return ComponentState.VALID_UNCHANGED;
        } else if (!isValid && isChanged()) {
            return ComponentState.INVALID_CHANGED;
        } else  {
            return ComponentState.VALID_CHANGED;
        }
    }

    private boolean isChanged() {
        return !isNewRequest && (isServingsChanged() || isSittingsChanged());
    }

    private boolean isServingsChanged() {
        return requestModel.getServings() != persistenceModel.getServings();
    }

    private boolean isSittingsChanged() {
        return requestModel.getSittings() != persistenceModel.getSittings();
    }

    private RecipePortionsPersistenceModel updatePersistenceFromRequestModel() {
        return RecipePortionsPersistenceModel.Builder.
                basedOnPersistenceModel(persistenceModel).
                setSittings(requestModel.getSittings()).
                setServings(requestModel.getServings()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
    }

    private RecipePortionsResponse.Model getResponseModel() {
        return new RecipePortionsResponse.Model.Builder().
                setServings(isNewRequest ?
                        persistenceModel.getServings() :
                        requestModel.getServings()).

                setSittings(isNewRequest ?
                        persistenceModel.getSittings() :
                        requestModel.getSittings()).

                setPortions(isNewRequest ?
                        persistenceModel.getServings() * persistenceModel.getSittings() :
                        requestModel.getServings() * requestModel.getSittings()).

                setCreateDate(persistenceModel.getCreateDate()).
                setLasUpdate(persistenceModel.getLastUpdate()).
                build();
    }

    private void sendResponse(RecipePortionsResponse response) {
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
        repository.save(convertModelToEntity(persistenceModel));
    }

    private RecipePortionsEntity convertModelToEntity(RecipePortionsPersistenceModel model) {
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
