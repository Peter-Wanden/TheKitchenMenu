package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
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

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.TextType;

public class RecipeIdentity
        extends
        UseCaseElement<RecipeIdentityRequest.DomainModel>
        implements
        DomainDataAccess.GetDomainModelCallback<RecipeIdentityPersistenceModel> {

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
    private final TextValidator textValidator;
    @Nonnull
    private final List<FailReasons> failReasons;

    private RecipeIdentityPersistenceModel persistenceModel;

    public RecipeIdentity(@Nonnull RepositoryRecipeIdentity repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull TextValidator textValidator) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.textValidator = textValidator;

        requestDomainModel = new RecipeIdentityRequest.DomainModel.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected void loadDataByDataId() {
        repository.getByDataId(useCaseDataId, this);
    }

    @Override
    protected void loadDataByDomainId() {
        repository.getActiveByDomainId(useCaseDomainId, this);
    }

    @Override
    public void dataSourceOnDomainModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        processUseCaseDomainData();
    }

    private RecipeIdentityPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        useCaseDataId = idProvider.getUId();

        return new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    @Override
    public void dataSourceOnDomainModelLoaded(RecipeIdentityPersistenceModel persistenceModel) {
        this.persistenceModel = persistenceModel;
        useCaseDataId = persistenceModel.getDataId();
        processUseCaseDomainData();
    }

    @Override
    protected void processRequestDomainData() {

    }

    @Override
    protected void processUseCaseDomainData() {
        setupUseCase();
        validateDomainData();
        buildResponse();
    }

    private void setupUseCase() {
        failReasons.clear();
    }

    private void validateDomainData() {
        validateTitle();
    }

    private void validateTitle() {
        TextValidatorRequest request = new TextValidatorRequest(
                TITLE_TEXT_TYPE,
                new TextValidatorModel(getTitle())
        );
        textValidator.execute(request, new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onUseCaseSuccess(TextValidatorResponse response) {
                validateDescription();
            }

            @Override
            public void onUseCaseError(TextValidatorResponse response) {
                addTitleFailReasonFromTextValidator(response.getFailReason());
                validateDescription();
            }
        });
    }

    private String getTitle() {
        return isNewRequest ? persistenceModel.getTitle() : requestDomainModel.getTitle();
    }

    private void addTitleFailReasonFromTextValidator(FailReasons failReason) {
        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(FailReason.TITLE_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(FailReason.TITLE_TOO_LONG);
        }
    }

    private void validateDescription() {
        TextValidatorRequest request = new TextValidatorRequest(
                DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(getDescription())
        );
        textValidator.execute(request, new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onUseCaseSuccess(TextValidatorResponse response) {
                if (failReasons.isEmpty()) {
                    failReasons.add(CommonFailReason.NONE);
                }
            }

            @Override
            public void onUseCaseError(TextValidatorResponse response) {
                addDescriptionFailReasonsFromTextValidator(response.getFailReason());
            }
        });
    }

    private String getDescription() {
        return isNewRequest ? persistenceModel.getDescription() : requestDomainModel.getDescription();
    }

    private void addDescriptionFailReasonsFromTextValidator(FailReasons failReason) {
        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(FailReason.DESCRIPTION_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(FailReason.DESCRIPTION_TOO_LONG);
        }
    }

    protected void buildResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder();
        builder.setDomainId(useCaseDomainId);

        if (ComponentState.VALID_CHANGED == getComponentState()) {
            RecipeIdentityPersistenceModel m = updatePersistenceModel();
            builder.setMetadata(getMetadata(m));
            persistenceModel = m;
            save();

        } else {
            builder.setMetadata(getMetadata(persistenceModel));
        }

        builder.setDomainModel(getResponseDomainModel());
        builder.setDataId(useCaseDataId);

        sendResponse(builder.build());
    }

    private UseCaseMetadataModel getMetadata(RecipeIdentityPersistenceModel m) {
        return new UseCaseMetadataModel.Builder().
                setState(getComponentState()).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(m.getCreateDate()).
                setLasUpdate(m.getLastUpdate()).
                build();
    }

    private ComponentState getComponentState() {
        boolean isValid = failReasons.contains(CommonFailReason.NONE);

        return isValid
                ?
                (isDomainDataChanged() ?
                        ComponentState.VALID_CHANGED :
                        ComponentState.VALID_UNCHANGED)
                :
                (isDomainDataChanged() ?
                        ComponentState.INVALID_CHANGED :
                        ComponentState.INVALID_UNCHANGED);
    }

    @Override
    protected boolean isDomainDataChanged() {
        return !isNewRequest && (isTitleChanged() || isDescriptionChanged());
    }

    private boolean isTitleChanged() {
        return !persistenceModel.getTitle().toLowerCase().
                equals(requestDomainModel.getTitle().toLowerCase().trim());
    }

    private boolean isDescriptionChanged() {
        return !persistenceModel.getDescription().toLowerCase().trim().
                equals(requestDomainModel.getDescription().toLowerCase().trim());
    }

    private RecipeIdentityPersistenceModel updatePersistenceModel() {
        useCaseDataId = idProvider.getUId();
        return new RecipeIdentityPersistenceModel.Builder().
                basedOnPersistenceModel(persistenceModel).
                setDataId(useCaseDataId).
                setTitle(requestDomainModel.getTitle()).
                setDescription(requestDomainModel.getDescription()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
    }

    private RecipeIdentityResponse.DomainModel getResponseDomainModel() {
        return new RecipeIdentityResponse.DomainModel.Builder().
                setTitle(isNewRequest ?
                        persistenceModel.getTitle() :
                        requestDomainModel.getTitle()).

                setDescription(isNewRequest ?
                        persistenceModel.getDescription() :
                        requestDomainModel.getDescription()).
                build();
    }

    private void sendResponse(RecipeIdentityResponse r) {
        System.out.println(TAG + "Response No:" + accessCount + " - " + r);
        List<FailReasons> failReasons = r.getMetadata().getFailReasons();

        if (failReasons.contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onUseCaseSuccess(r);
        } else {
            getUseCaseCallback().onUseCaseError(r);
        }
    }

    private void save() {
        repository.save(persistenceModel);
    }
}
