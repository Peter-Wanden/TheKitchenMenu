package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

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

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.*;

public class RecipeIdentity
        extends
        UseCase<RecipeIdentityRequest, RecipeIdentityResponse>
        implements
        DataSource.GetEntityCallback<RecipeIdentityEntity> {

    private static final String TAG = "tkm-" + RecipeIdentity.class.getSimpleName() + ": ";
    private static final ComponentName RECIPE_IDENTITY = ComponentName.IDENTITY;

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

    private final RepositoryRecipeIdentity repository;
    private final TimeProvider timeProvider;
    private final UseCaseHandler handler;
    private final TextValidator textValidator;

    private RecipeIdentityRequest.Model requestModel;
    private RecipeIdentityModel persistenceModel;

    private List<FailReasons> failReasons;
    private String recipeId = "";

    private boolean isCloned;
    private boolean isChanged;
    private boolean isNewRequest;
    private boolean isColleagueStartRequest;

    public RecipeIdentity(RepositoryRecipeIdentity repository,
                          TimeProvider timeProvider,
                          UseCaseHandler handler,
                          TextValidator textValidator) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.handler = handler;
        this.textValidator = textValidator;

        requestModel = RecipeIdentityRequest.Model.Builder.getDefault().build();
        persistenceModel = RecipeIdentityModel.Builder.getDefault().build();
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

    private boolean isNewRequest(String recipeId) {
        return isNewRequest = !this.recipeId.equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeIdentityEntity entity) {
        isChanged = false;
        persistenceModel = convertEntityToModel(entity);
        validateData();

        if (isCloned && failReasons.isEmpty()) {
            save(persistenceModel);
            isCloned = false;
        }
        buildResponse();
    }

    private RecipeIdentityModel convertEntityToModel(RecipeIdentityEntity entity) {
        long timeNow = timeProvider.getCurrentTimeInMills();
        return new RecipeIdentityModel.Builder().
                setId(isCloned ? recipeId : entity.getId()).
                setTitle(entity.getTitle() == null ? "" : entity.getTitle()).
                setDescription(entity.getDescription() == null ? "" : entity.getDescription()).
                setCreateDate(isCloned ? timeNow : entity.getCreateDate()).
                setLastUpdate(isCloned ? timeNow : entity.getLastUpdate()).
                build();
    }



    @Override
    public void onDataNotAvailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(FailReason.DATA_UNAVAILABLE);
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
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder().
                setFailReasons(getFailReasons());

        ComponentState state = getState();

        if (state == ComponentState.VALID_CHANGED) {
            saveUpdatedModel();
        }

        builder.setState(state).setModel(getResponseModel());
        sendResponse(builder.build());
    }

    public ComponentState getState() {
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
        persistenceModel = RecipeIdentityModel.Builder.
                basedOn(persistenceModel).
                setTitle(requestModel.getTitle()).
                setDescription(requestModel.getDescription()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
        save(persistenceModel);
    }

    private RecipeIdentityResponse.Model getResponseModel() {
        if (isNewRequest) {
            return RecipeIdentityResponse.Model.Builder.basedOn(persistenceModel).build();
        } else {
            return RecipeIdentityResponse.Model.Builder.basedOn(requestModel).setId(recipeId).build();
        }
    }

    private void sendResponse(RecipeIdentityResponse response) {
        System.out.println(TAG + response);

        resetState();

        if (!isColleagueStartRequest) {
            if (response.getFailReasons().contains(FailReason.NONE)) {
                getUseCaseCallback().onSuccess(response);
            } else {
                getUseCaseCallback().onError(response);
            }
        }
        isColleagueStartRequest = false;
    }

    private void resetState() {
        isChanged = false;
        failReasons.clear();
        isNewRequest = false;
    }

    private boolean isValid() {
        return failReasons.contains(FailReason.NONE);
    }

    private void save(RecipeIdentityModel model) {
        repository.save(convertModelToEntity(model));
    }

    // todo - move model / entity conversions to the data layer
    private RecipeIdentityEntity convertModelToEntity(RecipeIdentityModel model) {
        return new RecipeIdentityEntity(
                model.getId(),
                model.getTitle().trim(),
                model.getDescription().trim(),
                model.getCreateDate(),
                model.getLastUpdate()
        );
    }
}
