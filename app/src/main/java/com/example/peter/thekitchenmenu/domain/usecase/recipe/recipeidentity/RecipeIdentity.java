package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
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

    private static final TextType TITLE_TEXT_TYPE = TextType.SHORT_TEXT;
    private static final TextType DESCRIPTION_TEXT_TYPE = TextType.LONG_TEXT;

    @Nonnull
    private final RepositoryRecipeIdentity repository;
    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private final TextValidator textValidator;

    private RecipeIdentityRequest.Model requestModel;
    private RecipeIdentityPersistenceModel persistenceModel;

    private List<FailReasons> failReasons;
    private String recipeId = "";

    private boolean isCloned;
    private boolean isChanged;
    private boolean isNewRequest;

    public RecipeIdentity(@Nonnull RepositoryRecipeIdentity repository,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull UseCaseHandler handler,
                          @Nonnull TextValidator textValidator) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.handler = handler;
        this.textValidator = textValidator;

        requestModel = RecipeIdentityRequest.Model.Builder.getDefault().build();
        persistenceModel = RecipeIdentityPersistenceModel.Builder.getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected void execute(RecipeIdentityRequest request) {
        System.out.println(TAG + request);
        requestModel = request.getModel();

        if (isNewRequest(request.getRecipeId())) {
            extractIds(request);
        } else {
            processChanges();
        }
    }

    private boolean isNewRequest(String recipeId) {
        return isNewRequest = !this.recipeId.equals(recipeId);
    }

    private void extractIds(RecipeIdentityRequest request) {
        if (isCloneRequest(request)) {
            isCloned = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        loadData(request.getRecipeId());
    }

    private boolean isCloneRequest(RecipeIdentityRequest request) {
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    private void loadData(String recipeId) {
        repository.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity entity) {
        isChanged = false;
        persistenceModel = convertEntityToPersistenceModel(entity);
        validateData();

        if (isCloned && failReasons.isEmpty()) {
            save(persistenceModel);
            isCloned = false;
        }
        buildResponse();
    }

    private RecipeIdentityPersistenceModel convertEntityToPersistenceModel(RecipeIdentityEntity entity) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeIdentityPersistenceModel.Builder().
                setId(isCloned ? recipeId : entity.getId()).
                setTitle(entity.getTitle() == null ? "" : entity.getTitle()).
                setDescription(entity.getDescription() == null ? "" : entity.getDescription()).
                setCreateDate(isCloned ? currentTime : entity.getCreateDate()).
                setLastUpdate(isCloned ? currentTime : entity.getLastUpdate()).
                build();
    }

    @Override
    public void onDataNotAvailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(FailReason.DATA_UNAVAILABLE);
        buildResponse();
    }

    private RecipeIdentityPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return RecipeIdentityPersistenceModel.Builder.getDefault().
                setId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void processChanges() {
        System.out.println(TAG + "process changes");
        if (!persistenceModel.getTitle().equals(requestModel.getTitle().trim())) {
            isChanged = true;
        }
        if (!persistenceModel.getDescription().equals(requestModel.getDescription().trim())) {
            isChanged = true;
        }
        validateData();
    }

    private void validateData() {
        validateTitle();
    }

    private void validateTitle() {
        String title;
        if (isNewRequest) {
            title = persistenceModel.getTitle();
        } else {
            title = requestModel.getTitle();
        }
        TextValidatorRequest request = new TextValidatorRequest(
                TITLE_TEXT_TYPE,
                new TextValidatorModel(title)
        );
        handler.execute(textValidator, request, new UseCase.Callback<TextValidatorResponse>() {

            @Override
            public void onSuccess(TextValidatorResponse response) {
                validateDescription();
            }

            @Override
            public void onError(TextValidatorResponse response) {
                TextValidator.FailReason failReason = response.getFailReason();
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(FailReason.TITLE_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(FailReason.TITLE_TOO_LONG);
                }
                validateDescription();
            }
        });
    }

    private void validateDescription() {
        String description;
        if (isNewRequest) {
            description = persistenceModel.getDescription();
        } else {
            description = requestModel.getDescription();
        }
        TextValidatorRequest request = new TextValidatorRequest(
                DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(description)
        );
        handler.execute(textValidator, request, new UseCase.Callback<TextValidatorResponse>() {
            @Override
            public void onSuccess(TextValidatorResponse response) {
                if (!isNewRequest) {
                    buildResponse();
                }
            }

            @Override
            public void onError(TextValidatorResponse response) {
                TextValidator.FailReason failReason = response.getFailReason();
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(FailReason.DESCRIPTION_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(FailReason.DESCRIPTION_TOO_LONG);
                }
                if (!isNewRequest) {
                    buildResponse();
                }
            }
        });
    }

    private void buildResponse() {
        RecipeIdentityResponse.Builder responseBuilder = new RecipeIdentityResponse.Builder().
                setFailReasons(getFailReasons());

        if (getComponentState() == ComponentState.VALID_CHANGED) {
            saveUpdatedModel();
        }

        responseBuilder.setState(getComponentState()).setModel(getResponseModel());
        sendResponse(responseBuilder.build());
    }

    public ComponentState getComponentState() {
        if (failReasons.contains(FailReason.DATA_UNAVAILABLE)) {
            return ComponentState.DATA_UNAVAILABLE;

        } else if (!isValid() && !isChanged) {
            return ComponentState.INVALID_UNCHANGED;

        } else if (isValid() && !isChanged) {
            return ComponentState.VALID_UNCHANGED;

        } else if (!isValid() && isChanged) {
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

    private void saveUpdatedModel() {
        persistenceModel = RecipeIdentityPersistenceModel.Builder.
                basedOn(persistenceModel).
                setTitle(requestModel.getTitle()).
                setDescription(requestModel.getDescription()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
        save(persistenceModel);
    }

    private RecipeIdentityResponse.Model getResponseModel() {
        if (isNewRequest) {
            return RecipeIdentityResponse.Model.Builder.
                    basedOn(persistenceModel).
                    build();
        } else {
            return RecipeIdentityResponse.Model.Builder.
                    basedOn(requestModel).
                    setId(recipeId).
                    build();
        }
    }

    private void sendResponse(RecipeIdentityResponse response) {
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

    private boolean isValid() {
        return failReasons.contains(FailReason.NONE);
    }

    private void save(RecipeIdentityPersistenceModel model) {
        repository.save(convertModelToEntity(model));
    }

    // todo - move model / entity conversions to the data layer
    private RecipeIdentityEntity convertModelToEntity(RecipeIdentityPersistenceModel model) {
        return new RecipeIdentityEntity(
                model.getId(),
                model.getTitle().trim(),
                model.getDescription().trim(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
