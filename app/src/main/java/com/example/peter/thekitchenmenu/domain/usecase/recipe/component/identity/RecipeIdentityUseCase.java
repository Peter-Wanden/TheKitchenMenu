package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeIdentityUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity.TextLength;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseResult;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeIdentityUseCase
        extends
        UseCaseResult<
                        RecipeIdentityUseCaseDataAccess,
                        RecipeIdentityUseCasePersistenceModel,
                        RecipeIdentityUseCaseModel,
                        RecipeIdentityUseCaseRequestModel,
                        RecipeIdentityUseCaseResponseModel> {

    public enum FailReason
            implements
            FailReasons {
        TITLE_NULL(300),
        TITLE_TOO_SHORT(301),
        TITLE_TOO_LONG(302),
        DESCRIPTION_NULL(303),
        DESCRIPTION_TOO_SHORT(304),
        DESCRIPTION_TOO_LONG(305);

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

    private static final TextLength TITLE_TEXT_TYPE = TextLength.SHORT_TEXT;
    private static final TextLength DESCRIPTION_TEXT_TYPE = TextLength.LONG_TEXT;

    private final TextValidationBusinessEntity textValidator;
    private boolean isTitleValidationComplete;
    private boolean isDescriptionValidationComplete;

    public RecipeIdentityUseCase(RecipeIdentityUseCaseDataAccess dataAccess,
                                 RecipeIdentityDomainModelConverter modelConverter,
                                 UniqueIdProvider idProvider,
                                 TimeProvider timeProvider,
                                 TextValidationBusinessEntity textValidator) {
        super(dataAccess, modelConverter, idProvider, timeProvider);
        this.textValidator = textValidator;
    }

    @Override
    protected RecipeIdentityUseCaseModel createUseCaseModelFromDefaultValues() {
        return new RecipeIdentityUseCaseModel("", "");
    }

    @Override
    protected void isDomainDataElementsProcessed() {
        validateTitle();
        validateDescription();

        if (isTitleValidationComplete && isDescriptionValidationComplete) {
            buildResponse();
        }
    }

    private void validateTitle() {
        isTitleValidationComplete = false;

        textValidator.execute(new BusinessEntity.Request<>(new TextValidationModel(
                        TITLE_TEXT_TYPE, useCaseModel.getTitle())),
                response -> {
                    isTitleValidationComplete = true;
                    addTitleFailReasonsFromTextValidator(response.getFailReasons());
                    processResult();
                }
        );
    }

    private void addTitleFailReasonsFromTextValidator(List<FailReasons> failReasons) {
        if (failReasons.contains(TextValidationBusinessEntity.FailReason.TEXT_NULL)) {
            failReasons.add(FailReason.TITLE_NULL);
        } else if (failReasons.contains(TextValidationBusinessEntity.FailReason.TEXT_TOO_SHORT)) {
            failReasons.add(FailReason.TITLE_TOO_SHORT);
        } else if (failReasons.contains(TextValidationBusinessEntity.FailReason.TEXT_TOO_LONG)) {
            failReasons.add(FailReason.TITLE_TOO_LONG);
        }
    }

    private void validateDescription() {
        isDescriptionValidationComplete = false;

        textValidator.execute(new BusinessEntity.Request<>(new TextValidationModel(
                        DESCRIPTION_TEXT_TYPE, useCaseModel.getDescription())),
                response -> {
                    isDescriptionValidationComplete = true;
                    addDescriptionFailReasonsFromTextValidator(response.getFailReasons());
                    processResult();
                }
        );
    }

    private void addDescriptionFailReasonsFromTextValidator(List<FailReasons> failReasons) {
        if (failReasons.contains(TextValidationBusinessEntity.FailReason.TEXT_NULL)) {
            failReasons.add(FailReason.DESCRIPTION_NULL);
        } else if (failReasons.contains(TextValidationBusinessEntity.FailReason.TEXT_TOO_SHORT)) {
            failReasons.add(FailReason.DESCRIPTION_TOO_SHORT);
        } else if (failReasons.contains(TextValidationBusinessEntity.FailReason.TEXT_TOO_LONG)) {
            failReasons.add(FailReason.DESCRIPTION_TOO_LONG);
        }
    }

    private void processResult() {
        if (isChanged && isDomainModelValid()) {
            archivePreviousPersistenceModel();
            createNewPersistenceModel();
        }
        buildResponse();
    }

    private void buildResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(converter.convertUseCaseToResponseModel(useCaseModel));

        sendResponse(builder.build());
    }
}