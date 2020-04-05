package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Calculates and stores {@link Recipe} metadata state information based on recipe
 * {@link UseCaseResponse}'s
 *
 * Always use as a {@link Recipe} component
 */
public class RecipeMetadata
        extends UseCase
        implements DataAccess.GetDomainModelCallback<RecipeMetadataPersistenceModel> {

    private static final String TAG = "tkm-" + RecipeMetadata.class.getSimpleName() + ": ";

    public enum RecipeState {
        DATA_UNAVAILABLE(400),
        INVALID_UNCHANGED(401),
        INVALID_CHANGED(402),
        VALID_CHANGED(403),
        VALID_UNCHANGED(404),
        COMPLETE(405);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, RecipeState> options = new HashMap<>();

        RecipeState(int id) {
            this.id = id;
        }

        static {
            for (RecipeState s : RecipeState.values())
                options.put(s.id, s);
        }

        public static RecipeState getById(int id) {
            return options.get(id);
        }

        public int getId() {
            return id;
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

        public static ComponentState getFromId(int id) {
            return options.get(id);
        }

        public int getId() {
            return id;
        }
    }

    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final RepositoryRecipeMetadata repository;
    @Nonnull
    private final Set<ComponentName> requiredComponents;

    @Nonnull
    private final List<FailReasons> failReasons;

    private RecipeMetadataPersistenceModel persistenceModel;
    private RecipeState recipeState;
    private String dataId = "";
    private String recipeId = "";
    private String parentId = "";
    private HashMap<ComponentName, ComponentState> componentStates;
    private RecipeMetadataRequest.Model requestModel;

    private boolean hasRequiredComponents;
    private boolean hasInvalidModels;
    private boolean isNewRequest;

    //  TODO - last update - should be the time of the last updated component
    // TODO - Data layer:
    //  have uid as well as recipeId
    //  keep a copy of all metadata as it changes

    public RecipeMetadata(@Nonnull TimeProvider timeProvider,
                          @Nonnull RepositoryRecipeMetadata repository,
                          @Nonnull Set<ComponentName> requiredComponents) {

        this.timeProvider = timeProvider;
        this.repository = repository;
        this.requiredComponents = requiredComponents;

        recipeState = RecipeState.COMPLETE;
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeMetadataRequest metadataRequest = (RecipeMetadataRequest) request;
        System.out.println(TAG + metadataRequest);

        if (isNewRequest(metadataRequest.getDomainId())) {
            dataId = metadataRequest.getDataId();
            recipeId = metadataRequest.getDomainId();
            loadData(recipeId);
        } else {
            requestModel = metadataRequest.getModel();
            calculateState();
        }
    }

    private boolean isNewRequest(String recipeId) {
        return isNewRequest = !this.recipeId.equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getLatestByDomainId(recipeId, this);
    }

    @Override
    public void onModelLoaded(RecipeMetadataPersistenceModel model) {
        persistenceModel = model;
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

        return new RecipeMetadataPersistenceModel.Builder().
                getDefault().
                setDataId(dataId).
                setRecipeId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void calculateState() {
        checkForRequiredComponents();
        checkForInvalidComponents();
        checkForValidComponents();

        buildResponse();
    }

    private void checkForRequiredComponents() {

        int noOfRequiredComponents = requiredComponents.size();
        int noOfRequiredComponentsSubmitted = 0;

        for (ComponentName componentName : requiredComponents) {
            if (componentStates.containsKey(componentName) &&
                    componentStates.get(componentName) != ComponentState.DATA_UNAVAILABLE) {
                noOfRequiredComponentsSubmitted++;
            } else {
                componentStates.put(componentName, ComponentState.DATA_UNAVAILABLE);
                recipeState = RecipeState.DATA_UNAVAILABLE;
                addFailReasonMissingModels();
            }
        }
        hasRequiredComponents = noOfRequiredComponentsSubmitted >= noOfRequiredComponents;
    }

    private void addFailReasonMissingModels() {
        if (!failReasons.contains(FailReason.MISSING_COMPONENTS)) {
            failReasons.add(FailReason.MISSING_COMPONENTS);
        }
    }

    private void checkForInvalidComponents() {
        if (hasRequiredComponents) {
            for (ComponentName componentName : componentStates.keySet()) {
                ComponentState componentState = componentStates.get(componentName);

                if (ComponentState.INVALID_UNCHANGED == componentState &&
                        recipeState.getId() > RecipeState.INVALID_UNCHANGED.id) {

                    recipeState = RecipeState.INVALID_UNCHANGED;
                    addFailReasonInvalidComponents();

                } else if (componentState == ComponentState.INVALID_CHANGED &&
                        recipeState.getId() > RecipeState.INVALID_CHANGED.id) {

                    recipeState = RecipeState.INVALID_CHANGED;
                    addFailReasonInvalidComponents();
                }
            }
        }
    }

    private void addFailReasonInvalidComponents() {
        hasInvalidModels = true;
        failReasons.add(FailReason.INVALID_COMPONENTS);
    }

    private void checkForValidComponents() {
        if (hasRequiredComponents && !hasInvalidModels) {
            for (ComponentName componentName : componentStates.keySet()) {

                ComponentState componentState = componentStates.get(componentName);
                if (componentState == ComponentState.VALID_UNCHANGED &&
                        recipeState.getId() > RecipeState.VALID_UNCHANGED.id) {

                    recipeState = RecipeState.VALID_UNCHANGED;
                    addFailReasonNone();

                } else if (componentState == ComponentState.VALID_CHANGED &&
                        recipeState.getId() > RecipeState.VALID_CHANGED.id) {

                    recipeState = RecipeState.VALID_CHANGED;
                    addFailReasonNone();
                }
            }
        }
    }

    private void addFailReasonNone() {
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
    }

    private boolean isValid() {
        return recipeState == RecipeState.VALID_UNCHANGED ||
                recipeState == RecipeState.VALID_CHANGED ||
                recipeState == RecipeState.COMPLETE;
    }

    // todo - What should the metadata for the metadata look like? Should it exist?
    private UseCaseMetadata getMetadata() {
        List<FailReasons> failReasons = new ArrayList<>();
        failReasons.add(CommonFailReason.NONE);
        return new UseCaseMetadata.Builder().
                setState(ComponentState.VALID_CHANGED).
                setFailReasons(failReasons).
                setCreateDate(0L).
                setLasUpdate(0L).
                build();
    }

    private void buildResponse() {
        addFailReasonNone();

        RecipeMetadataResponse response = new RecipeMetadataResponse.Builder().
                setId(dataId).
                setModel(getModel()).
                build();

        save(getUpdatedPersistenceModel());

        sendResponse(response);
    }

    private RecipeMetadataResponse.Model getModel() {
        return new RecipeMetadataResponse.Model.Builder().
                setParentId(persistenceModel.getRecipeParentId()).
                setRecipeState(RecipeState.getById(recipeState.getId())).
                setFailReasons(new ArrayList<>(failReasons)).
                setComponentStates(new LinkedHashMap<>(componentStates)).
                build();
    }

    private RecipeMetadataPersistenceModel getUpdatedPersistenceModel() {
        return new RecipeMetadataPersistenceModel.Builder().build();
    }

    private boolean isChanged() {
        return false;
    }

    private void resetState() {
        recipeState = RecipeState.COMPLETE;
        failReasons.clear();
        componentStates.clear();
    }

    private void sendResponse(RecipeMetadataResponse response) {
        System.out.println(TAG + response);
        if (isValid()) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private void save(RecipeMetadataPersistenceModel model) {
        repository.save(model);
    }
}
