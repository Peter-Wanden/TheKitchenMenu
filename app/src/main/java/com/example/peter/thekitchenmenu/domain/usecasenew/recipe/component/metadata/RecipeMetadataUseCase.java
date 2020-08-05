package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata;

import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseResult;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;

import java.util.Set;

import javax.annotation.Nonnull;

public class RecipeMetadataUseCase extends UseCaseResult<
        DataAccessRecipeMetadata,
        RecipeMetadataUseCasePersistenceModel,
        RecipeMetadataUseCaseModel,
        RecipeMetadataUseCaseRequestModel,
        RecipeMetadataUseCaseResponseModel> {

    private static final String TAG = "tkm-" + RecipeMetadataUseCase.class.getSimpleName() + ": ";

    @Nonnull
    private final Set<RecipeComponentName> requiredComponentNames;
    @Nonnull
    private final Set<RecipeComponentName> additionalComponentNames;

    public RecipeMetadataUseCase(@Nonnull DataAccessRecipeMetadata dataAccess,
                                 @Nonnull RecipeMetadataDomainModelConverter converter,
                                 @Nonnull Set<RecipeComponentName> requiredComponentNames,
                                 @Nonnull Set<RecipeComponentName> additionalComponentNames) {
        super(dataAccess, converter);

        this.requiredComponentNames = requiredComponentNames;
        this.additionalComponentNames = additionalComponentNames;
    }

    @Override
    protected void beginProcessingDomainModel() {
        checkForMissingRequiredComponents();
        checkForInvalidComponentStates();

        useCaseModel = new RecipeMetadataUseCaseModel.Builder()
                .basedOnModel(useCaseModel)
                .setComponentState(getComponentState())
                .build();
    }

    private void checkForMissingRequiredComponents() {
        for (RecipeComponentName componentName : requiredComponentNames) {
            if (!domainModelHasRequiredComponent(componentName)) {
                addFailReasonsMissingRequiredComponents();
            }
        }
    }

    private boolean domainModelHasRequiredComponent(RecipeComponentName componentName) {
        return useCaseModel.getComponentStates().containsKey(componentName);
    }

    private void addFailReasonsMissingRequiredComponents() {
        if (!useCaseFailReasons.contains(RecipeMetadataUseCaseFailReason.MISSING_REQUIRED_COMPONENTS)) {
            useCaseFailReasons.add(RecipeMetadataUseCaseFailReason.MISSING_REQUIRED_COMPONENTS);
        }
    }

    private void checkForInvalidComponentStates() {
        ComponentState componentState;

        for (RecipeComponentName componentName : requiredComponentNames) {
            componentState = useCaseModel.getComponentStates().get(componentName);

            if (ComponentState.INVALID_UNCHANGED.equals(componentState) ||
                    ComponentState.INVALID_CHANGED.equals(componentState) ||
                    ComponentState.INVALID_DEFAULT.equals(componentState)) {
                addFailReasonInvalidComponents();
            }
        }

        // INVALID_DEFAULT state for an additional component means it is not being used, therefore
        // it is not classed as missing as it would be for a required component
        for (RecipeComponentName componentName : additionalComponentNames) {
            componentState = useCaseModel.getComponentStates().get(componentName);

            if (ComponentState.INVALID_UNCHANGED.equals(componentState) ||
                    ComponentState.INVALID_CHANGED.equals(componentState)) {
                addFailReasonInvalidComponents();
            }
        }
    }

    private void addFailReasonInvalidComponents() {
        if (!useCaseFailReasons.contains(RecipeMetadataUseCaseFailReason.INVALID_COMPONENTS)) {
            useCaseFailReasons.add(RecipeMetadataUseCaseFailReason.INVALID_COMPONENTS);
        }
    }
}
