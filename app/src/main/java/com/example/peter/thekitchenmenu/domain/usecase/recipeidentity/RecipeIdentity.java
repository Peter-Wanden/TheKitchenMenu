package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.FailReasons;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;
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
        DESCRIPTION_TOO_SHORT,
        DESCRIPTION_TOO_LONG,
        NONE
    }

    public static final String DO_NOT_CLONE = "";
    public static final TextType TITLE_TEXT_TYPE = TextType.SHORT_TEXT;
    public static final TextType DESCRIPTION_TEXT_TYPE = TextType.LONG_TEXT;

    private final RepositoryRecipeIdentity repository;
    private final TimeProvider timeProvider;
    private final UseCaseHandler handler;
    private UseCase<UseCase.Request, UseCase.Response> mediator;

    private RecipeIdentityRequest.Model requestModel;
    private RecipeIdentityModel persistenceModel;

    private List<FailReasons> failReasons;
    private String recipeId = "";
    private boolean isCloned;
    private boolean isChanged;
    private boolean isNewRequest;
    private boolean isDataUnAvailable;
    private boolean isTextValidationError;

    public RecipeIdentity(RepositoryRecipeIdentity repository,
                          TimeProvider timeProvider,
                          UseCaseHandler handler,
                          UseCase<UseCase.Request, UseCase.Response> mediator) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.handler = handler;
        this.mediator = mediator;

        requestModel = RecipeIdentityRequest.Model.Builder.getDefault().build();
        persistenceModel = RecipeIdentityModel.Builder.getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected void execute(RecipeIdentityRequest request) {
        System.out.println(TAG + request);
        requestModel = request.getModel();
        if (isNewRequest(request)) {
            isNewRequest = true;
            loadData(request);
        } else {
            processChanges();
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
        isChanged = false;
        persistenceModel = convertEntityToModel(entity);
        validateData();

        if (isCloned) {
            save(persistenceModel);
            isCloned = false;
        }
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

    private void validateData() {
        validateTitle();
    }

    private void validateTitle() {

        String title = persistenceModel.getTitle();
        TextValidatorRequest request = new TextValidatorRequest(
                TITLE_TEXT_TYPE,
                new TextValidatorModel(title)
        );
        handler.execute(mediator, request, new Callback<Response>() {
            @Override
            public void onSuccess(Response response) {
                validateDescription();
            }

            @Override
            public void onError(Response response) {
                isTextValidationError = true;
                TextValidator.FailReason failReason = ((TextValidatorResponse) response).getFailReason();
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(RecipeIdentity.FailReason.TITLE_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(RecipeIdentity.FailReason.TITLE_TOO_LONG);
                }
                validateDescription();
            }
        });
    }

    private void validateDescription() {
        String description = persistenceModel.getDescription();
        TextValidatorRequest request = new TextValidatorRequest(
                RecipeIdentity.DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(description)
        );
        handler.execute(mediator, request, new Callback<Response>() {
            @Override
            public void onSuccess(Response response) {
                if (!isTextValidationError) {

                }
            }

            @Override
            public void onError(Response response) {
                TextValidator.FailReason failReason = ((TextValidatorResponse) response).getFailReason();
                failReasons.add(failReason);
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG);
                }
            }
        });
    }

    @Override
    public void onDataNotAvailable() {
        persistenceModel = createNewPersistenceModel();
        isDataUnAvailable = true;
        buildResponse();
    }

    private RecipeIdentityModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return RecipeIdentityModel.Builder.getDefault().
                setId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void processChanges() {
        if (!persistenceModel.getTitle().equals(requestModel.getTitle().trim())) {
            isChanged = true;
        }
        if (!persistenceModel.getDescription().equals(requestModel.getDescription().trim())) {
            isChanged = true;
        }
        buildResponse();
    }

    private void buildResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder().
                setState(getState()).
                setFailReasons(getFailReasons()
                );

        RecipeIdentityResponse response = builder.
                setModel(getResponseModel()).
                build();

        sendResponse(response);
    }

    private ComponentState getState() {
        if (isDataUnAvailable) {
            return ComponentState.DATA_UNAVAILABLE;

        } else if (!isValid() && !isChanged) {
            return ComponentState.INVALID_UNCHANGED;

        } else if (isValid() && !isChanged) {
            return ComponentState.VALID_UNCHANGED;

        } else if (!isValid() && isChanged) {
            return ComponentState.INVALID_CHANGED;

        } else {
            persistenceModel = RecipeIdentityModel.Builder.
                    basedOn(persistenceModel).
                    setTitle(requestModel.getTitle()).
                    setDescription(requestModel.getDescription()).
                    setLastUpdate(timeProvider.getCurrentTimeInMills()).
                    build();
            save(persistenceModel);

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

    private RecipeIdentityResponse.Model getResponseModel() {
        return RecipeIdentityResponse.Model.Builder.basedOn(persistenceModel).build();
    }

    private void sendResponse(RecipeIdentityResponse response) {
        System.out.println(TAG + response);
        ComponentState state = response.getState();
        isChanged = false;

        if (state == ComponentState.VALID_UNCHANGED || state == ComponentState.VALID_CHANGED) {
            getUseCaseCallback().onSuccess(response);
        } else {
            if (isDataUnAvailable) {
                isDataUnAvailable = false;
            }
            getUseCaseCallback().onError(response);
        }
    }

    private boolean isValid() {
        return !persistenceModel.getTitle().isEmpty() || !requestModel.getTitle().isEmpty();
    }

    private void save(RecipeIdentityModel model) {
        repository.save(convertModelToEntity(model));
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
