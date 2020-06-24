package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
                RecipeMetadata.DomainModel>
        implements
        DomainDataAccess.GetDomainModelCallback<RecipeMetadataPersistenceModel> {

    private static final String TAG = "tkm-" + RecipeMetadata.class.getSimpleName() + ": ";

    protected static final class DomainModel
            implements
            UseCaseDomainModel {

        private String parentDomainId;
        private HashMap<ComponentName, ComponentState> componentStates;

        public DomainModel() {
        }

        public DomainModel(String parentDomainId,
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

    public enum FailReason implements FailReasons {
        MISSING_COMPONENTS(400),
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
        INVALID_UNCHANGED(1),
        INVALID_CHANGED(2),
        VALID_UNCHANGED(3),
        VALID_CHANGED(4);

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

    @Nonnull
    private final Set<ComponentName> requiredComponents;

    public RecipeMetadata(@Nonnull RepositoryRecipeMetadata repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull Set<ComponentName> requiredComponents) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        this.requiredComponents = requiredComponents;

        domainModel = new DomainModel();
    }

    @Override
    protected DomainModel createDomainModelFromPersistenceModel(
            @Nonnull RecipeMetadataPersistenceModel persistenceModel) {
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
        calculateState();
    }

    private void setState() {
        if (!isValid() && !isDomainModelChanged()) {
            recipeState = ComponentState.INVALID_UNCHANGED;
        } else if (!isValid() && isDomainModelChanged()) {
            recipeState = ComponentState.INVALID_CHANGED;
        } else if (isValid() && !isDomainModelChanged()) {
            recipeState = ComponentState.VALID_UNCHANGED;
        } else {
            addCommonFailReasons();
            recipeState = ComponentState.VALID_CHANGED;
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (hasRequiredComponents()) {
            for (ComponentState state : componentStates.values()) {
                if (ComponentState.INVALID_UNCHANGED == state ||
                        ComponentState.INVALID_CHANGED == state) {
                    isValid = false;
                    addFailReasonInvalidComponents();
                }
            }
        } else {
            isValid = false;
        }
        if (isValid) {
            addCommonFailReasons();
        }
        return isValid;
    }

    protected boolean isDomainModelChanged() {
        boolean isChanged = false;
        for (ComponentState state : componentStates.values()) {
            if (ComponentState.INVALID_CHANGED == state || ComponentState.VALID_CHANGED == state) {
                isChanged = true;
                break;
            }
        }
        return isChanged;
    }

    private boolean hasRequiredComponents() {
        int noOfRequiredComponents = requiredComponents.size();
        int noOfComponentsSubmitted = 0;

        for (ComponentName componentName : requiredComponents) {
            if (isComponentSateSubmitted(componentName)) {
                noOfComponentsSubmitted++;
            } else {
                componentNotSubmitted(componentName);
            }
        }
        return noOfComponentsSubmitted == noOfRequiredComponents;
    }

    private boolean isComponentSateSubmitted(ComponentName componentName) {
        return componentStates.containsKey(componentName);
    }

    private void componentNotSubmitted(ComponentName componentName) {
        componentStates.put(componentName, ComponentState.INVALID_UNCHANGED);
//        recipeState = ComponentState.INVALID_UNCHANGED;

        addFailReasonsMissingComponents();
    }

    private void addFailReasonsMissingComponents() {
        if (!failReasons.contains(FailReason.MISSING_COMPONENTS)) {
            failReasons.add(FailReason.MISSING_COMPONENTS);
        }
    }

    private void addFailReasonInvalidComponents() {
        if (!failReasons.contains(FailReason.INVALID_COMPONENTS)) {
            failReasons.add(FailReason.INVALID_COMPONENTS);
        }
    }

    protected void buildResponse() {
        RecipeMetadataResponse.Builder builder = new RecipeMetadataResponse.Builder();
        builder.setDomainId(useCaseDomainId);

        if (isDomainModelChanged()) {
            RecipeMetadataPersistenceModel m = updatePersistenceModel();
            builder.setMetadata(getMetadata(m));
            persistenceModel = m;
            save();

        } else {
            builder.setMetadata(getMetadata(persistenceModel));
        }

        builder.setDomainModel(getResponseDomainModel());
        builder.setDataId(useCaseDataId);

        sendResponse(builder.build());
    }

    private UseCaseMetadataModel getMetadata(RecipeMetadataPersistenceModel m) {
        return new UseCaseMetadataModel.Builder().
                setComponentState(recipeState).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(m.getCreateDate()).
                setLastUpdate(m.getLastUpdate()).
                build();
    }

    private RecipeMetadataResponse.Model getResponseDomainModel() {
        return new RecipeMetadataResponse.Model.Builder().
                setParentDomainId(persistenceModel.getParentDomainId()).
                setComponentStates(new LinkedHashMap<>(componentStates)).
                build();
    }

    private RecipeMetadataPersistenceModel updatePersistenceModel() {
        RecipeMetadataRequest r = (RecipeMetadataRequest) getRequest();
        useCaseDataId = idProvider.getUId();
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setParentDomainId(r.getDomainModel().getParentDomainId()).
                setRecipeState(recipeState).
                setComponentStates(componentStates).
                setFailReasons(failReasons).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(persistenceModel.getCreateDate()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
    }

    private void sendResponse(RecipeMetadataResponse r) {
        System.out.println(TAG + "Response No:" + accessCount + " - " + r);
        if (isValid()) {
            getUseCaseCallback().onUseCaseSuccess(r);
        } else {
            getUseCaseCallback().onUseCaseError(r);
        }
    }

    @Override
    protected void save() {
        repository.save(persistenceModel);
    }

    @Override
    protected void archiveExistingPersistenceModel(long currentTime) {

    }
}
