package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.FailReasons;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState.*;
import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.*;

public class RecipeIdentity
        extends UseCase<RecipeIdentityRequest, RecipeIdentityResponse>
        implements DataSource.GetEntityCallback<RecipeIdentityEntity> {

    private static final String TAG = "tkm-" + RecipeIdentity.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
        DATA_UNAVAILABLE,
        TITLE_TOO_SHORT,
        TITLE_TOO_LONG,
        TITLE_OK,
        DESCRIPTION_TOO_SHORT,
        DESCRIPTION_TOO_LONG,
        DESCRIPTION_OK,
        NONE
    }

    public static final String DO_NOT_CLONE = "";
    public static final TextType TITLE_TEXT_TYPE = TextType.SHORT_TEXT;
    public static final TextType DESCRIPTION_TEXT_TYPE = TextType.LONG_TEXT;

    private TimeProvider timeProvider;
    private RepositoryRecipeIdentity repository;
    private RecipeIdentityModel requestModel;
    private RecipeIdentityModel responseModel;

    private String recipeId = "";
    private boolean isCloned;
    private boolean isDataUnAvailable;

    public RecipeIdentity(RepositoryRecipeIdentity repository,
                          TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        requestModel = RecipeIdentityModel.Builder.getDefault().build();
        responseModel = RecipeIdentityModel.Builder.getDefault().build();
    }

    @Override
    protected void execute(RecipeIdentityRequest request) {
        System.out.println(TAG + request);
        requestModel = request.getModel();
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            buildResponse();
        }
    }

    private boolean isNewRequest(RecipeIdentityRequest request) {
        return !recipeId.equals(request.getRecipeId());
    }

    private void loadData(RecipeIdentityRequest request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getById(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(RecipeIdentityRequest request) {
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity entity) {
        isDataUnAvailable = false;
        requestModel = convertEntityToModel(entity);

        if (isCloned) {
            save(requestModel);
            isCloned = false;
        }

        equaliseState();
        buildResponse();
    }

    private RecipeIdentityModel convertEntityToModel(RecipeIdentityEntity entity) {
        long time = timeProvider.getCurrentTimeInMills();
        return new RecipeIdentityModel.Builder().
                setId(isCloned ? recipeId : entity.getId()).
                setTitle(entity.getTitle() == null ? "" : entity.getTitle()).
                setDescription(entity.getDescription() == null ? "" : entity.getDescription()).
                setCreateDate(isCloned ? time : entity.getCreateDate()).
                setLastUpdate(isCloned ? time : entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        requestModel = createNewModel();
        isDataUnAvailable = true;
        buildResponse();
    }

    private RecipeIdentityModel createNewModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return RecipeIdentityModel.Builder.getDefault().
                setId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void buildResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder().
                setState(getState()).
                setFailReasons(getFailReasons()
        );

        equaliseState();

        RecipeIdentityResponse response = builder.
                setModel(responseModel).
                build();

        sendResponse(response);
    }

    private ComponentState getState() {
        if (isDataUnAvailable) {
            return ComponentState.DATA_UNAVAILABLE;


        } else if (!isValid() && !isModelChanged()) {
            return ComponentState.INVALID_UNCHANGED;

        } else if (isValid() && !isModelChanged()) {
            return ComponentState.VALID_UNCHANGED;

        } else if (!isValid() && isModelChanged()) {
            return ComponentState.INVALID_CHANGED;

        } else {
            requestModel = RecipeIdentityModel.Builder.
                    basedOn(requestModel).
                    setLastUpdate(timeProvider.getCurrentTimeInMills()).
                    build();
            save(requestModel);

            return ComponentState.VALID_CHANGED;
        }
    }

    private List<FailReasons> getFailReasons() {
        List<FailReasons> failReasons = new ArrayList<>();

        if (isDataUnAvailable) {
            failReasons.add(FailReason.DATA_UNAVAILABLE);
        }
        if (isValid()) {
            failReasons.add(FailReason.NONE);
        }
        return failReasons;
    }

    private void sendResponse(RecipeIdentityResponse response) {
        System.out.println(TAG + response);
        ComponentState state = response.getState();

        if (state == ComponentState.VALID_UNCHANGED || state == ComponentState.VALID_CHANGED) {
            getUseCaseCallback().onSuccess(response);
        } else {
            if (isDataUnAvailable) isDataUnAvailable = false;
            getUseCaseCallback().onError(response);
        }
    }

    private boolean isModelChanged() {
        return !requestModel.equals(responseModel);
    }

    private boolean isValid() {
        if (requestModel.equals(RecipeIdentityModel.Builder.getDefault().build())) {
            return false;
        } else {
            return !requestModel.getTitle().isEmpty();
        }
    }

    private void save(RecipeIdentityModel model) {
        repository.save(convertModelToEntity(model));
    }

    private void equaliseState() {
        responseModel = requestModel;
    }

    // todo - move model / entity conversions to the data layer
    private RecipeIdentityEntity convertModelToEntity(RecipeIdentityModel model) {
        return new RecipeIdentityEntity(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
