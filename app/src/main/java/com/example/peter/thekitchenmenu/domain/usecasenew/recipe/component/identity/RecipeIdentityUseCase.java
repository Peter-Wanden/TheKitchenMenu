package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeIdentityUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.businessentity.EntityRequest;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationFailReason;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity.TextLength;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseResult;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.List;

public class RecipeIdentityUseCase
        extends
        UseCaseResult<
                RecipeIdentityUseCaseDataAccess,
                RecipeIdentityUseCasePersistenceModel,
                RecipeIdentityUseCaseModel,
                RecipeIdentityUseCaseRequestModel,
                RecipeIdentityUseCaseResponseModel> {

    public static final String TAG = "tkm-" + RecipeIdentityUseCase.class.getSimpleName() + ": ";

    private static final TextLength TITLE_TEXT_TYPE = TextLength.SHORT_TEXT;
    private static final TextLength DESCRIPTION_TEXT_TYPE = TextLength.LONG_TEXT;

    private final TextValidationBusinessEntity textValidator;
    private boolean isTitleValidationComplete;
    private boolean isDescriptionValidationComplete;

    public RecipeIdentityUseCase(RecipeIdentityUseCaseDataAccess dataAccess,
                                 RecipeIdentityDomainModelConverter modelConverter,
                                 TextValidationBusinessEntity textValidator) {
        super(dataAccess, modelConverter);
        this.textValidator = textValidator;
    }

    @Override
    protected void beginProcessingDomainModel() {
        validateTitle();
        validateDescription();

        if (isTitleValidationComplete && isDescriptionValidationComplete) {
            domainModelProcessingComplete();
        }
    }

    @Override
    protected RecipeIdentityUseCaseModel createUseCaseModelFromDefaultValues() {
        return new RecipeIdentityUseCaseModel("", "");
    }

    private void validateTitle() {
        isTitleValidationComplete = false;

        textValidator.execute(new EntityRequest<>(new TextValidationModel(
                        TITLE_TEXT_TYPE, useCaseModel.getTitle())),
                response -> {
                    isTitleValidationComplete = true;
                    addTitleFailReasonsFromTextValidator(response.getFailReasons());
                }
        );
    }

    private void addTitleFailReasonsFromTextValidator(List<FailReasons> failReasons) {
        if (failReasons.contains(TextValidationFailReason.TEXT_NULL)) {
            this.failReasons.add(RecipeIdentityUseCaseFailReason.TITLE_NULL);
        } else if (failReasons.contains(TextValidationFailReason.TEXT_TOO_SHORT)) {
            this.failReasons.add(RecipeIdentityUseCaseFailReason.TITLE_TOO_SHORT);
        } else if (failReasons.contains(TextValidationFailReason.TEXT_TOO_LONG)) {
            this.failReasons.add(RecipeIdentityUseCaseFailReason.TITLE_TOO_LONG);
        }
    }

    private void validateDescription() {
        isDescriptionValidationComplete = false;

        textValidator.execute(new EntityRequest<>(new TextValidationModel(
                        DESCRIPTION_TEXT_TYPE, useCaseModel.getDescription())),
                response -> {
                    isDescriptionValidationComplete = true;
                    addDescriptionFailReasonsFromTextValidator(response.getFailReasons());
                }
        );
    }

    private void addDescriptionFailReasonsFromTextValidator(List<FailReasons> failReasons) {
        if (failReasons.contains(TextValidationFailReason.TEXT_NULL)) {
            this.failReasons.add(RecipeIdentityUseCaseFailReason.DESCRIPTION_NULL);
        } else if (failReasons.contains(TextValidationFailReason.TEXT_TOO_SHORT)) {
            this.failReasons.add(RecipeIdentityUseCaseFailReason.DESCRIPTION_TOO_SHORT);
        } else if (failReasons.contains(TextValidationFailReason.TEXT_TOO_LONG)) {
            this.failReasons.add(RecipeIdentityUseCaseFailReason.DESCRIPTION_TOO_LONG);
        }
    }
}