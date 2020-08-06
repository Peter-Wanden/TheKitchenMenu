package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeMetadataUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.RecipeMacroUseCaseFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata.RecipeMacroMetadataUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Calculates and stores {@link Recipe} metadata state information based on collective recipe
 * component metadata values.
 */
public class RecipeMetadata
        extends
        UseCaseElement<
                RecipeMetadataUseCaseDataAccess,
                RecipeMacroMetadataUseCasePersistenceModel,
                RecipeMetadataUseCaseModel> {

    private static final String TAG = "tkm-" + RecipeMetadata.class.getSimpleName() + ": ";

    @Nonnull
    private final Set<RecipeComponentNameName> requiredComponentNames;
    @Nonnull
    private final Set<RecipeComponentNameName> additionalComponentNames;

    private ComponentState recipeState;

    public RecipeMetadata(@Nonnull RecipeMetadataUseCaseDataAccess repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull Set<RecipeComponentNameName> requiredComponentNames,
                          @Nonnull Set<RecipeComponentNameName> additionalComponentNames) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        this.requiredComponentNames = requiredComponentNames;
        this.additionalComponentNames = additionalComponentNames;

        useCaseModel = createUseCaseModelFromDefaultValues();
    }

    @Override
    public void onPersistenceModelUnavailable() {
        isChanged = true; // Prompts an initial save for a new recipe
        super.onPersistenceModelUnavailable();
    }

    @Override
    protected RecipeMetadataUseCaseModel createUseCaseModelFromDefaultValues() {
        HashMap<RecipeComponentNameName, ComponentState> defaultComponentStates = new HashMap<>();
        requiredComponentNames.forEach(componentName ->
                defaultComponentStates.put(componentName, ComponentState.INVALID_DEFAULT)
        );

        RecipeMetadataUseCaseModel defaultDomainModel = new RecipeMetadataUseCaseModel("", defaultComponentStates);

        return new RecipeMetadataUseCaseModel("", new HashMap<>());
    }

    @Override
    protected RecipeMetadataUseCaseModel createUseCaseModelFromPersistenceModel(
            @Nonnull RecipeMacroMetadataUseCasePersistenceModel persistenceModel) {

        recipeState = persistenceModel.getComponentState();
        failReasons.addAll(persistenceModel.getFailReasons());

        return new RecipeMetadataUseCaseModel("", persistenceModel.getComponentStates());
    }

    @Override
    protected RecipeMetadataUseCaseModel createUseCaseModelFromRequestModel() {
        RecipeMetadataRequest.DomainModel model = ((RecipeMetadataRequest) getRequest()).
                getDomainModel();

        return new RecipeMetadataUseCaseModel("", model.getComponentStates());
    }

    @Override
    protected void validateDomainModelElements() {
        checkForMissingRequiredComponents();
        checkForInvalidComponentStates();
        recipeState = getComponentState();
        save();
    }

    private void checkForMissingRequiredComponents() {
        for (RecipeComponentNameName componentName : requiredComponentNames) {
            if (!domainModelHasRequiredComponent(componentName)) {
                addFailReasonsMissingRequiredComponents();
            }
        }

        System.out.println(TAG + "checkForMissingRequiredComponents: " +
                "\n  - required component names= " + requiredComponentNames +
                "\n  - domainModel.componentStates= " + useCaseModel.getComponentStates());
    }

    private boolean domainModelHasRequiredComponent(RecipeComponentNameName componentName) {
        return useCaseModel.getComponentStates().containsKey(componentName);
    }

    private void addFailReasonsMissingRequiredComponents() {
        if (!failReasons.contains(RecipeMacroUseCaseFailReason.MISSING_REQUIRED_COMPONENTS)) {
            failReasons.add(RecipeMacroUseCaseFailReason.MISSING_REQUIRED_COMPONENTS);
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
        if (!failReasons.contains(RecipeMacroUseCaseFailReason.INVALID_COMPONENTS)) {
            failReasons.add(RecipeMacroUseCaseFailReason.INVALID_COMPONENTS);
        }
    }

    @Override
    protected void save() {
        if (!isDefaultDomainModel()) {
            if (isChanged) {

                useCaseDataId = idProvider.getUId();
                long currentTime = timeProvider.getCurrentTimeInMills();

                if (persistenceModel != null) {
                    archivePreviousState(currentTime);
                }

                persistenceModel = new RecipeMacroMetadataUseCasePersistenceModel.Builder().
                        setDataId(useCaseDataId).
                        setDomainId(useCaseDomainId).
                        setComponentState(recipeState).
                        setComponentStates(useCaseModel.getComponentStates()).
                        setFailReasons(failReasons).
                        setCreateDate(currentTime).
                        setLastUpdate(currentTime).
                        build();

                System.out.println(TAG + "save:" + persistenceModel);

                repository.save(persistenceModel);
            }
        }
        buildResponse();
    }

    @Override
    protected void archivePreviousState(long currentTime) {
        RecipeMacroMetadataUseCasePersistenceModel model = new RecipeMacroMetadataUseCasePersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setLastUpdate(currentTime).
                build();

        System.out.println(TAG + "archiveExistingPersistenceModel:" + model);
        repository.save(model);
    }

    protected void buildResponse() {
        RecipeMetadataResponse response = new RecipeMetadataResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseDomainModel()).
                build();

        sendResponse(response);
    }

    private RecipeMetadataResponse.DomainModel getResponseDomainModel() {
        return new RecipeMetadataResponse.DomainModel.Builder().
                setComponentStates(new LinkedHashMap<>(useCaseModel.getComponentStates())).
                build();
    }
}
