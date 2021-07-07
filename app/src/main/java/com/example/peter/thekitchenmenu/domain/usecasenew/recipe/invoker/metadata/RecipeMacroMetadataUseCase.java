package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeMetadataUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseResult;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.RecipeMacroUseCaseFailReason;

import java.util.Set;

import javax.annotation.Nonnull;

public class RecipeMacroMetadataUseCase extends UseCaseResult<
        RecipeMetadataUseCaseDataAccess,
        RecipeMacroMetadataUseCasePersistenceModel,
        RecipeMacroMetadataUseCaseModel,
        RecipeMacroMetadataUseCaseRequestModel,
        RecipeMacroMetadataUseCaseResponseModel> {

    private static final String TAG = "tkm-" + RecipeMacroMetadataUseCase.class.getSimpleName() + ": ";

    @Nonnull
    private final Set<RecipeComponentNameName> requiredComponentNames;
    @Nonnull
    private final Set<RecipeComponentNameName> additionalComponentNames;

    public RecipeMacroMetadataUseCase(@Nonnull RecipeMetadataUseCaseDataAccess dataAccess,
                                      @Nonnull RecipeMacroMetadataDomainModelConverter converter,
                                      @Nonnull Set<RecipeComponentNameName> requiredComponentNames,
                                      @Nonnull Set<RecipeComponentNameName> additionalComponentNames) {
        super(dataAccess, converter);

        this.requiredComponentNames = requiredComponentNames;
        this.additionalComponentNames = additionalComponentNames;
    }

    @Override
    protected void beginProcessingDomainModel() {
        checkForMissingRequiredComponents();
        checkForInvalidComponentStates();

        useCaseModel = new RecipeMacroMetadataUseCaseModel.Builder()
                .basedOnModel(useCaseModel)
                .setComponentState(getComponentState())
                .build();

        domainModelProcessingComplete();
    }

    private void checkForMissingRequiredComponents() {
        for (RecipeComponentNameName componentName : requiredComponentNames) {
            if (!domainModelHasRequiredComponent(componentName)) {
                addFailReasonsMissingRequiredComponents();
            }
        }
    }

    private boolean domainModelHasRequiredComponent(RecipeComponentNameName componentName) {
        return useCaseModel.getComponentStates().containsKey(componentName);
    }

    private void addFailReasonsMissingRequiredComponents() {
        if (!useCaseModel.getFailReasons().contains(RecipeMacroUseCaseFailReason.MISSING_REQUIRED_COMPONENTS)) {
            useCaseFailReasons.add(RecipeMacroUseCaseFailReason.MISSING_REQUIRED_COMPONENTS);
        }
    }

    private void checkForInvalidComponentStates() {
        ComponentState componentState;

        for (RecipeComponentNameName componentName : requiredComponentNames) {
            componentState = useCaseModel.getComponentStates().get(componentName);

            if (ComponentState.INVALID_UNCHANGED.equals(componentState) ||
                    ComponentState.INVALID_CHANGED.equals(componentState) ||
                    ComponentState.INVALID_DEFAULT.equals(componentState)) {
                addFailReasonInvalidComponents();
            }
        }

        // INVALID_DEFAULT state for an additional component means it is not being used, therefore
        // it is not classed as missing as it would be for a required component
        for (RecipeComponentNameName componentName : additionalComponentNames) {
            componentState = useCaseModel.getComponentStates().get(componentName);

            if (ComponentState.INVALID_UNCHANGED.equals(componentState) ||
                    ComponentState.INVALID_CHANGED.equals(componentState)) {
                addFailReasonInvalidComponents();
            }
        }
    }

    private void addFailReasonInvalidComponents() {
        if (!useCaseFailReasons.contains(RecipeMacroUseCaseFailReason.INVALID_COMPONENTS)) {
            useCaseFailReasons.add(RecipeMacroUseCaseFailReason.INVALID_COMPONENTS);
        }
    }
}
