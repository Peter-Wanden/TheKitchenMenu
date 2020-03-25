package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipePortions extends UseCase
        implements PrimitiveDataSource.GetEntityCallback<RecipePortionsEntity> {

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

    private String id = "";
    private boolean isNewRequest;

    private RecipePortionsRequest.Model requestModel;
    private RecipePortionsPersistenceModel persistenceModel;

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
        requestModel = portionsRequest.getModel();
        System.out.println(TAG + portionsRequest);

        if (isNewRequest(portionsRequest.getId())) {
            id = portionsRequest.getId();
            loadData(id);
        } else {
            processChanges();
        }
    }

    private boolean isNewRequest(String recipeId) {
        return isNewRequest = !this.id.equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getByRecipeId(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipePortionsEntity entity) {
        persistenceModel = convertEntityToPersistenceModel(entity);
        validateData();
        buildResponse();
    }

    private RecipePortionsPersistenceModel convertEntityToPersistenceModel(RecipePortionsEntity entity) {
        return new RecipePortionsPersistenceModel.Builder().
                setId(entity.getId()).
                setRecipeId(entity.getRecipeId()).
                setServings(entity.getServings()).
                setSittings(entity.getSittings()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);

        buildResponse();
    }

    private RecipePortionsPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipePortionsPersistenceModel.Builder().
                setId(idProvider.getUId()).
                setRecipeId(id).
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
                setId(id).
                setMetadata(getMetadata()).
                setModel(getResponseModel()).
                build();

        if (response.getMetadata().getState() == RecipeMetadata.ComponentState.VALID_CHANGED) {
            save(updatePersistenceFromRequestModel());
        }
        sendResponse(response);
    }

    private RecipeComponentMetadata getMetadata() {
        return new RecipeComponentMetadata.Builder().
                setState(getComponentState()).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreateDate(persistenceModel.getCreateDate()). // TODO - These times may be wrong
                setLasUpdate(persistenceModel.getLastUpdate()).  //  as they are updated after called
                build();
    }

    private RecipeMetadata.ComponentState getComponentState() {
        boolean isValid = failReasons.contains(CommonFailReason.NONE);
        if (!isValid && !isChanged()) {
            return RecipeMetadata.ComponentState.INVALID_UNCHANGED;
        } else if (isValid && !isChanged()) {
            return RecipeMetadata.ComponentState.VALID_UNCHANGED;
        } else if (!isValid && isChanged()) {
            return RecipeMetadata.ComponentState.INVALID_CHANGED;
        } else  {
            return RecipeMetadata.ComponentState.VALID_CHANGED;
        }
    }

    private boolean isChanged() {
        return !isNewRequest && (isServingsChanged() || isSittingsChanged());
    }

    private boolean isServingsChanged() {
        return persistenceModel.getServings() != requestModel.getServings();
    }

    private boolean isSittingsChanged() {
        return persistenceModel.getSittings() != requestModel.getSittings();
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

                build();
    }

    private void sendResponse(RecipePortionsResponse response) {
        System.out.println(TAG + response);
        resetState();

        if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private void resetState() {
        failReasons.clear();
        isNewRequest = false;
    }

    private void save(RecipePortionsPersistenceModel model) {
        repository.save(convertModelToEntity(model));
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
