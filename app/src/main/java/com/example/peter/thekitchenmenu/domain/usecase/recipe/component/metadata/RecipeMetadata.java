package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
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
                RepositoryRecipeMetadata,
                RecipeMetadataPersistenceModel,
                RecipeMetadata.DomainModel> {

    private static final String TAG = "tkm-" + RecipeMetadata.class.getSimpleName() + ": ";

    protected static final class DomainModel
            implements
            UseCaseDomainModel {

        private String parentDomainId;
        private HashMap<ComponentName, ComponentState> componentStates = new HashMap<>();

        private DomainModel() {}

        private DomainModel(String parentDomainId,
                           HashMap<ComponentName, ComponentState> componentStates) {

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

    public enum ComponentState {
        INVALID_DEFAULT(1),
        INVALID_UNCHANGED(2),
        INVALID_CHANGED(3),
        VALID_DEFAULT(4),
        VALID_UNCHANGED(5),
        VALID_CHANGED(6);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, ComponentState> options = new HashMap<>();

        ComponentState(int id) {
            this.id = id;
        }

        static {
            for (ComponentState c : ComponentState.values())
                options.put(c.id, c);
        }

        public static ComponentState fromInt(int id) {
            return options.get(id);
        }

        public int id() {
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

    private ComponentState recipeState;

    public RecipeMetadata(@Nonnull RepositoryRecipeMetadata repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull Set<ComponentName> requiredComponentNames,
                          @Nonnull Set<ComponentName> additionalComponentNames) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        this.requiredComponentNames = requiredComponentNames;
        this.additionalComponentNames = additionalComponentNames;

        domainModel = new DomainModel();
    }

    @Override
    public void onDomainModelUnavailable() {
        isChanged = true; // Prompts an initial save for a new recipe
        super.onDomainModelUnavailable();
    }

    @Override
    protected DomainModel createDomainModelFromPersistenceModel(
            @Nonnull RecipeMetadataPersistenceModel persistenceModel) {

        recipeState = persistenceModel.getComponentState();
        failReasons.addAll(persistenceModel.getFailReasons());

        return new DomainModel(
                persistenceModel.getParentDomainId(),
                persistenceModel.getComponentStates()
        );
    }

    @Override
    protected DomainModel createDomainModelFromDefaultValues() {
        return new DomainModel(
                NO_ID,
                new HashMap<>()
        );
    }

    @Override
    protected DomainModel createDomainModelFromRequestModel() {
        RecipeMetadataRequest.DomainModel model = ((RecipeMetadataRequest) getRequest()).
                getDomainModel();

        return new DomainModel(
                model.getParentDomainId(),
                model.getComponentStates()
        );
    }

    @Override
    protected void validateDomainModelElements() {
        checkForRequiredComponents();
        checkComponentStates();
        isChanged = isChanged();
        recipeState = getComponentState();
        save();
    }

    private void checkForRequiredComponents() {
        for (ComponentName componentName : requiredComponentNames) {
            if (!domainModel.componentStates.containsKey(componentName)) {
                System.out.println(TAG + "check for required components: adding key" + componentName);
                domainModel.componentStates.put(componentName, ComponentState.INVALID_DEFAULT);
                addFailReasonsMissingComponents();
            }
        }

        System.out.println(TAG + "checkForRequiredComponents: " +
                "\n  - required component names= " + requiredComponentNames +
                "\n  - domainModel.componentStates= " + domainModel.componentStates);
    }

    private void addFailReasonsMissingComponents() {
        if (!failReasons.contains(FailReason.MISSING_REQUIRED_COMPONENTS)) {
            failReasons.add(FailReason.MISSING_REQUIRED_COMPONENTS);
        }
    }

    private void checkComponentStates() {
        ComponentState componentState;

        for (ComponentName componentName : requiredComponentNames) {
            componentState = domainModel.componentStates.get(componentName);

            if (ComponentState.INVALID_UNCHANGED.equals(componentState) ||
                    ComponentState.INVALID_CHANGED.equals(componentState) ||
                    ComponentState.INVALID_DEFAULT.equals(componentState)) {
                addFailReasonInvalidComponents();
            }
        }

        for (ComponentName componentName : additionalComponentNames) {
            componentState = domainModel.componentStates.get(componentName);

            if (ComponentState.INVALID_UNCHANGED.equals(componentState) ||
                    ComponentState.INVALID_CHANGED.equals(componentState)) {
                addFailReasonInvalidComponents();
            }
        }
    }

    private void addFailReasonInvalidComponents() {
        if (!failReasons.contains(FailReason.INVALID_COMPONENTS)) {
            failReasons.add(FailReason.INVALID_COMPONENTS);
        }
    }

    private boolean isChanged() {
        for (ComponentState componentState : domainModel.componentStates.values()) {
            if (ComponentState.INVALID_CHANGED.equals(componentState) ||
                    ComponentState.VALID_CHANGED.equals(componentState)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void save() {
        System.out.println(TAG + "save:" + "isChanged=" + isChanged);

        if (isChanged) {

            useCaseDataId = idProvider.getUId();
            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archiveExistingPersistenceModel(currentTime);
            }

            persistenceModel = new RecipeMetadataPersistenceModel.Builder().
                    setDataId(useCaseDataId).
                    setDomainId(useCaseDomainId).
                    setParentDomainId(domainModel.parentDomainId).
                    setRecipeState(recipeState).
                    setComponentStates(domainModel.componentStates).
                    setFailReasons(failReasons).
                    setCreatedBy(Constants.getUserId()).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }
        buildResponse();
    }

    @Override
    protected void archiveExistingPersistenceModel(long currentTime) {
        RecipeMetadataPersistenceModel model = new RecipeMetadataPersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setLastUpdate(currentTime).
                build();

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
                setParentDomainId(domainModel.parentDomainId).
                setComponentStates(new LinkedHashMap<>(domainModel.componentStates)).
                build();
    }
}
