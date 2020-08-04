package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipePortionsUseCasseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseResult;

import javax.annotation.Nonnull;

public class RecipePortionsUseCase
        extends
        UseCaseResult<
                RecipePortionsUseCasseDataAccess,
                RecipePortionsUseCasePersistenceModel,
                RecipePortionsUseCaseModel,
                RecipePortionsUseCaseRequestModel,
                RecipePortionsUseCaseResponseModel> {

    static final int MIN_SERVINGS = 1;
    static final int MIN_SITTINGS = 1;
    private final int maxServings;
    private final int maxSittings;

    public RecipePortionsUseCase(@Nonnull RecipePortionsUseCasseDataAccess dataAccess,
                                 @Nonnull RecipePortionsDomainModelConverter converter,
                                 int maxServings,
                                 int maxSittings) {
        super(dataAccess, converter);

        this.maxServings = maxServings;
        this.maxSittings = maxSittings;
    }

    @Override
    protected void beginProcessingDomainModel() {
        validateServings();
        validateSittings();
        domainModelProcessingComplete();
    }

    private void validateServings() {
        if (useCaseModel.getServings() < MIN_SERVINGS) {
            useCaseFailReasons.add(RecipePortionsUseCaseFailReason.SERVINGS_TOO_LOW);

        } else if (useCaseModel.getServings() > maxServings) {
            useCaseFailReasons.add(RecipePortionsUseCaseFailReason.SERVINGS_TOO_HIGH);
        }
    }

    private void validateSittings() {
        if (useCaseModel.getSittings() < MIN_SITTINGS) {
            useCaseFailReasons.add(RecipePortionsUseCaseFailReason.SITTINGS_TOO_LOW);

        } else if (useCaseModel.getSittings() > maxSittings) {
            useCaseFailReasons.add(RecipePortionsUseCaseFailReason.SITTINGS_TOO_HIGH);
        }
    }

    @Override
    protected RecipePortionsUseCaseModel createUseCaseModelFromDefaultValues() {
        return new RecipePortionsUseCaseModel(MIN_SERVINGS, MIN_SITTINGS);
    }
}
