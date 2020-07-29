package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.ingredient.DataAccessIngredient;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
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

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.TextType;

public class Ingredient
        extends UseCaseBase
        implements DomainDataAccess.GetDomainModelCallback<IngredientPersistenceModel> {

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

    static final String NO_ID = "";
    private static final TextType NAME_TEXT_TYPE = TextType.SHORT_TEXT;
    private static final TextType DESCRIPTION_TEXT_TYPE = TextType.LONG_TEXT;

    @Nonnull
    private final DataAccessIngredient repository;
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

    private String dataId = "";
    private String ingredientId = "";
    private boolean isNewRequest;

    private IngredientRequest.Model requestModel;
    private IngredientPersistenceModel persistenceModel;

    public Ingredient(@Nonnull DataAccessIngredient repository,
                      @Nonnull UniqueIdProvider idProvider,
                      @Nonnull TimeProvider timeProvider,
                      @Nonnull IngredientDuplicateChecker duplicateNameChecker,
                      @Nonnull TextValidator textValidator) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.duplicateNameChecker = duplicateNameChecker;
        this.textValidator = textValidator;

        requestModel = new IngredientRequest.Model.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        IngredientRequest r = (IngredientRequest) request;
        requestModel = r.getDomainModel();
        System.out.println(TAG + r);

        if (isNewRequest(r)) {
            dataId = r.getDataId();
            ingredientId = r.getDomainId();
            loadData(ingredientId);
        } else {
            setupUseCase();
            processChanges();
        }
    }

    private boolean isNewRequest(IngredientRequest r) {
        return isNewRequest = !r.getDomainId().equals(ingredientId);
    }

    private void loadData(String ingredientId) {
        repository.getByDomainId(ingredientId, this);
    }

    @Override
    public void onPersistenceModelLoaded(IngredientPersistenceModel model) {
        persistenceModel = model;
        dataId = model.getDataId();
        ingredientId = model.getDomainId();

        if (isEditable()) {
            validateData();
        } else {
            failReasons.add(FailReason.UNEDITABLE);
            buildResponse();
        }
    }

    @Override
    public void onPersistenceModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);

        buildResponse();
    }

    private IngredientPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        dataId = idProvider.getUId();

        return new IngredientPersistenceModel.Builder().
                getDefault().
                setDataId(dataId).
                setDomainId(ingredientId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void setupUseCase() {
        failReasons.clear();
        isNewRequest = false;
    }

    private void processChanges() {
        checkForDuplicateName();
    }

    private void checkForDuplicateName() {
        duplicateNameChecker.checkForDuplicateAndNotify(
                requestModel.getName(),
                ingredientId,

                duplicateId -> {
                    if (!NO_DUPLICATE_FOUND.equals(duplicateId)) {
                        failReasons.add(FailReason.DUPLICATE);
                    }
                    validateData();
                }
        );
    }

    private void validateData() {
        validateName();
    }

    private void validateName() {
        TextValidatorRequest request = new TextValidatorRequest(
                NAME_TEXT_TYPE,
                new TextValidatorModel(selectName())
        );
        textValidator.execute(request, new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onUseCaseSuccess(TextValidatorResponse response) {
                validateDescription();
            }

            @Override
            public void onUseCaseError(TextValidatorResponse response) {
                addNameFailReason(response.getFailReason());
                validateDescription();
            }
        });
    }

    private String selectName() {
        return isNewRequest ? persistenceModel.getName() : requestModel.getName();
    }

    private void addNameFailReason(FailReasons failReason) {
        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(FailReason.NAME_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(FailReason.NAME_TOO_LONG);
        }
    }

    private void validateDescription() {
        TextValidatorRequest request = new TextValidatorRequest(
                DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(selectDescription())
        );
        textValidator.execute(request, new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onUseCaseSuccess(TextValidatorResponse response) {
                if (failReasons.isEmpty()) {
                    failReasons.add(CommonFailReason.NONE);
                }
                buildResponse();
            }

            @Override
            public void onUseCaseError(TextValidatorResponse response) {
                addDescriptionFailReason(response.getFailReason());
                buildResponse();
            }
        });
    }

    private String selectDescription() {
        return isNewRequest ? persistenceModel.getDescription() : requestModel.getDescription();
    }

    private void addDescriptionFailReason(FailReasons failReason) {
        if (TextValidator.FailReason.TOO_SHORT == failReason) {
            failReasons.add(FailReason.DESCRIPTION_TOO_SHORT);
        } else {
            failReasons.add(FailReason.DESCRIPTION_TOO_LONG);
        }
    }

    private void buildResponse() {
        IngredientResponse.Builder builder = new IngredientResponse.Builder();
        builder.setDomainId(ingredientId);

        if (ComponentState.VALID_CHANGED == getComponentState()) {
            IngredientPersistenceModel m = updatePersistenceModel();
            builder.setMetadata(getMetadata(m));
            persistenceModel = m;
            save();

        } else {
            builder.setMetadata(getMetadata(persistenceModel));
        }
        builder.setDomainModel(getResponseModel());
        builder.setDataId(dataId);
        sendResponse(builder.build());
    }

    private UseCaseMetadataModel getMetadata(IngredientPersistenceModel m) {
        return new UseCaseMetadataModel.Builder().
                setComponentState(getComponentState()).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(m.getCreateDate()).
                setLastUpdate(m.getLastUpdate()).
                build();
    }

    private ComponentState getComponentState() {
        boolean isValid = failReasons.contains(CommonFailReason.NONE);

        return isValid ?
                (isChanged() ? ComponentState.VALID_CHANGED : ComponentState.VALID_UNCHANGED
                ) :
                (isChanged() ? ComponentState.INVALID_CHANGED : ComponentState.INVALID_UNCHANGED
                );
    }

    private boolean isChanged() {
        return !isNewRequest && (isNameChanged() || isDescriptionChanged());
    }

    private boolean isEditable() {
        return Constants.getUserId().equals(persistenceModel.getCreatedBy());
    }

    private boolean isNameChanged() {
        return !persistenceModel.getName().toLowerCase().trim().
                equals(requestModel.getName().toLowerCase().trim());
    }

    private boolean isDescriptionChanged() {
        return !persistenceModel.getDescription().toLowerCase().trim().
                equals(requestModel.getDescription().toLowerCase().trim());
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

    private IngredientPersistenceModel updatePersistenceModel() {
        return new IngredientPersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setDataId(idProvider.getUId()).
                setName(requestModel.getName()).
                setDescription(requestModel.getDescription()).
                setConversionFactor(requestModel.getConversionFactor()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
    }

    private void save() {
        repository.save(persistenceModel);
    }

    private void sendResponse(IngredientResponse response) {
        System.out.println(TAG + response);
        if (failReasons.contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            getUseCaseCallback().onUseCaseError(response);
        }
    }
}