package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeDurationUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseResult;

public class RecipeDurationUseCase
        extends
        UseCaseResult<
                RecipeDurationUseCaseDataAccess,
                RecipeDurationUseCasePersistenceModel,
                RecipeDurationUseCaseModel,
                RecipeDurationUseCaseRequestModel,
                RecipeDurationUseCaseResponseModel> {

    private static final String TAG = "tkm-" + RecipeDurationUseCase.class.getSimpleName() + ": ";

    public static final int MIN_PREP_TIME = 0;
    public static final int MIN_COOK_TIME = 0;
    private final int MAX_PREP_TIME;
    private final int MAX_COOK_TIME;

    public RecipeDurationUseCase(RecipeDurationUseCaseDataAccess dataAccess,
                                 RecipeDurationDomainModelConverter converter,
                                 int maxPrepTime,
                                 int maxCookTime) {
        super(dataAccess, converter);

        MAX_PREP_TIME = maxPrepTime;
        MAX_COOK_TIME = maxCookTime;
    }

    @Override
    protected void beginProcessingDomainModel() {
        validatePrepTime();
        validateCookTime();

        domainModelProcessingComplete();
    }

    private void validatePrepTime() {
        if (useCaseModel.getPrepTime() > MAX_PREP_TIME) {
            useCaseFailReasons.add(RecipeDurationUseCaseFailReason.INVALID_PREP_TIME);
        }
    }

    private void validateCookTime() {
        if (useCaseModel.getCookTime() > MAX_COOK_TIME) {
            useCaseFailReasons.add(RecipeDurationUseCaseFailReason.INVALID_COOK_TIME);
        }
    }
}
