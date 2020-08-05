package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeIdentityUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel.UseCaseModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCaseRequestModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCaseResponseModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator.TextType;

public class RecipeIdentity
        extends
        UseCaseElement<
                RecipeIdentityUseCaseDataAccess,
                RecipeIdentityUseCasePersistenceModel,
                RecipeIdentity.DomainModel> {

    private static final String TAG = "tkm-" + RecipeIdentity.class.getSimpleName() + ": ";

    protected static final class DomainModel
            implements
            UseCaseModel {
        @Nonnull
        private String title;
        @Nonnull
        private String description;

        private DomainModel(@Nonnull String title, @Nonnull String description) {
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

    public enum FailReason
            implements
            FailReasons {
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
    private final TextValidator textValidator;

    private boolean isTitleValidationComplete;
    private boolean isDescriptionValidationComplete;

    public RecipeIdentity(@Nonnull RecipeIdentityUseCaseDataAccess repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull TextValidator textValidator) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        useCaseModel = createUseCaseModelFromDefaultValues();

        this.textValidator = textValidator;
    }

    @Override
    protected DomainModel createUseCaseModelFromPersistenceModel(
            @Nonnull RecipeIdentityUseCasePersistenceModel persistenceModel) {

        return new DomainModel(
                persistenceModel.getTitle(),
                persistenceModel.getDescription()
        );
    }

    @Override
    protected DomainModel createUseCaseModelFromDefaultValues() {
        return new DomainModel("", "");
    }

    @Override
    protected DomainModel createUseCaseModelFromRequestModel() {
        RecipeIdentityUseCaseRequestModel requestModel = ((RecipeIdentityRequest) getRequest()).
                getDomainModel();

        return new DomainModel(
                requestModel.getTitle(),
                requestModel.getDescription()
        );
    }

    @Override
    protected void validateDomainModelElements() {
        validateTitle();
        validateDescription();

        boolean isValidationComplete = isTitleValidationComplete && isDescriptionValidationComplete;

        if (isValidationComplete) {
            save();
        }
    }

    private void validateTitle() {
        isTitleValidationComplete = false;
        TextValidatorRequest request = new TextValidatorRequest(
                TITLE_TEXT_TYPE,
                new TextValidatorModel(useCaseModel.title)
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
        isDescriptionValidationComplete = false;
        TextValidatorRequest request = new TextValidatorRequest(
                DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(useCaseModel.description)
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

    @Override
    protected void save() {
        if (isChanged && isDomainModelValid()) {

            useCaseDataId = idProvider.getUId();
            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archivePreviousState(currentTime);
            }

            persistenceModel = new RecipeIdentityUseCasePersistenceModel.Builder().
                    setDataId(useCaseDataId).
                    setDomainId(useCaseDomainId).
                    setTitle(useCaseModel.title).
                    setDescription(useCaseModel.description).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }
        buildResponse();
    }

    @Override
    protected void archivePreviousState(long currentTime) {
        RecipeIdentityUseCasePersistenceModel model = new RecipeIdentityUseCasePersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setLastUpdate(currentTime).
                build();

        System.out.println(TAG + "archivePersistenceModel= " + model);

        repository.save(model);
    }

    @Override
    protected void buildResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseModel());

        sendResponse(builder.build());
    }

    private RecipeIdentityUseCaseResponseModel getResponseModel() {
        return new RecipeIdentityUseCaseResponseModel.Builder().
                setTitle(useCaseModel.title).
                setDescription(useCaseModel.description).
                build();
    }
}
