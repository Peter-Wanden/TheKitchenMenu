package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
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

import static com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientDuplicateChecker.NO_DUPLICATE_FOUND;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;
import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.*;

public class Ingredient
        extends UseCase
        implements DataSource.GetDomainModelCallback<IngredientPersistenceModel> {

    private static final String TAG = "tkm-" + Ingredient.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
        UNEDITABLE(151),
        DUPLICATE(152),
        NAME_TOO_SHORT(153),
        NAME_TOO_LONG(154),
        DESCRIPTION_TOO_SHORT(155),
        DESCRIPTION_TOO_LONG(156);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason fr : FailReason.values())
                options.put(fr.id, fr);
        }

        public static FailReason getFromId(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }

    static final String CREATE_NEW_INGREDIENT = "";
    private static final TextType NAME_TEXT_TYPE = TextType.SHORT_TEXT;
    private static final TextType DESCRIPTION_TEXT_TYPE = TextType.LONG_TEXT;

    @Nonnull
    private final RepositoryIngredient repository;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private final TextValidator textValidator;
    @Nonnull
    private final IngredientDuplicateChecker duplicateNameChecker;
    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String id = "";
    private String ingredientId;
    private boolean isNewRequest;
    private IngredientRequest request;
    private IngredientRequest.Model requestModel;
    private IngredientPersistenceModel persistenceModel;

    public Ingredient(@Nonnull RepositoryIngredient repository,
                      @Nonnull UniqueIdProvider idProvider,
                      @Nonnull TimeProvider timeProvider,
                      @Nonnull IngredientDuplicateChecker duplicateNameChecker,
                      @Nonnull UseCaseHandler handler,
                      @Nonnull TextValidator textValidator) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.duplicateNameChecker = duplicateNameChecker;
        this.handler = handler;
        this.textValidator = textValidator;

        requestModel = new IngredientRequest.Model.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        IngredientRequest ingredientRequest = (IngredientRequest) request;
        this.request = ingredientRequest;
        System.out.println(TAG + "request:" + ingredientRequest);

        if (isNewRequest(ingredientRequest.getIngredientId())) {
            ingredientId = ingredientRequest.getIngredientId();
            loadData(ingredientId);
        } else {
            processChanges();
        }
    }

    private boolean isNewRequest(String ingredientId) {
        return isNewRequest = !this.ingredientId.equals(ingredientId);
    }

    private void loadData(String ingredientId) {
        repository.getByDataId(ingredientId, this);
    }

    @Override
    public void onModelLoaded(IngredientPersistenceModel model) {
        persistenceModel = model;
        if (isEditable()) {
            validateData();
        } else {
            failReasons.add(FailReason.UNEDITABLE);
            buildResponse();
        }
    }

    @Override
    public void onModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);

        buildResponse();
    }

    private IngredientPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        id = idProvider.getUId();
        ingredientId = idProvider.getUId();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setId(id).
                setIngredientId(ingredientId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void processChanges() {
            validateData();
    }

    private void validateData() {
        checkForDuplicateName();
    }

    private void checkForDuplicateName() {
        duplicateNameChecker.checkForDuplicateAndNotify(
                request.getModel().getName(),
                request.getIngredientId(),

                duplicateId -> {
                    if (!NO_DUPLICATE_FOUND.equals(duplicateId)) {
                        failReasons.add(FailReason.DUPLICATE);
                    }
                    validateName();
                });
    }

    private void validateName() {
        String name;
        if (isNewRequest) {
            name = persistenceModel.getName();
        } else {
            name = requestModel.getName();
        }
        TextValidatorRequest request = new TextValidatorRequest(
                NAME_TEXT_TYPE,
                new TextValidatorModel(name)
        );
        handler.execute(textValidator, request, new UseCase.Callback<TextValidatorResponse>() {
            @Override
            public void onSuccess(TextValidatorResponse response) {
                validateDescription();
            }

            @Override
            public void onError(TextValidatorResponse response) {
                TextValidator.FailReason failReason = response.getFailReason();
                if (TextValidator.FailReason.TOO_SHORT.equals(failReason)) {
                    failReasons.add(FailReason.NAME_TOO_SHORT);

                } else if (TextValidator.FailReason.TOO_LONG == failReason) {
                    failReasons.add(FailReason.NAME_TOO_LONG);
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
                buildResponse();
            }

            @Override
            public void onError(TextValidatorResponse response) {
                TextValidator.FailReason failReason = response.getFailReason();
                if (TextValidator.FailReason.TOO_SHORT == failReason) {
                    failReasons.add(FailReason.DESCRIPTION_TOO_SHORT);
                } else {
                    failReasons.add(FailReason.DESCRIPTION_TOO_LONG);
                }
                buildResponse();
            }
        });
    }

    private void buildResponse() {
        IngredientResponse response = new IngredientResponse.Builder().
                setId(id).
                setIngredientId(ingredientId).
                setMetadata(getMetadata()).
                setModel(getResponseModel()).
                build();

        if (ComponentState.VALID_CHANGED == response.getMetadata().getState()) {
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
        return !isNewRequest && (isNameChanged() || isDescriptionChanged());
    }

    private boolean isEditable() {
        return Constants.getUserId().equals(persistenceModel.getUserId());
    }

    private boolean isNameChanged() {
        return !persistenceModel.getName().toLowerCase().trim().
                equals(request.getModel().getName().toLowerCase().trim());
    }

    private boolean isDescriptionChanged() {
        return !persistenceModel.getDescription().toLowerCase().trim().
                equals(request.getModel().getDescription().toLowerCase().trim());
    }

    private IngredientResponse.Model getResponseModel() {
        return new IngredientResponse.Model.Builder().
                setName(isNewRequest ?
                        persistenceModel.getName() :
                        requestModel.getName()).
                setDescription(isNewRequest ?
                        persistenceModel.getDescription() :
                        requestModel.getDescription()).
                setConversionFactor(isNewRequest ?
                        persistenceModel.getConversionFactor() :
                        requestModel.getConversionFactor()).
                build();
    }

    private IngredientPersistenceModel updatePersistenceFromRequestModel() {
        return IngredientPersistenceModel.Builder.
                basedOnPersistenceModel(persistenceModel).
                setConversionFactor(persistenceModel.getConversionFactor()).
                setName(persistenceModel.getName()).
                setDescription(persistenceModel.getDescription()).
                build();
    }

    private void save(IngredientPersistenceModel model) {
        repository.save(model);
    }

    private void sendResponse(IngredientResponse response) {
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
}
