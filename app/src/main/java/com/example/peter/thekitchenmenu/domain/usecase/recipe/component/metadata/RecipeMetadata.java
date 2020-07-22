package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId.NO_ID;

/**
 * Calculates and stores {@link Recipe} metadata state information based on use case response values
 */
public class RecipeMetadata
        extends
        UseCaseElement<
                DataAccessRecipeMetadata,
                RecipeMetadataPersistenceModel,
                RecipeMetadata.DomainModel> {

    private static final String TAG = "tkm-" + RecipeMetadata.class.getSimpleName() + ": ";

    protected static final class DomainModel
            implements
            com.example.peter.thekitchenmenu.domain.model.DomainModel.UseCaseModel {

        private String parentDomainId;
        private HashMap<ComponentName, UseCaseMetadata.ComponentState> componentStates;

        private DomainModel(String parentDomainId,
                            HashMap<ComponentName, UseCaseMetadata.ComponentState> componentStates) {

            this.parentDomainId = parentDomainId;
            this.componentStates = componentStates;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DomainModel)) return false;

            DomainModel that = (DomainModel) o;

            if (!Objects.equals(parentDomainId, that.parentDomainId))
                return false;
            return Objects.equals(componentStates, that.componentStates);
        }

        @Override
        public int hashCode() {
            int result = parentDomainId != null ? parentDomainId.hashCode() : 0;
            result = 31 * result + (componentStates != null ? componentStates.hashCode() : 0);
            return result;
        }

        @Nonnull
        @Override
        public String toString() {
            return "DomainModel{" +
                    "parentDomainId='" + parentDomainId + '\'' +
                    ", componentStates=" + componentStates +
                    '}';
        }
    }

    public enum ComponentName {
        COURSE(1),
        DURATION(2),
        IDENTITY(3),
        PORTIONS(4),
        TEXT_VALIDATOR(5),
        RECIPE_METADATA(6),
        RECIPE(7);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, ComponentName> options = new HashMap<>();

        ComponentName(int id) {
            this.id = id;
        }

        static {
            for (ComponentName n : ComponentName.values())
                options.put(n.id, n);
        }

        public static ComponentName getFromId(int id) {
            return options.get(id);
        }

        public int getId() {
            return id;
        }

    }

    public enum FailReason implements FailReasons {
        MISSING_REQUIRED_COMPONENTS(400),
        INVALID_COMPONENTS(401);

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

    @Nonnull
    private final Set<ComponentName> requiredComponentNames;
    @Nonnull
    private final Set<ComponentName> additionalComponentNames;

    private UseCaseMetadata.ComponentState recipeState;

    public RecipeMetadata(@Nonnull DataAccessRecipeMetadata repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull Set<ComponentName> requiredComponentNames,
                          @Nonnull Set<ComponentName> additionalComponentNames) {

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
    protected DomainModel createUseCaseModelFromDefaultValues() {
        HashMap<ComponentName, UseCaseMetadata.ComponentState> defaultComponentStates = new HashMap<>();
        requiredComponentNames.forEach(componentName ->
                defaultComponentStates.put(componentName, UseCaseMetadata.ComponentState.INVALID_DEFAULT)
        );

        DomainModel defaultDomainModel = new DomainModel(NO_ID, defaultComponentStates);

        return new DomainModel(
                NO_ID,
                new HashMap<>()
        );
    }

    @Override
    protected DomainModel createUseCaseModelFromPersistenceModel(
            @Nonnull RecipeMetadataPersistenceModel persistenceModel) {

        recipeState = persistenceModel.getComponentState();
        failReasons.addAll(persistenceModel.getFailReasons());

        return new DomainModel(
                persistenceModel.getParentDomainId(),
                persistenceModel.getComponentStates()
        );
    }

    @Override
    protected DomainModel createUseCaseModelFromRequestModel() {
        RecipeMetadataRequest.DomainModel model = ((RecipeMetadataRequest) getRequest()).
                getDomainModel();

        return new DomainModel(
                model.getParentDomainId(),
                model.getComponentStates()
        );
    }

    @Override
    protected void validateDomainModelElements() {
        checkForMissingRequiredComponents();
        checkForInvalidComponentStates();
        recipeState = getComponentState();
        save();
    }

    private void checkForMissingRequiredComponents() {
        for (ComponentName componentName : requiredComponentNames) {
            if (!domainModelHasRequiredComponent(componentName)) {
                addFailReasonsMissingRequiredComponents();
            }
        }

        System.out.println(TAG + "checkForMissingRequiredComponents: " +
                "\n  - required component names= " + requiredComponentNames +
                "\n  - domainModel.componentStates= " + useCaseModel.componentStates);
    }

    private boolean domainModelHasRequiredComponent(ComponentName componentName) {
        return useCaseModel.componentStates.containsKey(componentName);
    }

    private void addFailReasonsMissingRequiredComponents() {
        if (!failReasons.contains(FailReason.MISSING_REQUIRED_COMPONENTS)) {
            failReasons.add(FailReason.MISSING_REQUIRED_COMPONENTS);
        }
    }

    private void checkForInvalidComponentStates() {
        UseCaseMetadata.ComponentState componentState;

        for (ComponentName componentName : requiredComponentNames) {
            componentState = useCaseModel.componentStates.get(componentName);

            if (UseCaseMetadata.ComponentState.INVALID_UNCHANGED.equals(componentState) ||
                    UseCaseMetadata.ComponentState.INVALID_CHANGED.equals(componentState) ||
                    UseCaseMetadata.ComponentState.INVALID_DEFAULT.equals(componentState)) {
                addFailReasonInvalidComponents();
            }
        }

        // INVALID_DEFAULT state for an additional component means it is not being used, therefore
        // it is not classed as missing as it would be for a required component
        for (ComponentName componentName : additionalComponentNames) {
            componentState = useCaseModel.componentStates.get(componentName);

            if (UseCaseMetadata.ComponentState.INVALID_UNCHANGED.equals(componentState) ||
                    UseCaseMetadata.ComponentState.INVALID_CHANGED.equals(componentState)) {
                addFailReasonInvalidComponents();
            }
        }
    }

    private void addFailReasonInvalidComponents() {
        if (!failReasons.contains(FailReason.INVALID_COMPONENTS)) {
            failReasons.add(FailReason.INVALID_COMPONENTS);
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

                persistenceModel = new RecipeMetadataPersistenceModel.Builder().
                        setDataId(useCaseDataId).
                        setDomainId(useCaseDomainId).
                        setParentDomainId(useCaseModel.parentDomainId).
                        setRecipeState(recipeState).
                        setComponentStates(useCaseModel.componentStates).
                        setFailReasons(failReasons).
                        setCreatedBy(Constants.getUserId()).
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
        RecipeMetadataPersistenceModel model = new RecipeMetadataPersistenceModel.Builder().
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
                setParentDomainId(useCaseModel.parentDomainId).
                setComponentStates(new LinkedHashMap<>(useCaseModel.componentStates)).
                build();
    }
}
