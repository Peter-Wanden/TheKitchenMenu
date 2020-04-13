package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;
import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.*;

public class RecipeIdentity
        extends UseCase
        implements DomainDataAccess.GetDomainModelCallback<RecipeIdentityPersistenceModel> {

    private static final String TAG = "tkm-" + RecipeIdentity.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
        TITLE_TOO_SHORT(300),
        TITLE_TOO_LONG(301),
        DESCRIPTION_TOO_SHORT(302),
        DESCRIPTION_TOO_LONG(303);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason s : FailReason.values())
                options.put(s.id, s);
        }

        public static FailReason getById(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }

    private static final TextType TITLE_TEXT_TYPE = TextType.SHORT_TEXT;
    private static final TextType DESCRIPTION_TEXT_TYPE = TextType.LONG_TEXT;

    @Nonnull
    private final RepositoryRecipeIdentity repository;
    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private final TextValidator textValidator;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String dataId = "";
    private String recipeId = "";
    private boolean isNewRequest;

    private RecipeIdentityRequest.Model requestModel;
    private RecipeIdentityPersistenceModel persistenceModel;

    public RecipeIdentity(@Nonnull RepositoryRecipeIdentity repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull UseCaseHandler handler,
                          @Nonnull TextValidator textValidator) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.handler = handler;
        this.textValidator = textValidator;

        requestModel = new RecipeIdentityRequest.Model.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeIdentityRequest r = (RecipeIdentityRequest) request;
        requestModel = r.getModel();
        System.out.println(TAG + r);

        if (isNewRequest(r)) {
            dataId = r.getDataId();
            recipeId = r.getDomainId();
            loadData(recipeId);
        } else {
            processChanges();
        }
    }

    private boolean isNewRequest(RecipeIdentityRequest r) {
        return isNewRequest = !r.getDomainId().equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getByDataId(recipeId, this);
    }

    @Override
    public void onModelLoaded(RecipeIdentityPersistenceModel model) {
        persistenceModel = model;
        validateData();
        buildResponse();
    }

    @Override
    public void onModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        buildResponse();
    }

    private RecipeIdentityPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        dataId = idProvider.getUId();
        return new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                setDataId(dataId).
                setDomainId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void processChanges() {
        validateData();
        buildResponse();
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
                FailReasons failReason = response.getFailReason();
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
                if (failReasons.isEmpty()) {
                    failReasons.add(CommonFailReason.NONE);
                }
            }

            @Override
            public void onError(TextValidatorResponse response) {
                FailReasons failReason = response.getFailReason();
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(FailReason.DESCRIPTION_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(FailReason.DESCRIPTION_TOO_LONG);
                }
            }
        });
    }

    private void buildResponse() {
        RecipeIdentityResponse response = new RecipeIdentityResponse.Builder().
                setDataId(dataId).
                setDomainId(recipeId).
                setMetadata(getMetadata()).
                setModel(getResponseModel()).
                build();

        if (response.getMetadata().getState() == ComponentState.VALID_CHANGED) {
            save(updatePersistenceFromRequestModel());
        }
        sendResponse(response);
    }

    private UseCaseMetadata getMetadata() {
        return new UseCaseMetadata.Builder().
                setState(getComponentState()).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreateDate(persistenceModel.getCreateDate()).
                setLasUpdate(persistenceModel.getLastUpdate()).
                build();
    }

    private ComponentState getComponentState() {
        boolean isValid = failReasons.contains(CommonFailReason.NONE);
        if (!isValid && !isChanged()) {
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
        return !isNewRequest && (isTitleChanged() || isDescriptionChanged());
    }

    private boolean isTitleChanged() {
        return !persistenceModel.getTitle().toLowerCase().
                equals(requestModel.getTitle().toLowerCase().trim());
    }

    private boolean isDescriptionChanged() {
        return !persistenceModel.getDescription().toLowerCase().trim().
                equals(requestModel.getDescription().toLowerCase().trim());
    }

    private RecipeIdentityPersistenceModel updatePersistenceFromRequestModel() {
        return new RecipeIdentityPersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setTitle(requestModel.getTitle()).
                setDescription(requestModel.getDescription()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
    }

    private RecipeIdentityResponse.Model getResponseModel() {
        return new RecipeIdentityResponse.Model.Builder().
                setTitle(isNewRequest ?
                        persistenceModel.getTitle() :
                        requestModel.getTitle()).

                setDescription(isNewRequest ?
                        persistenceModel.getDescription() :
                        requestModel.getDescription()).
                build();
    }

    private void sendResponse(RecipeIdentityResponse response) {
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

    private void save(RecipeIdentityPersistenceModel model) {
        repository.save(model);
    }
}
