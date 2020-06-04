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
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.TextType;

public class RecipeIdentity
        extends
        UseCaseElement<RecipeIdentityRequest.DomainModel>
        implements
        DomainDataAccess.GetDomainModelCallback<RecipeIdentityPersistenceModel> {

    private static final String TAG = "tkm-" + RecipeIdentity.class.getSimpleName() + ": ";

    private static final class DomainModel {
        @Nonnull
        private String title = "";
        @Nonnull
        private String description = "";

        public DomainModel() {
        }

        public DomainModel(@Nonnull String title, @Nonnull String description) {
            this.title = title;
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DomainModel)) return false;
            DomainModel that = (DomainModel) o;
            return title.equals(that.title) &&
                    description.equals(that.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, description);
        }

        @Nonnull
        @Override
        public String toString() {
            return "DomainModel{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

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

    private DomainModel currentDomainModel;
    private DomainModel newDomainModel;

    private RecipeIdentityPersistenceModel persistenceModel;

    private boolean isChanged;
    private boolean isTitleValidationComplete;
    private boolean isDescriptionValidationComplete;

    public RecipeIdentity(@Nonnull RepositoryRecipeIdentity repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull TextValidator textValidator) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.textValidator = textValidator;

        currentDomainModel = new DomainModel();
        newDomainModel = new DomainModel();

        requestDomainModel = new RecipeIdentityRequest.DomainModel.Builder().getDefault().build();
    }

    @Override
    protected void loadDomainModelByDataId() {
        repository.getByDataId(useCaseDataId, this);
    }

    @Override
    protected void loadDomainModelByDomainId() {
        repository.getActiveByDomainId(useCaseDomainId, this);
    }

    @Override
    public void dataSourceOnDomainModelUnavailable() {
        isDomainDataUnavailable = true;
        newDomainModel = new DomainModel();
        reprocessCurrentDomainModel();
    }

    @Override
    public void dataSourceOnDomainModelLoaded(RecipeIdentityPersistenceModel persistenceModel) {
        isDomainDataUnavailable = false;
        this.persistenceModel = persistenceModel;
        useCaseDataId = persistenceModel.getDataId();
        useCaseDomainId = persistenceModel.getDomainId();
        newDomainModel = getNewDomainModelFromPersistenceModel(persistenceModel);
        currentDomainModel = newDomainModel;
        processNewDomainModel();
    }

    private DomainModel getNewDomainModelFromPersistenceModel(
            RecipeIdentityPersistenceModel persistenceModel) {
        return new DomainModel(persistenceModel.getTitle(), persistenceModel.getDescription());
    }

    @Override
    protected void processRequestDomainModel() {
        newDomainModel = getNewDomainModelFromRequestData();
        processNewDomainModel();
    }

    private DomainModel getNewDomainModelFromRequestData() {
        RecipeIdentityRequest.DomainModel requestModel = ((RecipeIdentityRequest) getRequest()).
                getDomainModel();
        return new DomainModel(requestModel.getTitle(), requestModel.getDescription());
    }

    private void processNewDomainModel() {
        System.out.println(TAG + "processDomainData:");
        isChanged = !currentDomainModel.equals(newDomainModel);

        if (!isChanged) {
            newDomainModel = currentDomainModel;
        }
        validateNewDomainModelElements();
    }

    @Override
    protected void reprocessCurrentDomainModel() {
        System.out.println(
                TAG + "reprocessCurrentDomainData called"
        );
        newDomainModel = currentDomainModel;
        processNewDomainModel();
    }

    private void validateNewDomainModelElements() {
        System.out.println(
                TAG + "validateDomainDataElements: new domain model=" + newDomainModel
        );
        setupDomainModelProcessing();

        validateTitle();
        validateDescription();
    }

    private void setupDomainModelProcessing() {
        failReasons.clear();
        isTitleValidationComplete = false;
        isDescriptionValidationComplete = false;
    }

    private void validateTitle() {
        TextValidatorRequest request = new TextValidatorRequest(
                TITLE_TEXT_TYPE,
                new TextValidatorModel(newDomainModel.title)
        );
        textValidator.execute(request, new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onUseCaseSuccess(TextValidatorResponse response) {
                processResults();
            }

            @Override
            public void onUseCaseError(TextValidatorResponse response) {
                addTitleFailReasonFromTextValidator(response.getFailReason());
                processResults();
            }

            private void processResults() {
                isTitleValidationComplete = true;
                processDomainModelValidationResults();
            }
        });
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
                new TextValidatorModel(newDomainModel.description)
        );
        textValidator.execute(request, new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onUseCaseSuccess(TextValidatorResponse response) {
                processResults();
            }

            @Override
            public void onUseCaseError(TextValidatorResponse response) {
                addDescriptionFailReasonsFromTextValidator(response.getFailReason());
                processResults();
            }

            private void processResults() {
                isDescriptionValidationComplete = true;
                processDomainModelValidationResults();
            }
        });
    }

    private void addDescriptionFailReasonsFromTextValidator(FailReasons failReason) {
        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(FailReason.DESCRIPTION_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(FailReason.DESCRIPTION_TOO_LONG);
        }
    }

    private void processDomainModelValidationResults() {
        if (isTitleValidationComplete && isDescriptionValidationComplete) {
            if (failReasons.isEmpty()) {
                failReasons.add(CommonFailReason.NONE);
            }
            if (isDomainModelValid() && isChanged) {
                save();
            }
            buildResponse();
        }
    }

    protected void buildResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseDomainModel());

        sendResponse(builder.build());
    }

    protected UseCaseMetadataModel getMetadata() {
        return new UseCaseMetadataModel.Builder().
                setState(getComponentState()).
                setFailReasons(new ArrayList<>(getFailReasons())).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(persistenceModel == null ? 0L : persistenceModel.getCreateDate()).
                setLasUpdate(persistenceModel == null ? 0L : persistenceModel.getLastUpdate()).
                build();
    }

    private RecipeIdentityResponse.DomainModel getResponseDomainModel() {
        currentDomainModel = newDomainModel;
        return new RecipeIdentityResponse.DomainModel.Builder().
                setTitle(currentDomainModel.title).
                setDescription(currentDomainModel.description).
                build();
    }

    private void save() {
        currentDomainModel = newDomainModel;
        boolean hasExistingPersistenceModel = persistenceModel != null;

        if (hasExistingPersistenceModel) {
            archiveExistingPersistenceModel();
        }
        saveNewPersistenceModel();
    }

    private void saveNewPersistenceModel() {
        useCaseDataId = idProvider.getUId();
        long currentTime = timeProvider.getCurrentTimeInMills();
        persistenceModel = new RecipeIdentityPersistenceModel.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setTitle(currentDomainModel.title).
                setDescription(currentDomainModel.description).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();

        repository.save(persistenceModel);
        isDomainDataUnavailable = false;
    }

    private void archiveExistingPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        RecipeIdentityPersistenceModel persistenceModel = new RecipeIdentityPersistenceModel.
                Builder().
                basedOnPersistenceModel(this.persistenceModel).
                setLastUpdate(currentTime).
                build();
        repository.save(persistenceModel);
    }
}
