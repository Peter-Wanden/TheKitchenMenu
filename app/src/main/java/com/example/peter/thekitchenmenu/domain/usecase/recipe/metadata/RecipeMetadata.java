package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

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
 * Always use as a {@link Recipe} component
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
        DATA_UNAVAILABLE(1),
        INVALID_UNCHANGED(2),
        INVALID_CHANGED(3),
        VALID_UNCHANGED(4),
        VALID_CHANGED(5);

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
    private boolean isNewRequest;

    private RecipeMetadataPersistenceModel persistenceModel;

    private ComponentState recipeState;
    private HashMap<ComponentName, ComponentState> componentStates;
    private HashMap<ComponentName, ComponentState> oldComponentStates;

    // TODO - last update - should be the time of the last updated component
    // TODO - Data layer:
    //  - keep a copy of all metadata as it changes

    public RecipeMetadata(@Nonnull RepositoryRecipeMetadata repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          @Nonnull Set<ComponentName> requiredComponents) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.requiredComponents = requiredComponents;

        recipeState = ComponentState.VALID_CHANGED;
        failReasons = new ArrayList<>();
        componentStates = new HashMap<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeMetadataRequest r = (RecipeMetadataRequest) request;
        System.out.println(TAG + r);

        if (isNewRequest(r)) {
            dataId = r.getDataId();
            recipeId = r.getDomainId();
            loadData(recipeId);
        } else {
            setupComponent();
            componentStates = r.getModel().getComponentStates();
            calculateState();
        }
    }

    private boolean isNewRequest(RecipeMetadataRequest r) {
        return isNewRequest = !r.getDomainId().equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getActiveByDomainId(recipeId, this);
    }

    @Override
    public void onModelLoaded(RecipeMetadataPersistenceModel model) {
        persistenceModel = model;
        recipeState = model.getRecipeState();
        componentStates = model.getComponentStates();
        failReasons.addAll(model.getFailReasons());
        dataId = model.getDataId();
        buildResponse();
    }

    @Override
    public void onModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        calculateState();
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

    private void setupComponent() {
        recipeState = ComponentState.VALID_CHANGED;
        failReasons.clear();
        oldComponentStates = componentStates;
        componentStates.clear();
    }

    private void calculateState() {
        if (hasRequiredComponents()) {
            checkForInvalidComponents();
            if (isAllComponentsValid()) {
                calculateValidState();
            }
        }
        buildResponse();
    }

    private boolean isAllComponentsValid() {
        return recipeState.stateLevel() > ComponentState.INVALID_CHANGED.stateLevel();
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
        return componentStates.containsKey(componentName) &&
                componentStates.get(componentName) != ComponentState.DATA_UNAVAILABLE;
    }

    private void componentNotSubmitted(ComponentName componentName) {
        componentStates.put(componentName, ComponentState.DATA_UNAVAILABLE);
        recipeState = ComponentState.DATA_UNAVAILABLE;

        if (!failReasons.contains(FailReason.MISSING_COMPONENTS)) {
            failReasons.add(FailReason.MISSING_COMPONENTS);
        }
    }

    private void checkForInvalidComponents() {
        for (ComponentName componentName : componentStates.keySet()) {
            ComponentState componentState = componentStates.get(componentName);

            if (ComponentState.INVALID_UNCHANGED == componentState &&
                    ComponentState.INVALID_UNCHANGED.stateLevel() < recipeState.stateLevel()) {

                recipeState = ComponentState.INVALID_UNCHANGED;
                addFailReasonInvalidComponents();

            } else if (ComponentState.INVALID_CHANGED == componentState &&
                    ComponentState.INVALID_CHANGED.stateLevel() < recipeState.stateLevel()) {

                recipeState = ComponentState.INVALID_CHANGED;
                addFailReasonInvalidComponents();
            }
        }
    }

    private void addFailReasonInvalidComponents() {
        failReasons.add(FailReason.INVALID_COMPONENTS);
    }

    private void calculateValidState() {
        recipeState = ComponentState.VALID_UNCHANGED;

        for (ComponentName componentName : componentStates.keySet()) {
            ComponentState componentState = componentStates.get(componentName);

            if (ComponentState.VALID_CHANGED == componentState) {
                recipeState = ComponentState.VALID_CHANGED;
            }

            addFailReasonNone();
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

    private boolean isChanged() {
        return !isNewRequest && !oldComponentStates.equals(componentStates);
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

    private void sendResponse(RecipeMetadataResponse response) {
        System.out.println(TAG + response);
        if (isValid()) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private boolean isValid() { // Todo, change to: failReasons.contains(CommonFailReason.NONE);
        return recipeState == ComponentState.VALID_UNCHANGED ||
                recipeState == ComponentState.VALID_CHANGED;
    }

    private void save() {
        repository.save(persistenceModel);
    }
}
