package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Calculates and stores {@link Recipe} metadata state information based on recipe component
 * responses.
 * <p>
 * Always use as part of a {@link Recipe} macro component
 */
public class RecipeMetadata
        extends UseCase
        implements DomainDataAccess.GetDomainModelCallback<RecipeMetadataPersistenceModel> {

    private static final String TAG = "tkm-" + RecipeMetadata.class.getSimpleName() + ": ";

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

        public static ComponentState getFromStateLevel(int id) {
            return options.get(id);
        }

        public int stateLevel() {
            return id;
        }
    }

    @Nonnull
    private final RepositoryRecipeMetadata repository;
    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final Set<ComponentName> requiredComponents;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String dataId = "";
    private String recipeId = "";

    private RecipeMetadataPersistenceModel persistenceModel;

    private ComponentState recipeState;
    private HashMap<ComponentName, ComponentState> componentStates;

    private int accessCount;

    public RecipeMetadata(@Nonnull RepositoryRecipeMetadata repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull Set<ComponentName> requiredComponents) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.requiredComponents = requiredComponents;

        recipeState = ComponentState.INVALID_UNCHANGED;
        failReasons = new ArrayList<>();
        componentStates = new HashMap<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        accessCount++;
        RecipeMetadataRequest r = (RecipeMetadataRequest) request;
        System.out.println(TAG + "Request No:" + accessCount + " - " + r);

        if (isNewRequest(r)) {
            dataId = r.getDataId();
            recipeId = r.getDomainId();
            loadData(recipeId);
        } else {
            failReasons.clear();
            componentStates = r.getModel().getComponentStates();
            processChanges();
        }
    }

    private boolean isNewRequest(RecipeMetadataRequest r) {
        return !r.getDomainId().equals(recipeId);
    }

    private void loadData(String recipeDomainId) {
        repository.getActiveByDomainId(recipeDomainId, this);
    }

    @Override
    public void onModelLoaded(RecipeMetadataPersistenceModel model) {
        persistenceModel = model;
        recipeState = model.getRecipeState();
        componentStates = model.getComponentStates();
        failReasons.addAll(model.getFailReasons());
        dataId = model.getDataId();
        processChanges();
    }

    @Override
    public void onModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        processChanges();
    }

    private RecipeMetadataPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        dataId = idProvider.getUId();

        return new RecipeMetadataPersistenceModel.Builder().
                getDefault().
                setDataId(dataId).
                setDomainId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void processChanges() {
        setState();
        buildResponse();
    }

    private void setState() {
        if (hasRequiredComponents()) {
            if (!isValid() && !isChanged()) {
                recipeState = ComponentState.INVALID_UNCHANGED;
            } else if (!isValid() && isChanged()) {
                recipeState = ComponentState.INVALID_CHANGED;
            } else if (isValid() && !isChanged()) {
                recipeState = ComponentState.VALID_UNCHANGED;
            } else {
                addFailReasonNone();
                recipeState = ComponentState.VALID_CHANGED;
            }
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        for (ComponentState state : componentStates.values()) {
            if (ComponentState.INVALID_UNCHANGED == state ||
                    state == ComponentState.INVALID_CHANGED) {
                isValid = false;
                addFailReasonInvalidComponents();
            }
        }
        if (isValid) {
            addFailReasonNone();
        }
        return isValid;
    }

    private boolean isChanged() {
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
        recipeState = ComponentState.INVALID_UNCHANGED;

        if (!failReasons.contains(FailReason.MISSING_COMPONENTS)) {
            failReasons.add(FailReason.MISSING_COMPONENTS);
        }
    }

    private void addFailReasonInvalidComponents() {
        if (!failReasons.contains(FailReason.INVALID_COMPONENTS)) {
            failReasons.add(FailReason.INVALID_COMPONENTS);
        }
    }

    private void addFailReasonNone() {
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
    }

    private void buildResponse() {
        RecipeMetadataResponse.Builder builder = new RecipeMetadataResponse.Builder();
        builder.setDomainId(recipeId);

        if (isChanged()) {
            RecipeMetadataPersistenceModel m = updatePersistenceModel();
            builder.setMetadata(getMetadata(m));
            persistenceModel = m;
            save();

        } else {
            builder.setMetadata(getMetadata(persistenceModel));
        }

        builder.setModel(getResponseModel());
        builder.setDataId(dataId);

        sendResponse(builder.build());
    }

    private UseCaseMetadata getMetadata(RecipeMetadataPersistenceModel m) {
        return new UseCaseMetadata.Builder().
                setState(recipeState).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(m.getCreateDate()).
                setLasUpdate(m.getLastUpdate()).
                build();
    }

    private RecipeMetadataResponse.Model getResponseModel() {
        return new RecipeMetadataResponse.Model.Builder().
                setParentDomainId(persistenceModel.getParentDomainId()).
                setComponentStates(new LinkedHashMap<>(componentStates)).
                build();
    }

    private RecipeMetadataPersistenceModel updatePersistenceModel() {
        RecipeMetadataRequest r = (RecipeMetadataRequest) getRequest();
        dataId = idProvider.getUId();
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(dataId).
                setDomainId(recipeId).
                setParentDomainId(r.getModel().getParentDomainId()).
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

    private void save() {
        repository.save(persistenceModel);
    }
}
